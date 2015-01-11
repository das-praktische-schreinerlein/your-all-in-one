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

import java.util.Map;

import de.yaio.utils.DataUtils;

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
    protected boolean flgDoIntend = true;
    protected boolean flgShowBrackets = true;
    protected Integer intendFuncArea = 80;
    protected boolean flgIntendSum = false;

    protected Integer maxEbene = 9999;
    protected Integer maxUeEbene = 3;
    protected Integer intend = 2;
    protected Integer intendLi = 2;
    protected Integer intendSys = 160;
    protected boolean flgTrimDesc = true;
    protected boolean flgReEscapeDesc = true;

    protected boolean flgShowState = true;
    protected boolean flgShowType = true;
    protected boolean flgShowName = true;
    protected boolean flgShowResLoc = true;
    protected boolean flgShowSymLink = true;
    protected boolean flgShowDocLayout = true;
    protected boolean flgShowIst = true;
    protected boolean flgShowPlan = true;
    protected boolean flgShowPlanCalc = false;
    protected boolean flgShowChildrenSum = false;
    protected boolean flgShowMetaData = true;
    protected boolean flgShowSysData = true;
    protected boolean flgShowDesc = true;
    protected boolean flgShowDescWithUe = false;
    protected boolean flgShowDescInNextLine = false;

    protected boolean flgRecalc = false;
    protected boolean flgProcessDocLayout = false;
    protected boolean flgProcessMarkdown = false;
    protected String strReadIfStatusInListOnly = "";
    protected String strClassFilter = "";
    protected String strTypeFilter = "";
    
    protected Map<String, String> mpClassFilter = null;
    protected Map<String, String> mpTypeFilter = null;
    protected Map<String, String> mpStateFilter = null;

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
        this.flgShowPlanCalc = baseOptions.isFlgShowPlanCalc();
        this.flgShowIst = baseOptions.isFlgShowIst();
        this.flgShowChildrenSum = baseOptions.isFlgShowChildrenSum();
        this.flgShowMetaData = baseOptions.isFlgShowMetaData();
        this.flgShowSysData = baseOptions.isFlgShowSysData();
        this.flgShowDesc = baseOptions.isFlgShowDesc();
        this.flgRecalc = baseOptions.isFlgRecalc();
        this.flgProcessDocLayout = baseOptions.isFlgProcessDocLayout();
        this.flgProcessMarkdown = baseOptions.isFlgProcessMarkdown();
        this.strReadIfStatusInListOnly = baseOptions.getStrReadIfStatusInListOnly();
        this.strClassFilter = baseOptions.getStrClassFilter();
        this.strTypeFilter = baseOptions.getStrTypeFilter();
        
        this.initFilterMaps();
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
    public boolean isFlgShowPlanCalc() {
        return flgShowPlanCalc;
    }
    public void setFlgShowPlanCalc(boolean flgShowPlanCalc) {
        this.flgShowPlanCalc = flgShowPlanCalc;
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
    public boolean isFlgRecalc() {
        return flgRecalc;
    }
    public void setFlgRecalc(boolean flgRecalc) {
        this.flgRecalc = flgRecalc;
    }
    public boolean isFlgProcessDocLayout() {
        return flgProcessDocLayout;
    }
    public void setFlgProcessDocLayout(boolean flgProcessDocLayout) {
        this.flgProcessDocLayout = flgProcessDocLayout;
    }
    public boolean isFlgProcessMarkdown() {
        return flgProcessMarkdown;
    }
    public void setFlgProcessMarkdown(boolean flgProcessMarkdown) {
        this.flgProcessMarkdown = flgProcessMarkdown;
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
    public String getStrReadIfStatusInListOnly() {
        return strReadIfStatusInListOnly;
    }
    public void setStrReadIfStatusInListOnly(String strReadIfStatusInListOnly) {
        this.strReadIfStatusInListOnly = strReadIfStatusInListOnly;
        this.mpStateFilter = DataUtils.initMapFromCsvString(this.strReadIfStatusInListOnly);
    }
    public String getStrClassFilter() {
        return strClassFilter;
    }
    public void setStrClassFilter(String strClassFilter) {
        this.mpClassFilter = DataUtils.initMapFromCsvString(this.strClassFilter);
        this.strClassFilter = strClassFilter;
    }
    public String getStrTypeFilter() {
        return strTypeFilter;
    }
    public void setStrTypeFilter(String strTypeFilter) {
        this.mpTypeFilter = DataUtils.initMapFromCsvString(this.strTypeFilter);
        this.strTypeFilter = strTypeFilter;
    }
    
    public int manageIntValues(Integer value) {
        return (value != null ? value : 0);
    }
    
    
    public Map<String, String> getMapClassFilter() {
        return this.mpClassFilter;
    };
    public Map<String, String> getMapTypeFilter() {
        return this.mpTypeFilter;
    };
    public Map<String, String> getMapStateFilter() {
        return this.mpStateFilter;
    };
    
    public void initFilterMaps() {
        this.setStrReadIfStatusInListOnly(this.getStrReadIfStatusInListOnly());
        this.setStrClassFilter(this.getStrClassFilter());
        this.setStrTypeFilter(this.getStrTypeFilter());
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
        setFlgShowPlanCalc(value);
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
        this.flgShowPlanCalc = false;
        this.flgShowChildrenSum = false;
        this.flgShowMetaData = false;
        this.flgShowSysData = false;
        this.flgShowDesc = false;
        this.flgShowDescWithUe = false;
        this.flgShowDescInNextLine = false;

        this.flgRecalc = false;
        this.flgProcessDocLayout = false;
        this.flgProcessMarkdown = false;
        this.setStrReadIfStatusInListOnly("");
        this.setStrClassFilter("");
        this.setStrTypeFilter("");
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
                        + ", flgShowPlanCalc=" + this.flgShowPlanCalc 
                        + ", flgShowChildrenSum=" + this.flgShowChildrenSum 
                        + ", flgShowMetaData=" + this.flgShowMetaData 
                        + ", flgShowSysData=" + this.flgShowSysData 
                        + ", flgShowDesc=" + this.flgShowDesc 
                        + ", flgShowDescWithUe=" + this.flgShowDescWithUe 
                        + ", flgShowDescInNextLine=" + this.flgShowDescInNextLine 
                        + ", flgRecalc=" + this.flgRecalc 
                        + ", flgProcessDocLayout=" + this.flgProcessDocLayout
                        + ", flgProcessMarkdown=" + this.flgProcessMarkdown
                        + ", strReadIfStatusInListOnly=" + this.strReadIfStatusInListOnly 
                        + ", strClassFilter=" + this.strClassFilter 
                        + ", strTypeFilter=" + this.strTypeFilter 
                        + "]";
    }
}
