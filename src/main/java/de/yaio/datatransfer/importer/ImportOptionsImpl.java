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
package de.yaio.datatransfer.importer;

import java.util.Map;

import de.yaio.utils.DataUtils;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *    options for import of Nodes
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ImportOptionsImpl implements ImportOptions {

    protected boolean flgParseDesc = true;
    protected boolean flgParseDocLayout = true;
    protected boolean flgParseIst = true;
    protected boolean flgParseMetaData = true;
    protected boolean flgParsePlan = true;
    protected boolean flgParsePlanCalc = false;
    protected boolean flgParseResLoc = true;
    protected boolean flgParseSymLink = true;
    protected boolean flgParseSysData = true;

    protected String strReadIfStatusInListOnly = "";
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
        this.flgParseSysData = baseOptions.isFlgParseSysData();
        this.flgParseDesc = baseOptions.isFlgParseDesc();
        this.strReadIfStatusInListOnly = baseOptions.getStrReadIfStatusInListOnly();
        this.strClassFilter = baseOptions.getStrClassFilter();
        this.strTypeFilter = baseOptions.getStrTypeFilter();
        
        this.initFilterMaps();
    }

    public boolean isFlgParseDesc() {
        return flgParseDesc;
    }
    public void setFlgParseDesc(final boolean flgParseDesc) {
        this.flgParseDesc = flgParseDesc;
    }
    public boolean isFlgParseDocLayout() {
        return flgParseDocLayout;
    }
    public void setFlgParseDocLayout(final boolean flgParseDocLayout) {
        this.flgParseDocLayout = flgParseDocLayout;
    }

    public boolean isFlgParseIst() {
        return flgParseIst;
    }
    public void setFlgParseIst(final boolean flgParseIst) {
        this.flgParseIst = flgParseIst;
    }
    public boolean isFlgParseMetaData() {
        return flgParseMetaData;
    }
    public void setFlgParseMetaData(final boolean flgParseMetaData) {
        this.flgParseMetaData = flgParseMetaData;
    }
    public boolean isFlgParsePlan() {
        return flgParsePlan;
    }
    public void setFlgParsePlan(final boolean flgParsePlan) {
        this.flgParsePlan = flgParsePlan;
    }
    public boolean isFlgParsePlanCalc() {
        return flgParsePlanCalc;
    }
    public void setFlgParsePlanCalc(final boolean flgParsePlanCalc) {
        this.flgParsePlanCalc = flgParsePlanCalc;
    }
    public boolean isFlgParseResLoc() {
        return flgParseResLoc;
    }

    public void setFlgParseResLoc(final boolean flgParseResLoc) {
        this.flgParseResLoc = flgParseResLoc;
    }

    public boolean isFlgParseSymLink() {
        return flgParseSymLink;
    }

    public void setFlgParseSymLink(final boolean flgParseSymLink) {
        this.flgParseSymLink = flgParseSymLink;
    }

    public boolean isFlgParseSysData() {
        return flgParseSysData;
    }
    public void setFlgParseSysData(final boolean flgParseSysData) {
        this.flgParseSysData = flgParseSysData;
    }

    public String getStrReadIfStatusInListOnly() {
        return strReadIfStatusInListOnly;
    }
    public void setStrReadIfStatusInListOnly(final String strReadIfStatusInListOnly) {
        this.strReadIfStatusInListOnly = strReadIfStatusInListOnly;
        this.mpStateFilter = DataUtils.initMapFromCsvString(this.strReadIfStatusInListOnly);
    }
    public String getStrClassFilter() {
        return strClassFilter;
    }
    public void setStrClassFilter(final String strClassFilter) {
        this.mpClassFilter = DataUtils.initMapFromCsvString(this.strClassFilter);
        this.strClassFilter = strClassFilter;
    }
    public String getStrTypeFilter() {
        return strTypeFilter;
    }
    public void setStrTypeFilter(final String strTypeFilter) {
        this.mpTypeFilter = DataUtils.initMapFromCsvString(this.strTypeFilter);
        this.strTypeFilter = strTypeFilter;
    }
    
    public int manageIntValues(final Integer value) {
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
    
    public void setAllFlgParse(final boolean value) {
        setFlgParseDesc(value);
        setFlgParseDocLayout(value);
        setFlgParseIst(value);
        setFlgParseMetaData(value);
        setFlgParsePlan(value);
        setFlgParsePlanCalc(value);
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
        this.flgParseResLoc = false;
        this.flgParseSymLink = false;
        this.flgParseSysData = false;

        this.setStrReadIfStatusInListOnly("");
        this.setStrClassFilter("");
        this.setStrTypeFilter("");
    }

    @Override
    public String toString() {
        return "OutputOptionsImpl ["
                        + "flgParseDesc=" + this.flgParseDesc
                        + ", flgParseDocLayout=" + this.flgParseDocLayout
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
                        + "]";
    }
}
