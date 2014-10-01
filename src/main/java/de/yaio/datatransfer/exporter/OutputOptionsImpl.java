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
package de.yaio.datatransfer.exporter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 * <h4>FeatureDescription:</h4>
 *     options for export of Nodes, initialized with default-values
 * 
 * @package de.yaio.datatransfer.exporter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class OutputOptionsImpl implements OutputOptions {
    public boolean flgDoIntend = true;
    public boolean flgShowBrackets = true;
    public Integer intendFuncArea = 80;
    public boolean flgIntendSum = false;

    public Integer maxEbene = 9999;
    public Integer maxUeEbene = 3;
    public Integer intend = 2;
    public Integer intendLi = 2;
    public Integer intendSys = 160;
    public boolean flgTrimDesc = true;
    public boolean flgReEscapeDesc = true;

    public boolean flgShowState = true;
    public boolean flgShowType = true;
    public boolean flgShowName = true;
    public boolean flgShowResLoc = true;
    public boolean flgShowSymLink = true;
    public boolean flgShowDocLayout = true;
    public boolean flgShowIst = true;
    public boolean flgShowPlan = true;
    public boolean flgShowChildrenSum = false;
    public boolean flgShowMetaData = true;
    public boolean flgShowSysData = true;
    public boolean flgShowDesc = true;
    public boolean flgShowDescWithUe = false;
    public boolean flgShowDescInNextLine = false;

    public boolean flgChildrenSum = false;
    public boolean flgProcessDocLayout = false;
    public String strReadIfStatusInListOnly = "";

    public OutputOptionsImpl() {
        super();
    }

    public OutputOptionsImpl(OutputOptions baseOptions) {
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
        this.flgTrimDesc = baseOptions.isFlgTrimDesc();
        this.flgShowType = baseOptions.isFlgShowType();
        this.flgShowState = baseOptions.isFlgShowState();
        this.flgShowName = baseOptions.isFlgShowName();
        this.flgShowDocLayout = baseOptions.isFlgShowDocLayout();
        this.flgShowPlan = baseOptions.isFlgShowPlan();
        this.flgShowIst = baseOptions.isFlgShowIst();
        this.flgShowChildrenSum = baseOptions.isFlgShowChildrenSum();
        this.flgShowMetaData = baseOptions.isFlgShowMetaData();
        this.flgShowSysData = baseOptions.isFlgShowSysData();
        this.flgShowDesc = baseOptions.isFlgShowDesc();
        this.flgChildrenSum = baseOptions.isFlgChildrenSum();
        this.flgProcessDocLayout = baseOptions.isFlgProcessDocLayout();
        this.strReadIfStatusInListOnly = baseOptions.getStrReadIfStatusInListOnly();
    }

    public boolean isFlgDoIntend() {
        return flgDoIntend;
    }
    public void setFlgDoIntend(boolean flgDoIntend) {
        this.flgDoIntend = flgDoIntend;
    }
    public boolean isFlgIntendSum() {
        return flgIntendSum;
    }
    public void setFlgIntendSum(boolean flgIntendSum) {
        this.flgIntendSum = flgIntendSum;
    }
    public boolean isFlgShowChildrenSum() {
        return flgShowChildrenSum;
    }
    public void setFlgShowChildrenSum(boolean flgShowChildrenSum) {
        this.flgShowChildrenSum = flgShowChildrenSum;
    }
    public String getStrReadIfStatusInListOnly() {
        return strReadIfStatusInListOnly;
    }
    public void setStrReadIfStatusInListOnly(String strReadIfStatusInListOnly) {
        this.strReadIfStatusInListOnly = strReadIfStatusInListOnly;
    }
    public int getIntendFuncArea() {
        return manageIntValues(intendFuncArea);
    }
    public void setIntendFuncArea(Integer intendPlanToPos) {
        this.intendFuncArea = intendPlanToPos;
    }
    public boolean isFlgShowBrackets() {
        return flgShowBrackets;
    }
    public void setFlgShowBrackets(boolean flgShowBrackets) {
        this.flgShowBrackets = flgShowBrackets;
    }

    public boolean isFlgShowName() {
        return flgShowName;
    }
    public void setFlgShowName(boolean flgShowName) {
        this.flgShowName = flgShowName;
    }
    public boolean isFlgShowResLoc() {
        return flgShowResLoc;
    }

    public void setFlgShowResLoc(boolean flgShowResLoc) {
        this.flgShowResLoc = flgShowResLoc;
    }

    public boolean isFlgShowSymLink() {
        return flgShowSymLink;
    }

    public void setFlgShowSymLink(boolean flgShowSymLink) {
        this.flgShowSymLink = flgShowSymLink;
    }

    public boolean isFlgShowPlan() {
        return flgShowPlan;
    }
    public void setFlgShowPlan(boolean flgShowPlan) {
        this.flgShowPlan = flgShowPlan;
    }
    public boolean isFlgShowIst() {
        return flgShowIst;
    }
    public void setFlgShowIst(boolean flgShowIst) {
        this.flgShowIst = flgShowIst;
    }
    public boolean isFlgShowDesc() {
        return flgShowDesc;
    }
    public void setFlgShowDesc(boolean flgShowDesc) {
        this.flgShowDesc = flgShowDesc;
    }
    public boolean isFlgShowType() {
        return flgShowType;
    }
    public void setFlgShowType(boolean flgShowType) {
        this.flgShowType = flgShowType;
    }
    public boolean isFlgShowState() {
        return flgShowState;
    }
    public void setFlgShowState(boolean flgShowState) {
        this.flgShowState = flgShowState;
    }
    public boolean isFlgShowDocLayout() {
        return flgShowDocLayout;
    }
    public void setFlgShowDocLayout(boolean flgShowDocLayout) {
        this.flgShowDocLayout = flgShowDocLayout;
    }
    public boolean isFlgShowMetaData() {
        return flgShowMetaData;
    }
    public void setFlgShowMetaData(boolean flgShowMetaData) {
        this.flgShowMetaData = flgShowMetaData;
    }
    public boolean isFlgShowSysData() {
        return flgShowSysData;
    }
    public void setFlgShowSysData(boolean flgShowSysData) {
        this.flgShowSysData = flgShowSysData;
    }
    public boolean isFlgChildrenSum() {
        return flgChildrenSum;
    }
    public void setFlgChildrenSum(boolean flgChildrenSum) {
        this.flgChildrenSum = flgChildrenSum;
    }
    public boolean isFlgProcessDocLayout() {
        return flgProcessDocLayout;
    }
    public void setFlgProcessDocLayout(boolean flgProcessDocLayout) {
        this.flgProcessDocLayout = flgProcessDocLayout;
    }
    public int getMaxEbene() {
        return manageIntValues(maxEbene);
    }
    public void setMaxEbene(Integer maxEbene) {
        this.maxEbene = maxEbene;
    }
    public int getMaxUeEbene() {
        return manageIntValues(maxUeEbene);
    }
    public void setMaxUeEbene(Integer maxUeEbene) {
        this.maxUeEbene = maxUeEbene;
    }
    public int getIntend() {
        return manageIntValues(intend);
    }
    public void setIntend(Integer intend) {
        this.intend = intend;
    }
    public int getIntendLi() {
        return manageIntValues(intendLi);
    }
    public void setIntendLi(Integer intendLi) {
        this.intendLi = intendLi;
    }
    public int getIntendSys() {
        return manageIntValues(intendSys);
    }
    public void setIntendSys(Integer intendSys) {
        this.intendSys = intendSys;
    }

    public boolean isFlgTrimDesc() {
        return flgTrimDesc;
    }
    public void setFlgTrimDesc(boolean flgTrimDesc) {
        this.flgTrimDesc = flgTrimDesc;
    }

    public boolean isFlgReEscapeDesc() {
        return flgReEscapeDesc;
    }

    public void setFlgReEscapeDesc(boolean flgReEscapeDesc) {
        this.flgReEscapeDesc = flgReEscapeDesc;
    }

    public boolean isFlgShowDescWithUe() {
        return flgShowDescWithUe;
    }

    public void setFlgShowDescWithUe(boolean flgShowDescWithUe) {
        this.flgShowDescWithUe = flgShowDescWithUe;
    }

    public boolean isFlgShowDescInNextLine() {
        return flgShowDescInNextLine;
    }

    public void setFlgShowDescInNextLine(boolean flgShowDescInNextLine) {
        this.flgShowDescInNextLine = flgShowDescInNextLine;
    }
    
    public int manageIntValues(Integer value) {
        return (value != null ? value : 0);
    }

    public void setAllFlgShow(boolean value) {
        setFlgShowType(value);
        setFlgShowState(value);
        setFlgShowName(value);
        setFlgShowResLoc(value);
        setFlgShowSymLink(value);
        setFlgShowDocLayout(value);
        setFlgShowIst(value);
        setFlgShowPlan(value);
        setFlgShowChildrenSum(value);
        setFlgShowMetaData(value);
        setFlgShowSysData(value);
        setFlgShowDesc(value);
    }
    
    public void resetDefaults() {
        this.flgDoIntend = false;
        this.flgShowBrackets = false;
        this.intendFuncArea = 0;
        this.flgIntendSum = false;

        this.maxEbene = 0;
        this.maxUeEbene = 0;
        this.intend = 0;
        this.intendLi = 0;
        this.intendSys = 0;
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
        this.flgShowChildrenSum = false;
        this.flgShowMetaData = false;
        this.flgShowSysData = false;
        this.flgShowDesc = false;
        this.flgShowDescWithUe = false;
        this.flgShowDescInNextLine = false;

        this.flgChildrenSum = false;
        this.flgProcessDocLayout = false;
        strReadIfStatusInListOnly = "";
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
                        + ", flgShowChildrenSum=" + this.flgShowChildrenSum 
                        + ", flgShowMetaData=" + this.flgShowMetaData 
                        + ", flgShowSysData=" + this.flgShowSysData 
                        + ", flgShowDesc=" + this.flgShowDesc 
                        + ", flgShowDescWithUe=" + this.flgShowDescWithUe 
                        + ", flgShowDescInNextLine=" + this.flgShowDescInNextLine 
                        + ", flgChildrenSum=" + this.flgChildrenSum 
                        + ", flgProcessDocLayout=" + this.flgProcessDocLayout
                        + ", strReadIfStatusInListOnly=" + this.strReadIfStatusInListOnly + "]";
    }
}
