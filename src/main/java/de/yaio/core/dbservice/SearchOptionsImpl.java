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
package de.yaio.core.dbservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;
import de.yaio.utils.DataUtils;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 * <h4>FeatureDescription:</h4>
 *     options for search of Nodes, initialized with default-values
 * 
 * @package de.yaio.datatransfer.exporter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SearchOptionsImpl implements SearchOptions {
    protected Integer maxEbene = 9999;
    protected Integer flgConcreteToDosOnly = 0;
    protected String strNotNodePraefix = "";

    protected String strReadIfStatusInListOnly = "";
    protected String strClassFilter = "";
    protected String strTypeFilter = "";
    protected String strWorkflowStateFilter = "";

    protected Map<String, String> mpClassFilter = null;
    protected Map<String, String> mpTypeFilter = null;
    protected Map<String, String> mpStateFilter = null;
    protected Map<String, WorkflowState> mpWorkflowStateFilter = null;

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
    public void setFlgConcreteToDosOnly(Integer flgConcreteToDosOnly) {
        this.flgConcreteToDosOnly = flgConcreteToDosOnly;
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
    
    @Override
    public Map<String, String> getMapClassFilter() {
        return this.mpClassFilter;
    };
    @Override
    public Map<String, String> getMapTypeFilter() {
        return this.mpTypeFilter;
    };
    @Override
    public Map<String, String> getMapStateFilter() {
        return this.mpStateFilter;
    };
    @Override
    public Map<String, WorkflowState> getMapWorkflowStateFilter() {
        return this.mpWorkflowStateFilter;
    };
    
    public void initFilterMaps() {
        this.setStrReadIfStatusInListOnly(this.getStrReadIfStatusInListOnly());
        this.setStrClassFilter(this.getStrClassFilter());
        this.setStrTypeFilter(this.getStrTypeFilter());
        this.setStrWorkflowStateFilter(this.getStrWorkflowStateFilter());
    }
    
    public void resetDefaults() {
        this.maxEbene = 0;
        this.flgConcreteToDosOnly = 0;
        this.setStrNotNodePraefix("");
        this.setStrReadIfStatusInListOnly("");
        this.setStrClassFilter("");
        this.setStrTypeFilter("");
        this.setStrWorkflowStateFilter("");
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
                        + ", mpStateFilter=" + this.mpStateFilter
                        + ", mpClassFilter=" + this.mpClassFilter
                        + ", mpTypeFilter=" + this.mpTypeFilter
                        + ", mpWorkflowStateFilter=" + this.mpWorkflowStateFilter
                        + "]";
    }

    protected int manageIntValues(final Integer value) {
        return value != null ? value : 0;
    }
}
