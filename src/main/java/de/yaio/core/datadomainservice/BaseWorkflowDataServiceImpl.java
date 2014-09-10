/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.core.datadomainservice;

import java.util.Date;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.ExtendedWorkflowData;
import de.yaio.core.datadomain.IstChildrenSumData;
import de.yaio.core.datadomain.IstData;
import de.yaio.core.datadomain.PlanChildrenSumData;
import de.yaio.core.datadomain.PlanData;
import de.yaio.core.node.BaseNode;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.utils.Calculator;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for dataDomain: BaseWorkflowData
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseWorkflowDataServiceImpl extends DataDomainRecalcImpl 
    implements BaseWorkflowDataService {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(BaseWorkflowDataServiceImpl.class);


    @Override
    public Class<?> getRecalcTargetClass() {
        return BaseWorkflowData.class;
    }

    @Override
    public int getRecalcTargetOrder() {
        return BaseWorkflowDataService.CONST_RECALC_ORDER;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataExport
     *     Presentation
     * <h4>FeatureDescription:</h4>
     *     add me as DataDomainRecalcer to the Service-Config
     * <h4>FeatureKeywords:</h4>
     *     Config
     * @param nodeService - instance of the nodeService which will call me as recalcer
     */
    public static void configureDataDomainRecalcer(NodeService nodeService) {
        DataDomainRecalc baseDataDomainRecalc  = new BaseWorkflowDataServiceImpl();
        nodeService.addDataDomainRecalcer(baseDataDomainRecalc);
    }
    
    @Override
    public void doRecalcBeforeChildren(DataDomain node, int recurceDirection) throws Exception {
        // NOP
    }

    @Override
    public void doRecalcAfterChildren(DataDomain node, int recurceDirection) throws Exception {
        // Check if node is compatibel
        if (! BaseWorkflowData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        
        // Roll
        this.recalcWorkflowData((BaseWorkflowData)node);
        this.recalcStateData((BaseWorkflowData)node);
    }
    
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     returns the StandFaktor of the BaseWorkflowData-Node<br>
     *     if instance of ExtendedWorkflowData then Plan/IstData is used<br>
     *     if not: 0.0 is set
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Double - standFaktor of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node to process
     * @return standFaktor of the node
     * @throws Exception - parser/format-Exceptions possible
     */
    public Double getMyStandFaktor(BaseWorkflowData node) throws Exception {
        Double standFaktor = 0.0;
        
        // check if node is extended
        if (ExtendedWorkflowData.class.isInstance(node)) {
            // calc from own Workflowdata
            PlanData planData = (ExtendedWorkflowData)node;
            IstData istData = (ExtendedWorkflowData)node;
            standFaktor = (Double)Calculator.calculate(
                    istData.getIstStand(), planData.getPlanAufwand(), 
                    Calculator.CONST_CALCULATE_ACTION_MULSTATE);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("node:" + node.getNameForLogger() 
                        + " mystandfaktor:" + standFaktor 
                        + " plaufwand:" + planData.getPlanAufwand() 
                        + " istStan:" + istData.getIstStand());
            }
        }
        return standFaktor;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     initializes the ChildSumData<br>
     *     if instance of ExtendedWorkflowData then Plan/IstData is used<br>
     *     if not: alle is set to null
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates meberVars of node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param node - node to process
     * @throws Exception - parser/format-Exceptions possible
     */
    public void initChildSumData(BaseWorkflowData node) throws Exception {
        PlanChildrenSumData planChildrenSumData = node;
        IstChildrenSumData istChildrenSumData = node;

        // check if node is extended
        if (ExtendedWorkflowData.class.isInstance(node)) {
            // init with own Workflowdata
            PlanData planData = (ExtendedWorkflowData)node;
            IstData istData = (ExtendedWorkflowData)node;

            planChildrenSumData.setPlanChildrenSumAufwand(planData.getPlanAufwand());
            planChildrenSumData.setPlanChildrenSumStart(planData.getPlanStart());
            planChildrenSumData.setPlanChildrenSumEnde(planData.getPlanEnde());
            istChildrenSumData.setIstChildrenSumAufwand(istData.getIstAufwand());
            istChildrenSumData.setIstChildrenSumStart(istData.getIstStart());
            istChildrenSumData.setIstChildrenSumEnde(istData.getIstEnde());
            istChildrenSumData.setIstChildrenSumStand(istData.getIstStand());
        } else {
            // init with null
            planChildrenSumData.setPlanChildrenSumAufwand(null);
            planChildrenSumData.setPlanChildrenSumStart(null);
            planChildrenSumData.setPlanChildrenSumEnde(null);
            istChildrenSumData.setIstChildrenSumAufwand(null);
            istChildrenSumData.setIstChildrenSumStart(null);
            istChildrenSumData.setIstChildrenSumEnde(null);
            istChildrenSumData.setIstChildrenSumStand(null);
        }
    }

    @Override
    public void recalcWorkflowData(BaseWorkflowData baseNode) throws Exception {
        // init ChildSumData
        this.initChildSumData(baseNode);

        // Standfaktor
        Double standFaktor = this.getMyStandFaktor(baseNode);
        Double tmpStandFaktor = null;

        // iterate children
        for (String nodeName : baseNode.getChildNodesByNameMap().keySet()) {
            BaseWorkflowData childNode = (BaseWorkflowData) baseNode.getChildNodesByNameMap().get(nodeName);
            
            // update ChildrenSum
            baseNode.setPlanChildrenSumAufwand((Double)Calculator.calculate(
                    baseNode.getPlanChildrenSumAufwand(), childNode.getPlanChildrenSumAufwand(), 
                    Calculator.CONST_CALCULATE_ACTION_SUM));
            baseNode.setPlanChildrenSumStart((Date)Calculator.calculate(
                    baseNode.getPlanChildrenSumStart(), childNode.getPlanChildrenSumStart(), 
                    Calculator.CONST_CALCULATE_ACTION_MIN));
            baseNode.setPlanChildrenSumEnde((Date)Calculator.calculate(
                    baseNode.getPlanChildrenSumEnde(), childNode.getPlanChildrenSumEnde(), 
                    Calculator.CONST_CALCULATE_ACTION_MAX));
            baseNode.setIstChildrenSumAufwand((Double)Calculator.calculate(
                    baseNode.getIstChildrenSumAufwand(), childNode.getIstChildrenSumAufwand(), 
                    Calculator.CONST_CALCULATE_ACTION_SUM));
            baseNode.setIstChildrenSumStart((Date)Calculator.calculate(
                    baseNode.getIstChildrenSumStart(), childNode.getIstChildrenSumStart(), 
                    Calculator.CONST_CALCULATE_ACTION_MIN));
            baseNode.setIstChildrenSumEnde((Date)Calculator.calculate(
                    baseNode.getIstChildrenSumEnde(), childNode.getIstChildrenSumEnde(), 
                    Calculator.CONST_CALCULATE_ACTION_MAX));

            // update Standfaktor
            tmpStandFaktor = (Double)Calculator.calculate(
                    childNode.getIstChildrenSumStand(), childNode.getPlanChildrenSumAufwand(), 
                    Calculator.CONST_CALCULATE_ACTION_MULSTATE);
            standFaktor = (Double)Calculator.calculate(
                    standFaktor, tmpStandFaktor, Calculator.CONST_CALCULATE_ACTION_SUM);
        }

        // calc Stand
        if (baseNode.getPlanChildrenSumAufwand() == null 
                        || baseNode.getPlanChildrenSumAufwand() <= Calculator.CONST_DOUBLE_NULL) {
            if (ExtendedWorkflowData.class.isInstance(baseNode)) {
                // calc from own Workflowdata
                IstData istData = (ExtendedWorkflowData)baseNode;
                baseNode.setIstChildrenSumStand(istData.getIstStand());
            } else {
                // no WF-DataDomain
                baseNode.setIstChildrenSumStand(null);
            }
        } else {
            baseNode.setIstChildrenSumStand((Double)Calculator.calculate(
                            standFaktor, baseNode.getPlanChildrenSumAufwand(), 
                            Calculator.CONST_CALCULATE_ACTION_STATE));
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node:" + baseNode.getNameForLogger()
                + " standfaktor:" + standFaktor 
                + " plaufwandcalc:" + baseNode.getPlanChildrenSumAufwand() 
                + " istStandChildrenSum:" + baseNode.getIstChildrenSumStand());
        }
    }

    @Override
    public void recalcStateData(BaseWorkflowData baseNode) throws Exception {
        if (baseNode == null) {
            return;
        }
        baseNode.setState(this.getRecalcedState(baseNode));
    }
    
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     return the recalced state (uses Plan/IstChildrenSum)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - recalced state
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param baseNode - node to process
     * @return recalced state
     * @throws Exception - parser/format-Exceptions possible
     */
    public String getRecalcedState(BaseWorkflowData baseNode) throws Exception {
        if (! ExtendedWorkflowData.class.isInstance(baseNode)) {
           // Normal Node
           return baseNode.getState();
        } else if (EventNode.class.isInstance(baseNode)) {
           // Event
           return this.getRecalcedEventState((ExtendedWorkflowData)baseNode);
        } else if (TaskNode.class.isInstance(baseNode)) {
           // Task
           return this.getRecalcedTaskState((ExtendedWorkflowData)baseNode);
        }
        
        return BaseNode.CONST_NODETYPE_IDENTIFIER_UNKNOWN;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     return the recalced state for a EventNode (uses Plan/IstChildrenSum)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - recalced state
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param baseNode - node to process
     * @return recalced state
     * @throws Exception - parser/format-Exceptions possible
     */
    public String getRecalcedEventState(ExtendedWorkflowData baseNode) {
        // Status aus den Plan/Istzahlen extrahieren
        String newState = baseNode.getState();

        // alles außer Infos betrachten
        if (   newState == null 
                || BaseNode.CONST_NODETYPE_IDENTIFIER_UNKNOWN.equalsIgnoreCase(newState)
                || baseNode.isWFStatus(newState)) {
            // Status zuruecksetzen
            newState = BaseNode.CONST_NODETYPE_IDENTIFIER_UNKNOWN;

            if (    baseNode.getIstChildrenSumStand() != null 
                    && (baseNode.getIstChildrenSumStand() >= Calculator.CONST_DOUBLE_100)) {
                // falls Stand=100 DONE
                newState = EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_DONE;
            } else if (   (baseNode.getPlanAufwand() != null 
                           && (baseNode.getPlanAufwand() >= Calculator.CONST_DOUBLE_NULL))
                    || (baseNode.getPlanChildrenSumAufwand() != null 
                           && (baseNode.getPlanChildrenSumAufwand() >= Calculator.CONST_DOUBLE_NULL))){
                // noch nicht erledigt
                if (   (    baseNode.getIstAufwand() != null 
                            && (baseNode.getIstAufwand() >= Calculator.CONST_DOUBLE_NULL))
                        || (baseNode.getIstChildrenSumAufwand() != null 
                            && (baseNode.getIstChildrenSumAufwand() >= Calculator.CONST_DOUBLE_NULL))
                        || (baseNode.getIstChildrenSumStand() != null 
                            && (baseNode.getIstChildrenSumStand() >= Calculator.CONST_DOUBLE_NULL))
                        ){
                    // wurde schon begonnen
                    newState = EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING;

                    // Termin pruefen
                    Date now = new Date();
                    Date myPlanEnde = baseNode.getPlanChildrenSumEnde();
                    if (myPlanEnde != null && now.after(myPlanEnde)) {
                        // verspaetet: haette schon beeendet sein muessen
                        newState = EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_SHORT;
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Short node:" + baseNode.getNameForLogger() 
                                    + " Stand:" + baseNode.getIstChildrenSumStand() 
                                    +" myPlanEnde:" + myPlanEnde);
                        }
                    }
                } else {
                    // noch nicht begonnen:
                    newState = EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_PLANED;

                    // Termin pruefen
                    Date now = new Date();
                    Date myPlanStart = baseNode.getPlanChildrenSumStart();
                    if (myPlanStart != null && now.after(myPlanStart)) {
                        // verspaetet: haette schon beginnen muessen
                        newState = EventNode.CONST_NODETYPE_IDENTIFIER_EVENT_LATE;
                    }
                }
            }
        }
        return newState;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     return the recalced state for a TaskNode (uses Plan/IstChildrenSum)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - recalced state
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BsuinessLogic
     * @param baseNode - node to process
     * @return recalced state
     * @throws Exception - parser/format-Exceptions possible
     */
    public String getRecalcedTaskState(ExtendedWorkflowData baseNode) {
        // Status aus den Plan/Istzahlen extrahieren
        String newState = baseNode.getState();
        
        LOGGER.debug("WFStatus: state = " + baseNode.isWFStatus(newState)  //$NON-NLS-1$
                        + " Class" + baseNode.getClass().getName()); //$NON-NLS-1$

        // alles außer Infos betrachten
        if (   newState == null 
            || BaseNode.CONST_NODETYPE_IDENTIFIER_UNKNOWN.equalsIgnoreCase(newState)
            || baseNode.isWFStatus(newState)) {
            // Status zuruecksetzen
            newState = BaseNode.CONST_NODETYPE_IDENTIFIER_UNKNOWN;
            
            if (baseNode.getIstChildrenSumStand() != null 
                    && (baseNode.getIstChildrenSumStand() >= Calculator.CONST_DOUBLE_100)) {
                // falls Stand=100 DONE
                newState = TaskNode.CONST_NODETYPE_IDENTIFIER_DONE;
            } else if (   (baseNode.getPlanAufwand() != null 
                           && (baseNode.getPlanAufwand() >= Calculator.CONST_DOUBLE_NULL))
                       || (baseNode.getPlanChildrenSumAufwand() != null 
                           && (baseNode.getPlanChildrenSumAufwand() >= Calculator.CONST_DOUBLE_NULL))
                       || (baseNode.getIstAufwand() != null 
                           && (baseNode.getIstAufwand() >= Calculator.CONST_DOUBLE_NULL))
                       || (baseNode.getIstChildrenSumAufwand() != null 
                           && (baseNode.getIstChildrenSumAufwand() >= Calculator.CONST_DOUBLE_NULL))
                       || (baseNode.getIstChildrenSumStand() != null 
                           && (baseNode.getIstChildrenSumStand() >= Calculator.CONST_DOUBLE_NULL))
                       ){
                // noch nicht erledigt
                if (   (baseNode.getIstAufwand() != null 
                        && (baseNode.getIstAufwand() >= Calculator.CONST_DOUBLE_NULL))
                    || (baseNode.getIstChildrenSumAufwand() != null 
                        && (baseNode.getIstChildrenSumAufwand() >= Calculator.CONST_DOUBLE_NULL))
                    || (baseNode.getIstChildrenSumStand() != null 
                        && (baseNode.getIstChildrenSumStand() >= Calculator.CONST_DOUBLE_NULL))
                    ){
                    // wurde schon begonnen
                    newState = TaskNode.CONST_NODETYPE_IDENTIFIER_RUNNNING;

                    // Termin pruefen
                    Date now = new Date();
                    Date myPlanEnde = baseNode.getPlanChildrenSumEnde();
                    if (myPlanEnde != null && now.after(myPlanEnde)) {
                        // verspaetet: haette schon beeendet sein muessen
                        newState = TaskNode.CONST_NODETYPE_IDENTIFIER_SHORT;
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Short node:" + baseNode.getNameForLogger()
                                    + " Stand:" + baseNode.getIstChildrenSumStand() 
                                    +" myPlanEnde:" + myPlanEnde);
                        }
                    }
                } else {
                    // noch nicht begonnen:
                    newState = TaskNode.CONST_NODETYPE_IDENTIFIER_OPEN;

                    // Termin pruefen
                    Date now = new Date();
                    Date myPlanStart = baseNode.getPlanChildrenSumStart();
                    if (myPlanStart == null) {
                        myPlanStart = baseNode.getPlanStart();
                    }
                    if (myPlanStart != null && now.after(myPlanStart)) {
                        // verspaetet: haette schon beginnen muessen
                        newState = TaskNode.CONST_NODETYPE_IDENTIFIER_LATE;
                    }
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("new NodeState " 
                    + " Stand:" + baseNode.getIstChildrenSumStand() 
                    + " Aufwand:" + baseNode.getPlanChildrenSumAufwand() 
                    +" state" + newState +  " for node:" + baseNode.getNameForLogger());
        }
        return newState;
    }
}
