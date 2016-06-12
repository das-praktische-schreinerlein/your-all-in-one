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

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/** 
 * factory to create sort for BaseNode
 * 
 * @FeatureDomain                Persistence
 * @package                      de.yaio.core.dbservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeSortFactory {
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(BaseNodeSortFactory.class);

    protected static final Map<String, String> CONST_AVAILIABLE_SORTS = new HashMap<String, String>();
    static {
        CONST_AVAILIABLE_SORTS.put("default", "");
        CONST_AVAILIABLE_SORTS.put("createdUp", sortNullBehind("sysCreateDate", "asc"));
        CONST_AVAILIABLE_SORTS.put("createdDown", sortNullBehind("sysCreateDate", "desc"));
        CONST_AVAILIABLE_SORTS.put("istEndeUp", sortNullBehind("istChildrenSumEnde", "asc"));
        CONST_AVAILIABLE_SORTS.put("istEndeDown", sortNullBehind("istChildrenSumEnde", "desc"));
        CONST_AVAILIABLE_SORTS.put("istStartUp", sortNullBehind("istChildrenSumStart", "asc"));
        CONST_AVAILIABLE_SORTS.put("istStartDown", sortNullBehind("istChildrenSumStart", "desc"));
        CONST_AVAILIABLE_SORTS.put("lastChangeUp", sortNullBehind("sysChangeDate", "asc"));
        CONST_AVAILIABLE_SORTS.put("lastChangeDown", sortNullBehind("sysChangeDate", "desc"));
        CONST_AVAILIABLE_SORTS.put("nameUp", "name asc");
        CONST_AVAILIABLE_SORTS.put("nameDown", "name desc");
        CONST_AVAILIABLE_SORTS.put("nodeNumberUp", "metaNodePraefix asc, metaNodeNummer asc");
        CONST_AVAILIABLE_SORTS.put("nodeNumberDown", "metaNodePraefix desc, metaNodeNummer desc");
        CONST_AVAILIABLE_SORTS.put("planEndeUp", sortNullBehind("planEnde", "asc"));
        CONST_AVAILIABLE_SORTS.put("planEndeDown", sortNullBehind("planEnde", "desc"));
        CONST_AVAILIABLE_SORTS.put("planStartUp", sortNullBehind("planStart", "asc"));
        CONST_AVAILIABLE_SORTS.put("planStartDown", sortNullBehind("planStart", "desc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumEndeUp", sortNullBehind("planChildrenSumEnde", "asc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumEndeDown", sortNullBehind("planChildrenSumEnde", "desc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumStartUp", sortNullBehind("planChildrenSumStart", "asc"));
        CONST_AVAILIABLE_SORTS.put("planChildrenSumStartDown", sortNullBehind("planChildrenSumStart", "desc"));
        CONST_AVAILIABLE_SORTS.put("typeUp", "type asc");
        CONST_AVAILIABLE_SORTS.put("typeDown", "type desc");
        CONST_AVAILIABLE_SORTS.put("workflowStateUp", "workflowState asc");
        CONST_AVAILIABLE_SORTS.put("workflowStateDown", "workflowState desc");
    }

    public static String createSort(final String sortConfig) {
        // setup sort
        String sort = "";
        if (sortConfig != null) {
            sort = CONST_AVAILIABLE_SORTS.get(sortConfig);
            if (sort != null) {
                if (sort.length() > 0) {
                    sort = sort + ", ";
                }
            } else {
                throw new IllegalArgumentException("Unknown sort:" + sortConfig);
            }
        }
        // setup order
        String order = "asc";

        return " order by " + sort
                + " statWorkflowTodoCount asc"
                + ", ebene desc"
                + ", parent_node " + order
                + ", sort_pos " + order;
    }

    protected static String sortNullBehind(final String fieldName, final String order) {
        return "CASE WHEN " + fieldName + " IS NULL THEN 1 ELSE 0 END, " + fieldName + " " + order;
    }
}
