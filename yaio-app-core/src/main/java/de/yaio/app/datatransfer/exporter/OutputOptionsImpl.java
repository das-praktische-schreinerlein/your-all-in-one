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
package de.yaio.app.datatransfer.exporter;

import de.yaio.app.core.dbservice.SearchOptionsImpl;

/** 
 * options for export of Nodes, initialized with default-values
 * 
 * @FeatureDomain                DatenExport
 * @package                      de.yaio.datatransfer.exporter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class OutputOptionsImpl extends SearchOptionsImpl implements OutputOptions {
    protected boolean flgDoIntend = true;
    protected boolean flgShowBrackets = true;
    protected Integer intendFuncArea = 80;
    protected boolean flgIntendSum = false;

    protected Integer maxUeEbene = 3;
    protected Integer intend = 2;
    protected Integer intendLi = 2;
    protected Integer intendSys = 160;
    protected boolean flgTrimDesc = true;
    protected boolean flgReEscapeDesc = true;
    protected boolean flgShowDescWithUe = false;
    protected boolean flgShowDescInNextLine = false;

    protected boolean flgShowChildrenSum = false;
    protected boolean flgShowDesc = true;
    protected boolean flgShowDocLayout = true;
    protected boolean flgShowIst = true;
    protected boolean flgShowMetaData = true;
    protected boolean flgShowName = true;
    protected boolean flgShowPlan = true;
    protected boolean flgShowPlanCalc = false;
    protected boolean flgShowResLoc = true;
    protected boolean flgShowState = true;
    protected boolean flgShowSymLink = true;
    protected boolean flgShowSysData = true;
    protected boolean flgShowType = true;

    protected boolean flgRecalc = false;
    protected boolean flgProcessDocLayout = false;
    protected boolean flgUsePublicBaseRef = false;

    
    public OutputOptionsImpl() {
        super();
    }

    public OutputOptionsImpl(final OutputOptions baseOptions) {
        super();
        this.flgDoIntend = baseOptions.isFlgDoIntend();
        this.flgShowBrackets = baseOptions.isFlgShowBrackets();
        this.intendFuncArea = baseOptions.getIntendFuncArea();
        this.flgIntendSum = baseOptions.isFlgIntendSum();
        this.maxEbene = baseOptions.getMaxEbene();
        this.maxUeEbene = baseOptions.getMaxUeEbene();
        this.intend = baseOptions.getIntend();
        this.intendLi = baseOptions.getIntendLi();
        this.intendSys = baseOptions.getIntendSys();
        this.flgConcreteToDosOnly = baseOptions.getFlgConcreteToDosOnly();
        this.flgTrimDesc = baseOptions.isFlgTrimDesc();
        this.flgShowType = baseOptions.isFlgShowType();
        this.flgShowState = baseOptions.isFlgShowState();
        this.flgShowName = baseOptions.isFlgShowName();
        this.flgShowDocLayout = baseOptions.isFlgShowDocLayout();
        this.flgShowPlan = baseOptions.isFlgShowPlan();
        this.flgShowPlanCalc = baseOptions.isFlgShowPlanCalc();
        this.flgShowIst = baseOptions.isFlgShowIst();
        this.flgShowChildrenSum = baseOptions.isFlgShowChildrenSum();
        this.flgShowMetaData = baseOptions.isFlgShowMetaData();
        this.flgShowSysData = baseOptions.isFlgShowSysData();
        this.flgShowDesc = baseOptions.isFlgShowDesc();
        this.flgRecalc = baseOptions.isFlgRecalc();
        this.flgProcessDocLayout = baseOptions.isFlgProcessDocLayout();
        this.flgUsePublicBaseRef = baseOptions.isFlgUsePublicBaseRef();
        this.strNotNodePraefix = baseOptions.getStrNotNodePraefix();
        this.strReadIfStatusInListOnly = baseOptions.getStrReadIfStatusInListOnly();
        this.strClassFilter = baseOptions.getStrClassFilter();
        this.strTypeFilter = baseOptions.getStrTypeFilter();
        this.strMetaNodeSubTypeFilter = baseOptions.getStrMetaNodeSubTypeFilter();
        this.strMetaNodeTypeTagsFilter = baseOptions.getStrMetaNodeTypeTagsFilter();

        this.initFilterMaps();
    }

    @Override
    public boolean isFlgDoIntend() {
        return flgDoIntend;
    }
    @Override
    public void setFlgDoIntend(final boolean flgDoIntend) {
        this.flgDoIntend = flgDoIntend;
    }
    @Override
    public boolean isFlgIntendSum() {
        return flgIntendSum;
    }
    @Override
    public void setFlgIntendSum(final boolean flgIntendSum) {
        this.flgIntendSum = flgIntendSum;
    }
    @Override
    public boolean isFlgShowChildrenSum() {
        return flgShowChildrenSum;
    }
    @Override
    public void setFlgShowChildrenSum(final boolean flgShowChildrenSum) {
        this.flgShowChildrenSum = flgShowChildrenSum;
    }
    @Override
    public int getIntendFuncArea() {
        return manageIntValues(intendFuncArea);
    }
    @Override
    public void setIntendFuncArea(final Integer intendPlanToPos) {
        this.intendFuncArea = intendPlanToPos;
    }
    @Override
    public boolean isFlgShowBrackets() {
        return flgShowBrackets;
    }
    @Override
    public void setFlgShowBrackets(final boolean flgShowBrackets) {
        this.flgShowBrackets = flgShowBrackets;
    }

    @Override
    public boolean isFlgShowName() {
        return flgShowName;
    }
    @Override
    public void setFlgShowName(final boolean flgShowName) {
        this.flgShowName = flgShowName;
    }
    @Override
    public boolean isFlgShowResLoc() {
        return flgShowResLoc;
    }

    @Override
    public void setFlgShowResLoc(final boolean flgShowResLoc) {
        this.flgShowResLoc = flgShowResLoc;
    }

    @Override
    public boolean isFlgShowSymLink() {
        return flgShowSymLink;
    }

    @Override
    public void setFlgShowSymLink(final boolean flgShowSymLink) {
        this.flgShowSymLink = flgShowSymLink;
    }

    @Override
    public boolean isFlgShowPlan() {
        return flgShowPlan;
    }
    @Override
    public void setFlgShowPlan(final boolean flgShowPlan) {
        this.flgShowPlan = flgShowPlan;
    }
    @Override
    public boolean isFlgShowPlanCalc() {
        return flgShowPlanCalc;
    }
    @Override
    public void setFlgShowPlanCalc(final boolean flgShowPlanCalc) {
        this.flgShowPlanCalc = flgShowPlanCalc;
    }
    @Override
    public boolean isFlgShowIst() {
        return flgShowIst;
    }
    @Override
    public void setFlgShowIst(final boolean flgShowIst) {
        this.flgShowIst = flgShowIst;
    }
    @Override
    public boolean isFlgShowDesc() {
        return flgShowDesc;
    }
    @Override
    public void setFlgShowDesc(final boolean flgShowDesc) {
        this.flgShowDesc = flgShowDesc;
    }
    @Override
    public boolean isFlgShowType() {
        return flgShowType;
    }
    @Override
    public void setFlgShowType(final boolean flgShowType) {
        this.flgShowType = flgShowType;
    }
    @Override
    public boolean isFlgShowState() {
        return flgShowState;
    }
    @Override
    public void setFlgShowState(final boolean flgShowState) {
        this.flgShowState = flgShowState;
    }
    @Override
    public boolean isFlgShowDocLayout() {
        return flgShowDocLayout;
    }
    @Override
    public void setFlgShowDocLayout(final boolean flgShowDocLayout) {
        this.flgShowDocLayout = flgShowDocLayout;
    }
    @Override
    public boolean isFlgShowMetaData() {
        return flgShowMetaData;
    }
    @Override
    public void setFlgShowMetaData(final boolean flgShowMetaData) {
        this.flgShowMetaData = flgShowMetaData;
    }
    @Override
    public boolean isFlgShowSysData() {
        return flgShowSysData;
    }
    @Override
    public void setFlgShowSysData(final boolean flgShowSysData) {
        this.flgShowSysData = flgShowSysData;
    }
    @Override
    public boolean isFlgRecalc() {
        return flgRecalc;
    }
    @Override
    public void setFlgRecalc(final boolean flgRecalc) {
        this.flgRecalc = flgRecalc;
    }
    @Override
    public boolean isFlgProcessDocLayout() {
        return flgProcessDocLayout;
    }
    @Override
    public void setFlgProcessDocLayout(final boolean flgProcessDocLayout) {
        this.flgProcessDocLayout = flgProcessDocLayout;
    }
    @Override
    public boolean isFlgUsePublicBaseRef() {
        return flgUsePublicBaseRef;
    }
    @Override
    public void setFlgUsePublicBaseRef(final boolean flgUsePublicBaseRef) {
        this.flgUsePublicBaseRef = flgUsePublicBaseRef;
    }
    @Override
    public int getMaxEbene() {
        return manageIntValues(maxEbene);
    }
    @Override
    public void setMaxEbene(final Integer maxEbene) {
        this.maxEbene = maxEbene;
    }
    @Override
    public int getMaxUeEbene() {
        return manageIntValues(maxUeEbene);
    }
    @Override
    public void setMaxUeEbene(final Integer maxUeEbene) {
        this.maxUeEbene = maxUeEbene;
    }
    @Override
    public int getIntend() {
        return manageIntValues(intend);
    }
    @Override
    public void setIntend(final Integer intend) {
        this.intend = intend;
    }
    @Override
    public int getIntendLi() {
        return manageIntValues(intendLi);
    }
    @Override
    public void setIntendLi(final Integer intendLi) {
        this.intendLi = intendLi;
    }
    @Override
    public int getIntendSys() {
        return manageIntValues(intendSys);
    }
    @Override
    public void setIntendSys(final Integer intendSys) {
        this.intendSys = intendSys;
    }

    @Override
    public boolean isFlgTrimDesc() {
        return flgTrimDesc;
    }
    @Override
    public void setFlgTrimDesc(final boolean flgTrimDesc) {
        this.flgTrimDesc = flgTrimDesc;
    }

    @Override
    public boolean isFlgReEscapeDesc() {
        return flgReEscapeDesc;
    }

    @Override
    public void setFlgReEscapeDesc(final boolean flgReEscapeDesc) {
        this.flgReEscapeDesc = flgReEscapeDesc;
    }

    @Override
    public boolean isFlgShowDescWithUe() {
        return flgShowDescWithUe;
    }

    @Override
    public void setFlgShowDescWithUe(final boolean flgShowDescWithUe) {
        this.flgShowDescWithUe = flgShowDescWithUe;
    }

    @Override
    public boolean isFlgShowDescInNextLine() {
        return flgShowDescInNextLine;
    }

    @Override
    public void setFlgShowDescInNextLine(final boolean flgShowDescInNextLine) {
        this.flgShowDescInNextLine = flgShowDescInNextLine;
    }

    @Override
    public void setAllFlgShow(final boolean value) {
        setFlgShowType(value);
        setFlgShowState(value);
        setFlgShowName(value);
        setFlgShowResLoc(value);
        setFlgShowSymLink(value);
        setFlgShowDocLayout(value);
        setFlgShowIst(value);
        setFlgShowPlan(value);
        setFlgShowPlanCalc(value);
        setFlgShowChildrenSum(value);
        setFlgShowMetaData(value);
        setFlgShowSysData(value);
        setFlgShowDesc(value);
    }

    @Override
    public void resetDefaults() {
        super.resetDefaults();
        this.flgDoIntend = false;
        this.flgShowBrackets = false;
        this.intendFuncArea = 0;
        this.flgIntendSum = false;

        this.maxEbene = 0;
        this.maxUeEbene = 0;
        this.intend = 0;
        this.intendLi = 0;
        this.intendSys = 0;
        this.flgConcreteToDosOnly = 0;
        this.flgTrimDesc = false;
        this.flgReEscapeDesc = false;

        this.flgShowState = false;
        this.flgShowType = false;
        this.flgShowName = false;
        this.flgShowResLoc = false;
        this.flgShowSymLink = false;
        this.flgShowDocLayout = false;
        this.flgShowIst = false;
        this.flgShowPlan = false;
        this.flgShowPlanCalc = false;
        this.flgShowChildrenSum = false;
        this.flgShowMetaData = false;
        this.flgShowSysData = false;
        this.flgShowDesc = false;
        this.flgShowDescWithUe = false;
        this.flgShowDescInNextLine = false;

        this.flgRecalc = false;
        this.flgProcessDocLayout = false;
        this.flgUsePublicBaseRef = false;
    }

    @Override
    public String toString() {
        return "OutputOptionsImpl [flgDoIntend=" + this.flgDoIntend
                        + ", flgShowBrackets=" + this.flgShowBrackets
                        + ", intendFuncArea=" + this.intendFuncArea
                        + ", flgIntendSum=" + this.flgIntendSum 
                        + ", maxEbene=" + this.maxEbene 
                        + ", maxUeEbene=" + this.maxUeEbene
                        + ", intend=" + this.intend 
                        + ", intendLi=" + this.intendLi 
                        + ", intendSys=" + this.intendSys
                        + ", flgConcreteToDosOnly=" + this.flgConcreteToDosOnly
                        + ", flgTrimDesc=" + this.flgTrimDesc
                        + ", flgReEscapeDesc=" + this.flgReEscapeDesc
                        + ", flgShowState=" + this.flgShowState
                        + ", flgShowType=" + this.flgShowType
                        + ", flgShowName=" + this.flgShowName
                        + ", flgShowResLoc=" + this.flgShowResLoc
                        + ", flgShowSymLink=" + this.flgShowSymLink
                        + ", flgShowDocLayout=" + this.flgShowDocLayout
                        + ", flgShowIst=" + this.flgShowIst 
                        + ", flgShowPlan=" + this.flgShowPlan 
                        + ", flgShowPlanCalc=" + this.flgShowPlanCalc 
                        + ", flgShowChildrenSum=" + this.flgShowChildrenSum 
                        + ", flgShowMetaData=" + this.flgShowMetaData 
                        + ", flgShowSysData=" + this.flgShowSysData 
                        + ", flgShowDesc=" + this.flgShowDesc 
                        + ", flgShowDescWithUe=" + this.flgShowDescWithUe 
                        + ", flgShowDescInNextLine=" + this.flgShowDescInNextLine 
                        + ", flgRecalc=" + this.flgRecalc 
                        + ", flgProcessDocLayout=" + this.flgProcessDocLayout
                        + ", flgUsePublicBaseRef=" + this.flgUsePublicBaseRef
                        + ", strNotNodePraefix=" + this.strNotNodePraefix
                        + ", strReadIfStatusInListOnly=" + this.strReadIfStatusInListOnly 
                        + ", strClassFilter=" + this.strClassFilter 
                        + ", strTypeFilter=" + this.strTypeFilter 
                        + ", strWorkflowStateFilter=" + this.strWorkflowStateFilter
                        + ", strMetaNodeSubTypeFilter=" + this.strMetaNodeSubTypeFilter
                        + ", strMetaNodeTypeTagsFilter=" + this.strMetaNodeTypeTagsFilter
                        + ", mpStateFilter=" + this.mpStateFilter
                        + ", mpClassFilter=" + this.mpClassFilter
                        + ", mpTypeFilter=" + this.mpTypeFilter
                        + ", mpWorkflowStateFilter=" + this.mpWorkflowStateFilter
                        + ", mpMetaNodeSubTypeFilter=" + this.mpMetaNodeSubTypeFilter
                        + ", mpMetaNodeTypeTagsFilter=" + this.mpMetaNodeTypeTagsFilter
                        + "]";
    }
}
