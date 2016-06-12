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

import de.yaio.core.datadomain.WorkflowState;
import de.yaio.utils.db.DBFilter;
import de.yaio.utils.db.DBFilterFactory;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/** 
 * factory to create dbfilter for BaseNode
 * 
 * @FeatureDomain                Persistence
 * @package                      de.yaio.core.dbservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeFilterFactory {
    public static List<DBFilter> createSearchOptionsFilter(final SearchOptions searchOptions) {
        List<DBFilter> dbFilters = new ArrayList<>();
        if (searchOptions == null) {
            return dbFilters;
        }

        // create filters for maps
        dbFilters.addAll(DBFilterFactory.createMapStringFilter("state",
                searchOptions.getMapStateFilter() != null ? searchOptions.getMapStateFilter().keySet() : null));
        dbFilters.addAll(DBFilterFactory.createMapStringFilter("dtype",
                searchOptions.getMapClassFilter() != null ? searchOptions.getMapClassFilter().keySet() : null));
        dbFilters.addAll(DBFilterFactory.createMapStringFilter("type",
                searchOptions.getMapTypeFilter() != null ? searchOptions.getMapTypeFilter().keySet() : null));
        dbFilters.addAll(DBFilterFactory.createMapStringFilter("meta_node_sub_type",
                searchOptions.getMapMetaNodeSubTypeFilter() != null ? searchOptions.getMapMetaNodeSubTypeFilter().keySet() : null));
        dbFilters.addAll(DBFilterFactory.createMapStringContainsFilter("meta_node_type_tags",
                searchOptions.getMapMetaNodeTypeTagsFilter() != null ? searchOptions.getMapMetaNodeTypeTagsFilter().keySet() : null));

        // create filter for wfstate (convert enum to integer)
        Map<String, WorkflowState> wfStateMap = searchOptions.getMapWorkflowStateFilter();
        if (MapUtils.isNotEmpty(wfStateMap)) {
            Set<Integer>wfStateValues = new HashSet<>();
            for (WorkflowState state : wfStateMap.values()) {
                wfStateValues.add(state.getValue());
            }
            dbFilters.addAll(DBFilterFactory.createMapIntFilter("workflow_state", wfStateValues));
        }

        // create filter for ebene
        String sql = "(ebene <= :ltmaxEbene)";
        List<DBFilter.Parameter> parameters = new ArrayList<>();
        parameters.add(new DBFilter.Parameter("ltmaxEbene", new Integer(searchOptions.getMaxEbene())));
        dbFilters.add(new DBFilter(sql, parameters));

        // create datefilter
        dbFilters.addAll(DBFilterFactory.createIsNullFilter("istStartIsNull", "ist_start", searchOptions.getIstStartIsNull()));
        dbFilters.addAll(DBFilterFactory.createIsNullFilter("istEndeIsNull", "ist_ende", searchOptions.getIstEndeIsNull()));
        dbFilters.addAll(DBFilterFactory.createIsNullFilter("planStartIsNull", "plan_start", searchOptions.getPlanStartIsNull()));
        dbFilters.addAll(DBFilterFactory.createIsNullFilter("planEndeIsNull", "plan_ende", searchOptions.getPlanEndeIsNull()));
        dbFilters.addAll(DBFilterFactory.createDateFilter("istStartGE", "ist_start", searchOptions.getIstStartGE(), ">="));
        dbFilters.addAll(DBFilterFactory.createDateFilter("istStartLE", "ist_start", searchOptions.getIstStartLE(), "<="));
        dbFilters.addAll(DBFilterFactory.createDateFilter("istEndeGE", "ist_ende", searchOptions.getIstEndeGE(), ">="));
        dbFilters.addAll(DBFilterFactory.createDateFilter("istEndeLE", "ist_ende", searchOptions.getIstEndeLE(), "<="));
        dbFilters.addAll(DBFilterFactory.createDateFilter("planStartGE", "plan_start", searchOptions.getPlanStartGE(), ">="));
        dbFilters.addAll(DBFilterFactory.createDateFilter("planStartLE", "plan_start", searchOptions.getPlanStartLE(), "<="));
        dbFilters.addAll(DBFilterFactory.createDateFilter("planEndeGE", "plan_ende", searchOptions.getPlanEndeGE(), ">="));
        dbFilters.addAll(DBFilterFactory.createDateFilter("planEndeLE", "plan_ende", searchOptions.getPlanEndeLE(), "<="));

        return dbFilters;
    }

    public static List<DBFilter> createFulltextFilter(final String pfulltext) {
        List<DBFilter> dbFilters = new ArrayList<>();

        // tokenize words
        String[] searchWords;
        if (!StringUtils.isEmpty(pfulltext)) {
            String fulltext = pfulltext.replace("  ", " ");
            searchWords = fulltext.split(" ");
            for (int idx = 0; idx < searchWords.length; idx++) {
                String sql = "(lower(name) like lower(:fulltext" + idx + ")"
                        + " or lower(node_desc) like lower(:fulltext" + idx + ")"
                        + " or lower(sym_link_ref) like lower(:fulltext" + idx + ")"
                        + " or lower(sym_link_name) like lower(:fulltext" + idx + ")"
                        + " or lower(res_loc_name) like lower(:fulltext" + idx + ")"
                        + " or lower(res_loc_ref) like lower(:fulltext" + idx + ")"
                        + ")";
                List<DBFilter.Parameter> parameters = new ArrayList<>();
                parameters.add(new DBFilter.Parameter("fulltext" + idx, "%" + searchWords[idx] + "%"));
                dbFilters.add(new DBFilter(sql, parameters));
            }
        }
        return dbFilters;
    }

    public static List<DBFilter> createNotNodePraefixFilter(final String pfulltext) {
        List<DBFilter> dbFilters = new ArrayList<>();

        // tokenize words
        String[] searchWords;
        if (!StringUtils.isEmpty(pfulltext)) {
            String fulltext = pfulltext.replace("  ", " ");
            searchWords = fulltext.split(" ");
            for (int idx = 0; idx < searchWords.length; idx++) {
                String sql = "not (lower(meta_node_praefix) like lower(:notnodepraefix" + idx + ")"
                        + ")";
                List<DBFilter.Parameter> parameters = new ArrayList<>();
                parameters.add(new DBFilter.Parameter("notnodepraefix" + idx, searchWords[idx]));
                dbFilters.add(new DBFilter(sql, parameters));
            }
        }
        return dbFilters;
    }

    public static List<DBFilter> createConcreteTodosOnlyFilter(final Integer pFlgConcreteTodosOnly) {
        List<DBFilter> dbFilters = new ArrayList<>();

        if (pFlgConcreteTodosOnly != null && pFlgConcreteTodosOnly > 0) {
            String sql = "planAufwand > 0";
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            dbFilters.add(new DBFilter(sql, parameters));
        }
        return dbFilters;
    }

    public static List<DBFilter> createRootSysUIDFilter(final String pRootSysUID) {
        List<DBFilter> dbFilters = new ArrayList<>();

        if (!StringUtils.isEmpty(pRootSysUID)) {
            String sql = "cachedParentHierarchy like :cachedParentHierarchy";
            List<DBFilter.Parameter> parameters = new ArrayList<>();
            parameters.add(new DBFilter.Parameter("cachedParentHierarchy", "%,"+ pRootSysUID + ",%"));
            dbFilters.add(new DBFilter(sql, parameters));
        }
        return dbFilters;
    }
}
