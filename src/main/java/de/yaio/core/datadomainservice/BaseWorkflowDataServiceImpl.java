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
package de.yaio.core.datadomainservice;

import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.commons.data.DataUtils;
import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.ExtendedWorkflowData;
import de.yaio.core.datadomain.IstChildrenSumData;
import de.yaio.core.datadomain.IstData;
import de.yaio.core.datadomain.PlanCalcData;
import de.yaio.core.datadomain.PlanChildrenSumData;
import de.yaio.core.datadomain.PlanData;
import de.yaio.core.datadomain.PlanDependencieData.PredecessorDependencieType;
import de.yaio.core.node.EventNode;
import de.yaio.core.node.TaskNode;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.utils.Calculator;
import de.yaio.utils.PredecessorCalculator;

/** 
 * businesslogic for dataDomain: BaseWorkflowData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseWorkflowDataServiceImpl extends DataDomainRecalcImpl 
    implements BaseWorkflowDataService {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(BaseWorkflowDataServiceImpl.class);

    private static BaseWorkflowDataServiceImpl instance = new BaseWorkflowDataServiceImpl();
    
    /** 
     * return the main instance of this service
     * @return                       the main instance of this service
     */
    public static BaseWorkflowDataServiceImpl getInstance() {
        return instance;
    }

    @Override
    public Class<?> getRecalcTargetClass() {
        return BaseWorkflowData.class;
    }

    @Override
    public int getRecalcTargetOrder() {
        return BaseWorkflowDataService.CONST_RECALC_ORDER;
    }

    /** 
     * add me as DataDomainRecalcer to the Service-Config
     * @param nodeService            instance of the nodeService which will call me as recalcer
     */
    public static void configureDataDomainRecalcer(final NodeService nodeService) {
        DataDomainRecalc baseDataDomainRecalc  = BaseWorkflowDataServiceImpl.getInstance();
        nodeService.addDataDomainRecalcer(baseDataDomainRecalc);
    }
    
    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final int recurceDirection) throws Exception {
        
        // Check if node is compatibel
        if (!BaseWorkflowData.class.isInstance(node)) {
                throw new IllegalArgumentException();
            }
        
        // Roll
        // TODO this.calcPlanData((BaseWorkflowData) node);
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final int recurceDirection) throws Exception {
        // Check if node is compatibel
        if (!BaseWorkflowData.class.isInstance(node)) {
                throw new IllegalArgumentException();
        }
        
        // Roll
        this.recalcWorkflowData((BaseWorkflowData) node);
        this.recalcStateData((BaseWorkflowData) node);
    }
    
    /** 
     * returns the StandFaktor of the BaseWorkflowData-Node<br>
     * if instance of ExtendedWorkflowData then Plan/IstData is used<br>
     * if not: 0.0 is set
     * @param node                   node to process
     * @return                       standFaktor of the node
     * @throws Exception             parser/format-Exceptions possible
     */
    public Double getMyStandFaktor(final BaseWorkflowData node) throws Exception {
        Double standFaktor = 0.0;
        
        // check if node is extended
        if (ExtendedWorkflowData.class.isInstance(node)) {
            // calc from own Workflowdata
            PlanData planData = (ExtendedWorkflowData) node;
            IstData istData = (ExtendedWorkflowData) node;
            standFaktor = (Double) Calculator.calculate(
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
     * initializes the ChildSumData<br>
     * if instance of ExtendedWorkflowData then Plan/IstData is used<br>
     * if not: alle is set to null
     * @param node                   node to process
     * @throws Exception             parser/format-Exceptions possible
     */
    public void initChildSumData(final BaseWorkflowData node) throws Exception {
        PlanChildrenSumData planChildrenSumData = node;
        IstChildrenSumData istChildrenSumData = node;

        // check if node is extended
        if (ExtendedWorkflowData.class.isInstance(node)) {
            // init with own Workflowdata
            PlanData planData = (ExtendedWorkflowData) node;
            // TODO switch from Plan to PlanCalc
//            PlanCalcData planCalcData = (ExtendedWorkflowData) node;
            IstData istData = (ExtendedWorkflowData) node;

            planChildrenSumData.setPlanChildrenSumAufwand(planData.getPlanAufwand());
            // TODO switch from Plan to PlanCalc
//            planChildrenSumData.setPlanChildrenSumStart(planCalcData.getPlanCalcStart());
//            planChildrenSumData.setPlanChildrenSumEnde(planCalcData.getPlanCalcEnde());
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
    public void calcPlanData(final BaseWorkflowData baseNode) throws Exception {
        // init calcedData
        if (ExtendedWorkflowData.class.isInstance(baseNode)) {
            // init with own Workflowdata
            PlanData planData = (ExtendedWorkflowData) baseNode;
            baseNode.setPlanCalcStart(planData.getPlanStart());
            baseNode.setPlanCalcEnde(planData.getPlanEnde());
        }

        // calc from predecessor
        Date predecessorStart = getCalcedDateFromPredecessor(baseNode, true);
        Date predecessorEnde = getCalcedDateFromPredecessor(baseNode, false);

        // init with dependencies
        if (baseNode.getPlanCalcStart() != null && baseNode.getPlanCalcEnde() != null) {
            // we are set -> NOOP
        } else if (baseNode.getPlanCalcStart() != null) {
            // myStart is set -> calc myEnd
            if (baseNode.getPlanDuration() != null) {
                // calc myEnd from duration
                Date newEndData = PredecessorCalculator.addDurationToDate(
                                baseNode.getPlanCalcStart(), 
                                true, 
                                baseNode.getPlanDuration(), 
                                baseNode.getPlanDurationMeasure());
                baseNode.setPlanCalcEnde(newEndData);
            } else if (predecessorEnde != null 
                       && !predecessorEnde.before(baseNode.getPlanCalcStart())) {
                // get myEnde from valid predecessor
                baseNode.setPlanCalcEnde(predecessorEnde);
            }
        } else if (baseNode.getPlanCalcEnde() != null) {
            // myEnd is set -> calc myStart
            if (baseNode.getPlanDuration() != null) {
                // calc myStart from duration
                Date newStartData = PredecessorCalculator.addDurationToDate(
                                baseNode.getPlanCalcEnde(), 
                                false, 
                                baseNode.getPlanDuration(), 
                                baseNode.getPlanDurationMeasure());
                baseNode.setPlanCalcStart(newStartData);
            } else if (predecessorStart != null 
                       && !predecessorStart.after(baseNode.getPlanCalcEnde())) {
                // get myStart from valid predecessor
                baseNode.setPlanCalcStart(predecessorStart);
            }
        } else {
            // nothing set: get all from predecessor
            baseNode.setPlanCalcStart(predecessorStart);
            baseNode.setPlanCalcEnde(predecessorEnde);
        }
        
        // calc planCalcCheckSum
        String planCalcCheckSum = getPlanCalcCheckSum(baseNode);
        if (baseNode.getPlanCalcCheckSum() == null) {
            // planCalcChecksum empty -> set new
            baseNode.setPlanCalcCheckSum(planCalcCheckSum);
            baseNode.setFlgForceUpdate(true);
        } else if (!planCalcCheckSum.equals(baseNode.getPlanCalcCheckSum())) {
            // planCalcChecksum differs
            baseNode.setPlanCalcCheckSum(planCalcCheckSum);
            baseNode.setFlgForceUpdate(true);
        }
    }
    
    public String getPlanCalcCheckSum(final PlanCalcData node) throws Exception {
        // Daten holen
        String data = getDataBlocks4PlanCalcCheckSum(node);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getDataBlocks4PlanCalcCheckSum = " + data);
        }

        // Checksumme
        return DataUtils.generateCheckSum(data);
    }

    @XmlTransient
    @JsonIgnore
    public String getDataBlocks4PlanCalcCheckSum(final PlanCalcData node) throws Exception {
        // Content erzeugen
        StringBuffer data = new StringBuffer();
        
        data.append(" planCalcStart=").append(DataUtils.getNewDate(node.getPlanCalcStart()))
            .append(" planCalcEnde=").append(DataUtils.getNewDate(node.getPlanCalcEnde()));
        return data.toString();
    }
    
    
    public Date getCalcedDateFromPredecessor(final BaseWorkflowData baseNode, final boolean flgStart) {
        Date date = null;
        
        // get and check predecessor
        BaseWorkflowData predecessor = getPredecessor(baseNode);
        if (predecessor == null) {
            return date;
        }
        if (baseNode.getPlanPredecessorDependencieType() == PredecessorDependencieType.NO) {
            // no dependency
            return null;
        }
        
        // get date for dependencieType
        if (baseNode.getPlanPredecessorDependencieType() == PredecessorDependencieType.inherit) {
            // inherit the dates
            if (flgStart) {
                date = predecessor.getPlanCalcStart();
            } else {
                date = predecessor.getPlanCalcEnde();
            }
        } else if (baseNode.getPlanPredecessorDependencieType() == PredecessorDependencieType.EndEnd) {
            // myEnd = yourEnd
            if (flgStart) {
                // start = yourEnd - myduration
                date = PredecessorCalculator.addDurationToDate(
                                predecessor.getPlanCalcEnde(), 
                                false, 
                                baseNode.getPlanDuration(), 
                                baseNode.getPlanDurationMeasure());
            } else {
                date = predecessor.getPlanCalcEnde();
            }
        } else if (baseNode.getPlanPredecessorDependencieType() == PredecessorDependencieType.EndStart) {
            // myStart = yourEnd
            if (flgStart) {
                // start = yourEnd
                date = predecessor.getPlanCalcEnde();
            } else {
                // end = yourEnd + myduration
                date = PredecessorCalculator.addDurationToDate(
                                predecessor.getPlanCalcEnde(), 
                                true, 
                                baseNode.getPlanDuration(), 
                                baseNode.getPlanDurationMeasure());
            }
        } else if (baseNode.getPlanPredecessorDependencieType() == PredecessorDependencieType.StartStart) {
            // myStart = yourStart
            if (flgStart) {
                // start = yourStart
                date = predecessor.getPlanCalcStart();
            } else {
                // end = yourStart + myduration
                date = PredecessorCalculator.addDurationToDate(
                                predecessor.getPlanCalcStart(), 
                                true, 
                                baseNode.getPlanDuration(), 
                                baseNode.getPlanDurationMeasure());
            }
        }

        // add potential shift
        date = PredecessorCalculator.addDurationToDate(
                        date, 
                        true, 
                        baseNode.getPlanPredecessorShift(), 
                        baseNode.getPlanPredecessorShiftMeasure());
        
        return date;
    }
    
    public BaseWorkflowData getPredecessor(final BaseWorkflowData baseNode) {
        BaseWorkflowData predecessor = null;
        predecessor = baseNode.getPlanPredecessor();
        return predecessor;
    }
    
    @Override
    public void recalcWorkflowData(final BaseWorkflowData baseNode) throws Exception {
        // init ChildSumData
        this.initChildSumData(baseNode);
        
        // init my workflowState
        baseNode.setWorkflowState(calcMyWorkflowState(baseNode));

        // Standfaktor
        Double standFaktor = this.getMyStandFaktor(baseNode);
        Double tmpStandFaktor = null;
        Double sumStandNullAufwand = 0.0;
        Integer countStandNullAufwand = 0;

        // iterate children
        for (String nodeName : baseNode.getChildNodesByNameMap().keySet()) {
            BaseWorkflowData childNode = (BaseWorkflowData) baseNode.getChildNodesByNameMap().get(nodeName);
            
            // update ChildrenSum
            baseNode.setPlanChildrenSumAufwand((Double) Calculator.calculate(
                    baseNode.getPlanChildrenSumAufwand(), childNode.getPlanChildrenSumAufwand(), 
                    Calculator.CONST_CALCULATE_ACTION_SUM));
            baseNode.setPlanChildrenSumStart((Date) Calculator.calculate(
                    baseNode.getPlanChildrenSumStart(), childNode.getPlanChildrenSumStart(), 
                    Calculator.CONST_CALCULATE_ACTION_MIN));
            baseNode.setPlanChildrenSumEnde((Date) Calculator.calculate(
                    baseNode.getPlanChildrenSumEnde(), childNode.getPlanChildrenSumEnde(), 
                    Calculator.CONST_CALCULATE_ACTION_MAX));
            baseNode.setIstChildrenSumAufwand((Double) Calculator.calculate(
                    baseNode.getIstChildrenSumAufwand(), childNode.getIstChildrenSumAufwand(), 
                    Calculator.CONST_CALCULATE_ACTION_SUM));
            baseNode.setIstChildrenSumStart((Date) Calculator.calculate(
                    baseNode.getIstChildrenSumStart(), childNode.getIstChildrenSumStart(), 
                    Calculator.CONST_CALCULATE_ACTION_MIN));
            baseNode.setIstChildrenSumEnde((Date) Calculator.calculate(
                    baseNode.getIstChildrenSumEnde(), childNode.getIstChildrenSumEnde(), 
                    Calculator.CONST_CALCULATE_ACTION_MAX));

            // update Standfaktor
            tmpStandFaktor = (Double) Calculator.calculate(
                    childNode.getIstChildrenSumStand(), childNode.getPlanChildrenSumAufwand(), 
                    Calculator.CONST_CALCULATE_ACTION_MULSTATE);
            standFaktor = (Double) Calculator.calculate(
                    standFaktor, tmpStandFaktor, Calculator.CONST_CALCULATE_ACTION_SUM);
            
            // update sumStandNullAufwand
            if (childNode.getIstChildrenSumStand() != null 
                && !Calculator.isAufwand(childNode.getPlanChildrenSumAufwand())) {
                sumStandNullAufwand += childNode.getIstChildrenSumStand();
                countStandNullAufwand++;
            }
            
            // sync WorkflowState with childrens state
            baseNode.setWorkflowState((WorkflowState) Calculator.calculate(
                            childNode.getWorkflowState(), 
                            baseNode.getWorkflowState(), 
                            Calculator.CONST_CALCULATE_ACTION_WORKFLOWSTATE));
        }

        // calc Stand
        // TODO: Bugfix wenn kein Aufwand definiert
        // if me and children 
        if (!Calculator.isAufwand(baseNode.getPlanChildrenSumAufwand())) {
            Double istStand = null;
            if (ExtendedWorkflowData.class.isInstance(baseNode)) {
                // calc from own Workflowdata
                IstData istData = (ExtendedWorkflowData) baseNode;
                PlanData planData = (ExtendedWorkflowData) baseNode;
                istStand = istData.getIstStand();
                
                // but take a look if there is no planaufwand!!!
                if (istData.getIstStand() != null 
                    && !Calculator.isAufwand(planData.getPlanAufwand())) {
                    sumStandNullAufwand += istData.getIstStand();
                    countStandNullAufwand++;
                }
                // calc the average of all children
                if (countStandNullAufwand > 0) {
                    istStand = sumStandNullAufwand / countStandNullAufwand;
                }
                baseNode.setIstChildrenSumStand(istStand);
            } else {
                // no WF-DataDomain
                // calc the average of all children
                if (countStandNullAufwand > 0) {
                    istStand = sumStandNullAufwand / countStandNullAufwand;
                }
                baseNode.setIstChildrenSumStand(istStand);
            }
        } else {
            baseNode.setIstChildrenSumStand((Double) Calculator.calculate(
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
    public void recalcStateData(final BaseWorkflowData baseNode) throws Exception {
        if (baseNode == null) {
            return;
        }
        baseNode.setState(this.getRecalcedState(baseNode));
    }
    
    
    /** 
     * return the recalced state (uses Plan/IstChildrenSum)
     * @param baseNode               node to process
     * @return                       recalced state
     * @throws Exception             parser/format-Exceptions possible
     */
    public String getRecalcedState(final BaseWorkflowData baseNode) throws Exception {
        if (!ExtendedWorkflowData.class.isInstance(baseNode)) {
           // Normal Node
           return baseNode.getState();
        } else if (EventNode.class.isInstance(baseNode)) {
           // Event
           return baseNode.getBaseNodeService().getStateForWorkflowState(baseNode);
        } else if (TaskNode.class.isInstance(baseNode)) {
           // Task
            return baseNode.getBaseNodeService().getStateForWorkflowState(baseNode);
        }
        
        return BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN;
    }

    /** 
     * return the recalced workflowstate for a BaseNode (uses Plan/Ist)
     * @param node                   node to process
     * @return                       recalced state
     * @throws Exception             parser/format-Exceptions possible
     */
    public WorkflowState calcMyWorkflowState(final BaseWorkflowData node) {
        WorkflowState newState = node.getWorkflowState();
        LOGGER.debug("WFStatus: state = " + newState
                        + " Class" + node.getClass().getName());

        // alles außer Infos betrachten
        if (newState != WorkflowState.CANCELED 
            && newState != WorkflowState.NOWORKFLOW) {
            // check if node is extended
            if (ExtendedWorkflowData.class.isInstance(node)) {
                // Status aus den Plan/Istzahlen extrahieren
                ExtendedWorkflowData baseNode = (ExtendedWorkflowData) node; 
                
                // Status zuruecksetzen
                newState = WorkflowState.NOTPLANED;
                
                if (baseNode.getIstStand() != null 
                        && (baseNode.getIstStand() >= Calculator.CONST_DOUBLE_100)) {
                    // falls Stand=100 DONE
                    newState = WorkflowState.DONE;
                } else if (Calculator.isAufwand(baseNode.getPlanAufwand())
                           || Calculator.isAufwand(baseNode.getIstAufwand())
                           || Calculator.isStand(baseNode.getIstStand())
                           || (baseNode.getPlanStart() != null)
                           || (baseNode.getPlanEnde() != null)
                           ) {
                    // noch nicht erledigt
                    if (Calculator.isAufwand(baseNode.getIstAufwand())
                        || Calculator.isStand(baseNode.getIstStand())
                        ) {
                        // wurde schon begonnen
                        newState = WorkflowState.RUNNING;
    
                    } else {
                        // noch nicht begonnen:
                        newState = WorkflowState.OPEN;
    
                        // Termin pruefen
                        Date now = new Date();
                        Date myPlanStart = baseNode.getPlanStart();
                        // TODO switch from Plan to PlanCalc
//                        Date myPlanStart = baseNode.getPlanCalcStart();
                        if (myPlanStart != null && now.after(myPlanStart)) {
                            // verspaetet: haette schon beginnen muessen
                            newState = WorkflowState.LATE;
                        }
                    }
                    
                    // Termin pruefen
                    Date now = new Date();
                    // TODO switch from Plan to PlanCalc
//                  Date myPlanEnde = baseNode.getPlanCalcEnde();
                    Date myPlanEnde = baseNode.getPlanEnde();
                    if (myPlanEnde != null && now.after(myPlanEnde)) {
                        // verspaetet: haette schon beeendet sein muessen
                        newState = WorkflowState.WARNING;
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Short node:" + baseNode.getNameForLogger()
                                    + " Stand:" + baseNode.getIstStand() 
                                    + " myPlanEnde:" + myPlanEnde);
                        }
                    }
                    
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("new NodeState " 
                            + " Stand:" + baseNode.getIstStand() 
                            + " Aufwand:" + baseNode.getPlanAufwand() 
                            + " state" + newState +  " for node:" + baseNode.getNameForLogger());
                }
            } else {
                // its no WorkflowNode
                newState = WorkflowState.NOWORKFLOW;
            }
        }
        return newState;
    }

    @Override
    public Date calcCurrentStart(final ExtendedWorkflowData node) {
        // wenn belegt IST-Daten benutzen, sonst Plandaten
        Date curStart = node.getIstStart();
        if (curStart != null) {
            return curStart;
        }

        // TODO switch from Plan to PlanCalc
//        return node.getPlanCalcStart();
        return node.getPlanStart();
    }

    @Override
    public Date calcCurrentEnde(final ExtendedWorkflowData node) {
        // wenn belegt IST-Daten benutzen, sonst Plandaten
        Date curEnde = node.getIstEnde();
        // TODO switch from Plan to PlanCalc
//        Date curPlanEnde = node.getPlanCalcEnde();
        Date curPlanEnde = node.getPlanEnde();
        Date curIstStart = node.getIstStart();
        Date curIstEnde = node.getIstEnde();
        Double curIstStan = node.getIstStand();
        if (curEnde != null) {
            if (curPlanEnde != null && curIstStart != null) {
                // wenn Istdaten vor Plandaten und noch nicht fertig, dann Plandaten
                if (curIstEnde.before(curPlanEnde) && curIstStan < 100) {
                    return curPlanEnde;
                }
            }
            return curIstEnde;
        }

        return curPlanEnde;
    }
}
