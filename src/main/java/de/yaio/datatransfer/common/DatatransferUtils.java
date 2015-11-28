/** 
 * software for projectmanagement and documentation
 * 
 * @FeatureDomain                Collaboration 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.datatransfer.common;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.datadomain.DocLayoutData;
import de.yaio.core.datadomain.IstChildrenSumData;
import de.yaio.core.datadomain.IstData;
import de.yaio.core.datadomain.MetaData;
import de.yaio.core.datadomain.PlanCalcData;
import de.yaio.core.datadomain.PlanChildrenSumData;
import de.yaio.core.datadomain.PlanData;
import de.yaio.core.datadomain.ResContentData;
import de.yaio.core.datadomain.ResContentData.UploadWorkflowState;
import de.yaio.core.datadomain.ResIndexData;
import de.yaio.core.datadomain.ResIndexData.IndexWorkflowState;
import de.yaio.core.datadomain.ResLocData;
import de.yaio.core.datadomain.SymLinkData;
import de.yaio.core.datadomain.SysData;
import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.InfoNode;
import de.yaio.core.node.SymLinkNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.node.UrlResNode;
import de.yaio.core.nodeservice.UrlResNodeService;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImportOptionsImpl;
import de.yaio.datatransfer.jpa.JPAExporter;
import de.yaio.datatransfer.json.JSONFullExporter;
import de.yaio.datatransfer.json.JSONFullImporter;
import de.yaio.datatransfer.json.JSONResponse;
import de.yaio.utils.Calculator;

/** 
 * Services to parse text to nodes and convert them in different 
 * formats (wiki, ppl, excel..)
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class DatatransferUtils {

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DatatransferUtils.class);

    /** 
     * copy the node and all children to the new parent (recalc all and save to db)
     * @FeatureDomain                WikiImporter
     * @FeatureResult                copies and save ne node
     * @FeatureKeywords              WikiImporter
     * @param node                   node to copy
     * @param newParent              parent for new node
     * @throws Exception             ParserExceptions possible
     */
    @Transactional
    public void copyNode(final BaseNode node, final BaseNode newParent) throws Exception {
        // read old parent
        BaseNode oldParent = node.getParentNode();

        // read children for new parent
        newParent.initChildNodesFromDB(0);

        //
        // export node
        //
        node.initChildNodesFromDB(-1);
        JSONFullExporter exporter = new JSONFullExporter();
        OutputOptions oOptions = new OutputOptionsImpl();
        oOptions.setFlgShowIst(false);
        oOptions.setFlgShowSysData(false);
        oOptions.setFlgShowMetaData(false);
        String jsonSrc = exporter.getMasterNodeResult(node, oOptions);

        // create dummy masternode
        BaseNode masterNode = createTemporaryMasternode(
                        newParent.getSysUID(), newParent.getMetaNodePraefix(), newParent.getMetaNodeNummer());
        
        // Parser+Options anlegen
        ImportOptions importOptions = new ImportOptionsImpl();
        importOptions.setAllFlgParse(false);
        importOptions.setFlgParseDesc(true);
        importOptions.setFlgParseDocLayout(true);
        importOptions.setFlgParsePlan(true);
        importOptions.setFlgParsePlanCalc(true);
        importOptions.setFlgParseResLoc(true);
        importOptions.setFlgParseSymLink(true);
        parseNodesFromJson(importOptions, masterNode, jsonSrc);
        
        // JPA-Exporter
        JPAExporter jpaExporter = new JPAExporter();
        jpaExporter.getMasterNodeResult(masterNode, null);

        // renew old parent only if different from newParent
        if (newParent.getSysUID() != oldParent.getSysUID()) {
            // renew oldParent
            oldParent = BaseNode.findBaseNode(oldParent.getSysUID());
            oldParent.initChildNodesFromDB(0);
            
            // recalc old parent
            BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(oldParent);
        }
    }

    /** 
     * move the node and all children to the new parent (recalc all and save to db)
     * @FeatureDomain                WikiImporter
     * @FeatureResult                copies and save ne node
     * @FeatureKeywords              WikiImporter
     * @param node                   node to move
     * @param newParent              new parent for node
     * @param newSortPos             position for node  in childlist of newParent
     * @throws Exception             ParserExceptions possible
     */
    @Transactional
    public void moveNode(final BaseNode node, final BaseNode newParent, final Integer newSortPos) throws Exception {
        // map the data
        boolean flgChangedParent = false;
        boolean flgChangedPosition = false;

        // read children for old parent
        BaseNode oldParent = node.getParentNode();
        oldParent.initChildNodesFromDB(0);

        // read children for both parents
        newParent.initChildNodesFromDB(0);

        // check for new parent
        if (newParent.getSysUID() != oldParent.getSysUID()) {
            // reset sortpos
            node.setSortPos(null);
            flgChangedPosition = true;

            // set new parent
            flgChangedParent = true;
            node.setParentNode(newParent);
        } else {
            // check if position changed
            if (node.getSortPos().intValue() != newSortPos.intValue()) {
                flgChangedPosition = true;
            }
        }

        // check for needed update
        if (flgChangedParent || flgChangedPosition) {
            // recalc the position
            newParent.getBaseNodeService().moveChildToSortPos(newParent, node, newSortPos);

            // save children of newParent
            newParent.saveChildNodesToDB(0, true);

            // recalc and save
            BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(node);

            // renew old parent only if changed
            if (flgChangedParent) {
                // renew oldParent
                oldParent = BaseNode.findBaseNode(oldParent.getSysUID());

                // recalc old parent
                BaseNodeDBServiceImpl.getInstance().updateMeAndMyParents(oldParent);
            }
        } else {
            // read children for node
            node.initChildNodesFromDB(0);
        }
    }

    /** 
     * map the nodeData from newNode to origNode
     * @FeatureDomain                Webservice
     * @FeatureResult                boolean flgChange - true if data changed
     * @FeatureKeywords              Webservice Query
     * @param origNode               
     * @param newNode                the new node created from request-data
     * @return                       true if data changed
     * @throws IllegalAccessException thrown if class of origNode!=newNode
     */
    public boolean mapNodeData(final BaseNode origNode, final BaseNode newNode) 
                    throws IllegalAccessException {
        boolean flgChange = false;
        
        // check class
        if (!origNode.getClassName().equals(newNode.getClassName())) {
            // class differ!!
            throw new IllegalAccessException("cant map origNode (" + origNode.getClassName() + "):" 
                            + origNode.getSysUID() + " with newNode:" + newNode.getClassName());
        }
        
        // common-fields

        // check for new name
        if (Calculator.compareValues(
                        origNode.getName(), newNode.getName()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setName(newNode.getName());
            flgChange = true;
        }
        // check for type
        if (Calculator.compareValues(
                        origNode.getType(), newNode.getType()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setType(newNode.getType());
            flgChange = true;
        }
        // check for nodeDesc
        if (Calculator.compareValues(
                        origNode.getNodeDesc(), newNode.getNodeDesc()) 
                        != Calculator.CONST_COMPARE_EQ) {
            origNode.setNodeDesc(newNode.getNodeDesc());
            flgChange = true;
        }
        
        // check for special nodedata recursively
        
        // Task+EventNodes
        if (TaskNode.class.isInstance(origNode) 
            || EventNode.class.isInstance(origNode)) {
            TaskNode newTaskNode = (TaskNode) newNode;
            TaskNode origTaskNode = (TaskNode) origNode;
            
            // get state from type
            origNode.setState(origNode.getType());
            // check for Plan aufwand
            if (Calculator.compareValues(
                            origTaskNode.getPlanAufwand(), newTaskNode.getPlanAufwand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanAufwand(newTaskNode.getPlanAufwand());
                flgChange = true;
            }
            // check for Plan datestart
            if (Calculator.compareValues(
                            origTaskNode.getPlanStart(), newTaskNode.getPlanStart()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanStart(newTaskNode.getPlanStart());
                flgChange = true;
            }
            // check for Plan dateend
            if (Calculator.compareValues(
                            origTaskNode.getPlanEnde(), newTaskNode.getPlanEnde()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setPlanEnde(newTaskNode.getPlanEnde());
                flgChange = true;
            }
            // check for Ist stand
            if (Calculator.compareValues(
                            origTaskNode.getIstStand(), newTaskNode.getIstStand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstStand(newTaskNode.getIstStand());
                flgChange = true;
            }
            // check for Ist aufwand
            if (Calculator.compareValues(
                            origTaskNode.getIstAufwand(), newTaskNode.getIstAufwand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstAufwand(newTaskNode.getIstAufwand());
                flgChange = true;
            }
            // check for Ist datestart
            if (Calculator.compareValues(
                            origTaskNode.getIstStart(), newTaskNode.getIstStart()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstStart(newTaskNode.getIstStart());
                flgChange = true;
            }
            // check for Ist dateend
            if (Calculator.compareValues(
                            origTaskNode.getIstEnde(), newTaskNode.getIstEnde()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origTaskNode.setIstEnde(newTaskNode.getIstEnde());
                flgChange = true;
            }
        }
        
        // InfoNodes
        if (InfoNode.class.isInstance(origNode)) {
            InfoNode newInfoNode = (InfoNode) newNode;
            InfoNode origInfoNode = (InfoNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());
            // check for DocLayoutTagCommand
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutTagCommand(), newInfoNode.getDocLayoutTagCommand()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutTagCommand(newInfoNode.getDocLayoutTagCommand());
                flgChange = true;
            }
            // check for DocLayoutShortName
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutShortName(), newInfoNode.getDocLayoutShortName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutShortName(newInfoNode.getDocLayoutShortName());
                flgChange = true;
            }
            // check for DocLayoutAddStyleClass
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutAddStyleClass(), newInfoNode.getDocLayoutAddStyleClass()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutAddStyleClass(newInfoNode.getDocLayoutAddStyleClass());
                flgChange = true;
            }
            // check for DocLayoutFlgCloseDiv
            if (Calculator.compareValues(
                            origInfoNode.getDocLayoutFlgCloseDiv(), newInfoNode.getDocLayoutFlgCloseDiv()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origInfoNode.setDocLayoutFlgCloseDiv(newInfoNode.getDocLayoutFlgCloseDiv());
                flgChange = true;
            }
        }

        // UrlResNode
        if (UrlResNode.class.isInstance(origNode)) {
            UrlResNode newUrlResNode = (UrlResNode) newNode;
            UrlResNode origUrlResNode = (UrlResNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());

            // check for ResLocRef
            if (Calculator.compareValues(
                            origUrlResNode.getResLocRef(), newUrlResNode.getResLocRef())
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocRef(newUrlResNode.getResLocRef());
                flgChange = true;
            }
            // check for ResLocName
            if (Calculator.compareValues(
                            origUrlResNode.getResLocName(), newUrlResNode.getResLocName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocName(newUrlResNode.getResLocName());
                flgChange = true;
            }
            // check for ResLocTags
            if (Calculator.compareValues(
                            origUrlResNode.getResLocTags(), newUrlResNode.getResLocTags()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setResLocTags(newUrlResNode.getResLocTags());
                flgChange = true;
            }

            // check for ResContentDMSState for real urls only
            UploadWorkflowState oldUploadState = origUrlResNode.getResContentDMSState();
            UploadWorkflowState newUploadState = newUrlResNode.getResContentDMSState();
            if (UrlResNodeService.CONST_NODETYPE_IDENTIFIER_URLRES.equals(newUrlResNode.getType())) {
                if (Calculator.compareValues(oldUploadState, newUploadState) != Calculator.CONST_COMPARE_EQ
                        && newUploadState != null
                        && newUploadState.equals(UploadWorkflowState.UPLOAD_OPEN)
                        && (oldUploadState == null || !oldUploadState.equals(UploadWorkflowState.UPLOAD_RUNNING))) {
                    origUrlResNode.setResContentDMSState(newUrlResNode.getResContentDMSState());
                    flgChange = true;
                }
            }

            // check for ResIndexDMSState is set to open and upload is open or done
            IndexWorkflowState oldIndexState = origUrlResNode.getResIndexDMSState();
            IndexWorkflowState newIndexState = newUrlResNode.getResIndexDMSState();
            if (Calculator.compareValues(oldIndexState, newIndexState) != Calculator.CONST_COMPARE_EQ
                    && newIndexState != null
                    && newIndexState.equals(IndexWorkflowState.INDEX_OPEN)
                    && (oldIndexState == null || !oldIndexState.equals(IndexWorkflowState.INDEX_RUNNING))
                    && (   (oldUploadState != null
                            && (oldUploadState.equals(UploadWorkflowState.UPLOAD_OPEN)
                                || oldUploadState.equals(UploadWorkflowState.UPLOAD_DONE)))
                        || (newUploadState != null
                            && (newUploadState.equals(UploadWorkflowState.UPLOAD_OPEN))))) {
                origUrlResNode.setResIndexDMSState(newUrlResNode.getResIndexDMSState());
                flgChange = true;
            }
        }
        
        // SymLinkNode
        if (SymLinkNode.class.isInstance(origNode)) {
            SymLinkNode newUrlResNode = (SymLinkNode) newNode;
            SymLinkNode origUrlResNode = (SymLinkNode) origNode;

            // get state from type
            origNode.setState(origNode.getType());

            // check for ResLocRef
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkRef(), newUrlResNode.getSymLinkRef()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkRef(newUrlResNode.getSymLinkRef());
                flgChange = true;
            }
            // check for SymLinkName
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkName(), newUrlResNode.getSymLinkName()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkName(newUrlResNode.getSymLinkName());
                flgChange = true;
            }
            // check for SymLinkTags
            if (Calculator.compareValues(
                            origUrlResNode.getSymLinkTags(), newUrlResNode.getSymLinkTags()) 
                            != Calculator.CONST_COMPARE_EQ) {
                origUrlResNode.setSymLinkTags(newUrlResNode.getSymLinkTags());
                flgChange = true;
            }
        }
        
        // validate
        Set<ConstraintViolation<BaseNode>> violations = newNode.validateMe();
        if (violations.size() > 0) {
            ConstraintViolationException ex = new ConstraintViolationException(
                            "error while validating newNode" + newNode.getNameForLogger(), 
                            new HashSet<ConstraintViolation<?>>(violations));
            throw ex;
        }

        return flgChange;
    }

    /** 
     * parse jsonSrc with JsonImporter and ImportOptions, and add it to the masternode
     * @FeatureDomain                JsonImporter
     * @FeatureResult                adds children from jsonSrc to masterNode
     * @FeatureKeywords              JsonImporter
     * @param importOptions          importOptions for jsonImporter
     * @param masterNode             baseNode to add the children
     * @param jsonSrc                jsonSrc to parse with JsonImporter
     * @throws Exception             ParserExceptions possible
     */
    public void parseNodesFromJson(final ImportOptions importOptions,
                                   final BaseNode masterNode, 
                                   final String jsonSrc) throws Exception {
        // extract 
        JSONFullImporter jsonImporter = new JSONFullImporter(importOptions);
        JSONResponse response = jsonImporter.parseJSONResponse(jsonSrc);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse Response:" + response);
        }
        BaseNode baseNode = (BaseNode) response.getNode();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse Response.node:" + baseNode);
        }
        if ("MasterplanMasternode1".equals(baseNode.getSysUID()) 
            || masterNode.getSysUID().equals(baseNode.getSysUID())) {
            // dont import masternode and parents twice: copy children
            for (BaseNode childNode : baseNode.getChildNodes()) {
                childNode.setParentNode(masterNode);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("add Child:" + childNode.getNameForLogger());
                }
            }
        } else {
            // add masternode
            baseNode.setParentNode(masterNode);
        }
        resetRestrictedData(masterNode, importOptions, true);
        LOGGER.info("masterNode after json:" + masterNode.getBaseNodeService().visualizeNodeHierarchy("", masterNode));
        
    }
    
    /** 
     * create a masternode for import (temporary)
     * @FeatureDomain                Importer
     * @FeatureResult                returns BaseNode
     * @FeatureKeywords              Importer
     * @param sysUID                 sysUID
     * @param nodePraefix            metaNodePraefix
     * @param nodeNummer             metaNodeNumber
     * @return                       the Basenode to use as masternode 
     */
    public BaseNode createTemporaryMasternode(final String sysUID, final String nodePraefix, final String nodeNummer) {
        BaseNode tmpMasterNode = new BaseNode();
        tmpMasterNode.setSysUID(sysUID);
        tmpMasterNode.setMetaNodePraefix(nodePraefix);
        tmpMasterNode.setMetaNodeNummer(nodeNummer);
        tmpMasterNode.setEbene(0);
        return tmpMasterNode;
    }

    /** 
     * call resetDataDomain recursively for all dataDomains not set in ImportOptions
     * @FeatureDomain                Importer
     * @FeatureResult                returns BaseNode
     * @FeatureKeywords              Importer
     * @param node                   the node to reset
     * @param importOptions          importOptions with flags which DataDomains should be resetet
     * @param childrenOnly           reset for children only
     */
    public void resetRestrictedData(final BaseNode node, final ImportOptions importOptions, final boolean childrenOnly) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("resetSysUID:" + node.getNameForLogger());
        }
        if (!childrenOnly) {
            // reset calced data
            if (IstChildrenSumData.class.isInstance(node)) {
                ((IstChildrenSumData) node).resetIstChildrenSumData();
            }
            if (PlanChildrenSumData.class.isInstance(node)) {
                ((PlanChildrenSumData) node).resetPlanChildrenSumData();
            }
            if (ResContentData.class.isInstance(node)) {
                ((ResContentData) node).resetResContentData();
            }
            if (ResIndexData.class.isInstance(node)) {
                ((ResIndexData) node).resetResIndexData();
            }
            
            // reset if flag is not set
            if (!importOptions.isFlgParseDesc()) {
                node.setNodeDesc(null);
            }
            if (!importOptions.isFlgParseDocLayout() && DocLayoutData.class.isInstance(node)) {
                ((DocLayoutData) node).resetDocLayoutData();
            }
            if (!importOptions.isFlgParseIst() && IstData.class.isInstance(node)) {
                ((IstData) node).resetIstData();
            }
            if (!importOptions.isFlgParseMetaData() && MetaData.class.isInstance(node)) {
                ((MetaData) node).resetMetaData();
            }
            if (!importOptions.isFlgParsePlan() && PlanData.class.isInstance(node)) {
                ((PlanData) node).resetPlanData();
            }
            if (!importOptions.isFlgParsePlanCalc() && PlanCalcData.class.isInstance(node)) {
                ((PlanCalcData) node).resetPlanCalcData();
            }
            if (!importOptions.isFlgParseResLoc() && ResLocData.class.isInstance(node)) {
                ((ResLocData) node).resetResLocData();
            }
            if (!importOptions.isFlgParseSymLink() && SymLinkData.class.isInstance(node)) {
                ((SymLinkData) node).resetSymLinkData();
            }
            if (!importOptions.isFlgParseSysData() && SysData.class.isInstance(node)) {
                ((SysData) node).resetSysData();
            }
        }
            
        for (BaseNode childNode : node.getChildNodes()) {
            resetRestrictedData(childNode, importOptions, false);
        }
    }
    
}
