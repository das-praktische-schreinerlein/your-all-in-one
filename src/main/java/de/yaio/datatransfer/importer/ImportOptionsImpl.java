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
package de.yaio.datatransfer.importer;

import de.yaio.commons.data.DataUtils;

import java.util.Map;

/** 
 *    options for import of Nodes
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ImportOptionsImpl implements ImportOptions {

    protected boolean flgParseDesc = true;
    protected boolean flgParseDocLayout = true;
    protected boolean flgParseIst = true;
    protected boolean flgParseMetaData = true;
    protected boolean flgParsePlan = true;
    protected boolean flgParsePlanCalc = false;
    protected boolean flgParseResLoc = true;
    protected boolean flgParseResContent = false;
    protected boolean flgParseResIndex = false;
    protected boolean flgParseSymLink = true;
    protected boolean flgParseSysData = true;

    protected String strReadIfStatusInListOnly = "";
    protected String strDefaultMetaNodePraefix = CONST_DEFAULT_META_NODE_PRAEFIX;
    protected String strClassFilter = "";
    protected String strTypeFilter = "";
    
    protected Map<String, String> mpClassFilter = null;
    protected Map<String, String> mpTypeFilter = null;
    protected Map<String, String> mpStateFilter = null;

    public ImportOptionsImpl() {
        super();
    }

    public ImportOptionsImpl(final ImportOptions baseOptions) {
        super();
        this.flgParseDocLayout = baseOptions.isFlgParseDocLayout();
        this.flgParsePlan = baseOptions.isFlgParsePlan();
        this.flgParsePlanCalc = baseOptions.isFlgParsePlanCalc();
        this.flgParseIst = baseOptions.isFlgParseIst();
        this.flgParseMetaData = baseOptions.isFlgParseMetaData();
        this.flgParseResContent = baseOptions.isFlgParseResContent();
        this.flgParseResIndex = baseOptions.isFlgParseResIndex();
        this.flgParseResLoc = baseOptions.isFlgParseResLoc();
        this.flgParseSysData = baseOptions.isFlgParseSysData();
        this.flgParseDesc = baseOptions.isFlgParseDesc();
        this.strReadIfStatusInListOnly = baseOptions.getStrReadIfStatusInListOnly();
        this.strClassFilter = baseOptions.getStrClassFilter();
        this.strTypeFilter = baseOptions.getStrTypeFilter();
        this.strDefaultMetaNodePraefix = baseOptions.getStrDefaultMetaNodePraefix();
        
        this.initFilterMaps();
    }


    @Override
    public void initFilterMaps() {
        this.setStrReadIfStatusInListOnly(this.getStrReadIfStatusInListOnly());
        this.setStrClassFilter(this.getStrClassFilter());
        this.setStrTypeFilter(this.getStrTypeFilter());
    }
    
    @Override
    public void setAllFlgParse(final boolean value) {
        setFlgParseDesc(value);
        setFlgParseDocLayout(value);
        setFlgParseIst(value);
        setFlgParseMetaData(value);
        setFlgParsePlan(value);
        setFlgParsePlanCalc(value);
        setFlgParseResContent(value);
        setFlgParseResIndex(value);
        setFlgParseResLoc(value);
        setFlgParseSymLink(value);
        setFlgParseSysData(value);
    }

    public void resetDefaults() {
        this.flgParseDesc = false;
        this.flgParseDocLayout = false;
        this.flgParseIst = false;
        this.flgParseMetaData = false;
        this.flgParsePlan = false;
        this.flgParsePlanCalc = false;
        this.flgParseResContent = false;
        this.flgParseResIndex = false;
        this.flgParseResLoc = false;
        this.flgParseSymLink = false;
        this.flgParseSysData = false;
        this.strDefaultMetaNodePraefix = "UNKNOWN";

        this.setStrReadIfStatusInListOnly("");
        this.setStrClassFilter("");
        this.setStrTypeFilter("");
    }

    @Override
    public String toString() {
        return "OutputOptionsImpl ["
                        + "flgParseDesc=" + this.flgParseDesc
                        + ", flgParseDocLayout=" + this.flgParseDocLayout
                        + ", flgParseResContent=" + this.flgParseResContent
                        + ", flgParseResIndex=" + this.flgParseResIndex
                        + ", flgParseResLoc=" + this.flgParseResLoc
                        + ", flgParseSymLink=" + this.flgParseSymLink
                        + ", flgParseIst=" + this.flgParseIst 
                        + ", flgParsePlan=" + this.flgParsePlan 
                        + ", flgParsePlanCalc=" + this.flgParsePlanCalc 
                        + ", flgParseMetaData=" + this.flgParseMetaData 
                        + ", flgParseSysData=" + this.flgParseSysData 
                        + ", flgParseDesc=" + this.flgParseDesc 
                        + ", strReadIfStatusInListOnly=" + this.strReadIfStatusInListOnly 
                        + ", strClassFilter=" + this.strClassFilter 
                        + ", strTypeFilter=" + this.strTypeFilter 
                        + ", strDefaultMetaNodePraefix:" + this.strDefaultMetaNodePraefix
        + "]";
    }

    @Override
    public boolean isFlgParseDesc() {
        return flgParseDesc;
    }
    @Override
    public void setFlgParseDesc(final boolean flgParseDesc) {
        this.flgParseDesc = flgParseDesc;
    }
    @Override
    public boolean isFlgParseDocLayout() {
        return flgParseDocLayout;
    }
    @Override
    public void setFlgParseDocLayout(final boolean flgParseDocLayout) {
        this.flgParseDocLayout = flgParseDocLayout;
    }
    @Override
    public boolean isFlgParseIst() {
        return flgParseIst;
    }
    @Override
    public void setFlgParseIst(final boolean flgParseIst) {
        this.flgParseIst = flgParseIst;
    }
    @Override
    public boolean isFlgParseMetaData() {
        return flgParseMetaData;
    }
    @Override
    public void setFlgParseMetaData(final boolean flgParseMetaData) {
        this.flgParseMetaData = flgParseMetaData;
    }
    @Override
    public boolean isFlgParsePlan() {
        return flgParsePlan;
    }
    @Override
    public void setFlgParsePlan(final boolean flgParsePlan) {
        this.flgParsePlan = flgParsePlan;
    }
    @Override
    public boolean isFlgParsePlanCalc() {
        return flgParsePlanCalc;
    }
    @Override
    public void setFlgParsePlanCalc(final boolean flgParsePlanCalc) {
        this.flgParsePlanCalc = flgParsePlanCalc;
    }
    @Override
    public boolean isFlgParseResContent() {
        return flgParseResContent;
    }
    @Override
    public void setFlgParseResContent(final boolean flgParseResContent) {
        this.flgParseResContent = flgParseResContent;
    }
    @Override
    public boolean isFlgParseResIndex() {
        return flgParseResIndex;
    }
    @Override
    public void setFlgParseResIndex(final boolean flgParseResIndex) {
        this.flgParseResIndex = flgParseResIndex;
    }
    @Override
    public boolean isFlgParseResLoc() {
        return flgParseResLoc;
    }
    @Override
    public void setFlgParseResLoc(final boolean flgParseResLoc) {
        this.flgParseResLoc = flgParseResLoc;
    }
    @Override
    public boolean isFlgParseSymLink() {
        return flgParseSymLink;
    }
    @Override
    public void setFlgParseSymLink(final boolean flgParseSymLink) {
        this.flgParseSymLink = flgParseSymLink;
    }
    @Override
    public boolean isFlgParseSysData() {
        return flgParseSysData;
    }
    @Override
    public void setFlgParseSysData(final boolean flgParseSysData) {
        this.flgParseSysData = flgParseSysData;
    }
    @Override
    public String getStrReadIfStatusInListOnly() {
        return strReadIfStatusInListOnly;
    }
    @Override
    public void setStrReadIfStatusInListOnly(final String strReadIfStatusInListOnly) {
        this.strReadIfStatusInListOnly = strReadIfStatusInListOnly;
        this.mpStateFilter = DataUtils.initMapFromCsvString(this.strReadIfStatusInListOnly);
    }
    @Override
    public final String getStrDefaultMetaNodePraefix() {
        return this.strDefaultMetaNodePraefix;
    }
    @Override
    public final void setStrDefaultMetaNodePraefix(final String strDefaultMetaNodePraefix) {
        this.strDefaultMetaNodePraefix = strDefaultMetaNodePraefix;
    }
    @Override
    public String getStrClassFilter() {
        return strClassFilter;
    }
    @Override
    public void setStrClassFilter(final String strClassFilter) {
        this.mpClassFilter = DataUtils.initMapFromCsvString(this.strClassFilter);
        this.strClassFilter = strClassFilter;
    }
    @Override
    public String getStrTypeFilter() {
        return strTypeFilter;
    }
    @Override
    public void setStrTypeFilter(final String strTypeFilter) {
        this.mpTypeFilter = DataUtils.initMapFromCsvString(this.strTypeFilter);
        this.strTypeFilter = strTypeFilter;
    }
    
    @Override
    public Map<String, String> getMapClassFilter() {
        return this.mpClassFilter;
    }
    @Override
    public Map<String, String> getMapTypeFilter() {
        return this.mpTypeFilter;
    }
    @Override
    public Map<String, String> getMapStateFilter() {
        return this.mpStateFilter;
    }

    protected int manageIntValues(final Integer value) {
        return value != null ? value : 0;
    }
    
}
