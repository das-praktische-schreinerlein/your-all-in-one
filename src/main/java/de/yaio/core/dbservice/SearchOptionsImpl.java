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
package de.yaio.core.dbservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import de.yaio.commons.data.DataUtils;
import de.yaio.core.datadomain.WorkflowState;

/** 
 * options for search of Nodes, initialized with default-values
 * 
 * @FeatureDomain                DatenExport
 * @package                      de.yaio.datatransfer.exporter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SearchOptionsImpl implements SearchOptions {
    protected Integer maxEbene = 9999;
    protected Integer flgConcreteToDosOnly = 0;
    protected String strNotNodePraefix = "";

    protected String strReadIfStatusInListOnly = "";
    protected String strClassFilter = "";
    protected String strTypeFilter = "";
    protected String strWorkflowStateFilter = "";
    protected String strMetaNodeSubTypeFilter = "";
    protected String strMetaNodeTypeTagsFilter = "";

    protected Map<String, String> mpClassFilter = null;
    protected Map<String, String> mpTypeFilter = null;
    protected Map<String, String> mpStateFilter = null;
    protected Map<String, WorkflowState> mpWorkflowStateFilter = null;
    protected Map<String, String> mpMetaNodeSubTypeFilter = null;
    protected Map<String, String> mpMetaNodeTypeTagsFilter = null;

    protected Date istStartLE = null;
    protected Date istStartGE = null;
    protected Date istEndeLE = null;
    protected Date istEndeGE = null;
    protected Date planStartLE = null;
    protected Date planStartGE = null;
    protected Date planEndeLE = null;
    protected Date planEndeGE = null;

    protected String istStartIsNull = null;
    protected String istEndeIsNull = null;
    protected String planStartIsNull = null;
    protected String planEndeIsNull = null;

    public SearchOptionsImpl() {
        super();
    }

    public SearchOptionsImpl(final SearchOptions baseOptions) {
        super();
        this.maxEbene = baseOptions.getMaxEbene();
        this.strNotNodePraefix = baseOptions.getStrNotNodePraefix();
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
    public int getFlgConcreteToDosOnly() {
        return manageIntValues(flgConcreteToDosOnly);
    }
    @Override
    public void setFlgConcreteToDosOnly(final Integer flgConcreteToDosOnly) {
        this.flgConcreteToDosOnly = flgConcreteToDosOnly;
    }

    @Override
    public Date getPlanEndeGE() {
        return planEndeGE;
    }

    public void setPlanEndeGE(Date planEndeGE) {
        this.planEndeGE = planEndeGE;
    }

    @Override
    public Date getIstStartLE() {
        return istStartLE;
    }

    public void setIstStartLE(Date istStartLE) {
        this.istStartLE = istStartLE;
    }

    @Override
    public Date getIstStartGE() {
        return istStartGE;
    }

    public void setIstStartGE(Date istStartGE) {
        this.istStartGE = istStartGE;
    }

    @Override
    public Date getIstEndeLE() {
        return istEndeLE;
    }

    public void setIstEndeLE(Date istEndeLE) {
        this.istEndeLE = istEndeLE;
    }

    @Override
    public Date getIstEndeGE() {
        return istEndeGE;
    }

    public void setIstEndeGE(Date istEndeGE) {
        this.istEndeGE = istEndeGE;
    }

    @Override
    public Date getPlanStartLE() {
        return planStartLE;
    }

    public void setPlanStartLE(Date planStartLE) {
        this.planStartLE = planStartLE;
    }

    @Override
    public Date getPlanStartGE() {
        return planStartGE;
    }

    public void setPlanStartGE(Date planStartGE) {
        this.planStartGE = planStartGE;
    }

    @Override
    public Date getPlanEndeLE() {
        return planEndeLE;
    }

    public void setPlanEndeLE(Date planEndeLE) {
        this.planEndeLE = planEndeLE;
    }

    @Override
    public String getPlanEndeIsNull() {
        return planEndeIsNull;
    }

    public void setPlanEndeIsNull(String planEndeIsNull) {
        this.planEndeIsNull = planEndeIsNull;
    }

    @Override
    public String getIstStartIsNull() {
        return istStartIsNull;
    }

    public void setIstStartIsNull(String istStartIsNull) {
        this.istStartIsNull = istStartIsNull;
    }

    @Override
    public String getIstEndeIsNull() {
        return istEndeIsNull;
    }

    public void setIstEndeIsNull(String istEndeIsNull) {
        this.istEndeIsNull = istEndeIsNull;
    }

    @Override
    public String getPlanStartIsNull() {
        return planStartIsNull;
    }

    public void setPlanStartIsNull(String planStartIsNull) {
        this.planStartIsNull = planStartIsNull;
    }


    @Override
    public String getStrNotNodePraefix() {
        return this.strNotNodePraefix;
    }

    @Override
    public void setStrNotNodePraefix(final String strNotNodePraefix) {
        this.strNotNodePraefix = strNotNodePraefix;
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
        this.strClassFilter = strClassFilter;
        this.mpClassFilter = DataUtils.initMapFromCsvString(this.strClassFilter);
    }
    public String getStrTypeFilter() {
        return strTypeFilter;
    }
    public void setStrTypeFilter(final String strTypeFilter) {
        this.strTypeFilter = strTypeFilter;
        this.mpTypeFilter = DataUtils.initMapFromCsvString(this.strTypeFilter);
    }
    public String getStrWorkflowStateFilter() {
        return strWorkflowStateFilter;
    }
    public void setStrWorkflowStateFilter(final String strWorkflowStateFilter) {
        this.strWorkflowStateFilter = strWorkflowStateFilter;
        this.mpWorkflowStateFilter = new HashMap<String, WorkflowState>();
        Map<String, String> tmp = DataUtils.initMapFromCsvString(this.strWorkflowStateFilter);
        if (MapUtils.isEmpty(tmp)) {
            return;
        }
        for (String state : tmp.keySet()) {
            this.mpWorkflowStateFilter.put(state, WorkflowState.valueOf(state));
        }
    }

    public String getStrMetaNodeSubTypeFilter() {
        return strMetaNodeSubTypeFilter;
    }
    public void setStrMetaNodeSubTypeFilter(final String strMetaNodeSubTypeFilter) {
        this.strMetaNodeSubTypeFilter = strMetaNodeSubTypeFilter;
        this.mpMetaNodeSubTypeFilter = DataUtils.initMapFromCsvString(this.strMetaNodeSubTypeFilter);
    }

    public String getStrMetaNodeTypeTagsFilter() {
        return strMetaNodeTypeTagsFilter;
    }
    public void setStrMetaNodeTypeTagsFilter(final String strMetaNodeTypeTagsFilter) {
        this.strMetaNodeTypeTagsFilter = strMetaNodeTypeTagsFilter;
        this.mpMetaNodeTypeTagsFilter = DataUtils.initMapFromCsvString(this.strMetaNodeTypeTagsFilter);
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
    @Override
    public Map<String, WorkflowState> getMapWorkflowStateFilter() {
        return this.mpWorkflowStateFilter;
    }
    @Override
    public Map<String, String> getMapMetaNodeSubTypeFilter() {
        return this.mpMetaNodeSubTypeFilter;
    }
    @Override
    public Map<String, String> getMapMetaNodeTypeTagsFilter() {
        return this.mpMetaNodeTypeTagsFilter;
    }

    public void initFilterMaps() {
        this.setStrReadIfStatusInListOnly(this.getStrReadIfStatusInListOnly());
        this.setStrClassFilter(this.getStrClassFilter());
        this.setStrTypeFilter(this.getStrTypeFilter());
        this.setStrWorkflowStateFilter(this.getStrWorkflowStateFilter());
        this.setStrMetaNodeSubTypeFilter(this.getStrMetaNodeSubTypeFilter());
        this.setStrMetaNodeTypeTagsFilter(this.getStrMetaNodeTypeTagsFilter());
    }
    
    public void resetDefaults() {
        this.maxEbene = 0;
        this.flgConcreteToDosOnly = 0;
        this.setStrNotNodePraefix("");
        this.setStrReadIfStatusInListOnly("");
        this.setStrClassFilter("");
        this.setStrTypeFilter("");
        this.setStrWorkflowStateFilter("");
        this.setStrMetaNodeSubTypeFilter("");
        this.setStrMetaNodeTypeTagsFilter("");
    }

    @Override
    public String toString() {
        return "SearchOptionsImpl [maxEbene=" + this.maxEbene
                        + ", flgConcreteToDosOnly=" + this.flgConcreteToDosOnly
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

    protected int manageIntValues(final Integer value) {
        return value != null ? value : 0;
    }
}
