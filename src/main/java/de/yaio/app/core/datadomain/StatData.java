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
package de.yaio.app.core.datadomain;


/** 
 * interface for DataDomain: Stat (countChildren, countWorkflow, countWorkflowTodos) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface StatData extends DataDomain {
    int CONST_ORDER = 95;

    void setStatChildNodeCount(Integer newStatChildNodeCount);
    Integer getStatChildNodeCount();
    void setStatWorkflowCount(Integer newStatWorkflowCount);
    Integer getStatWorkflowCount();
    void setStatWorkflowTodoCount(Integer newStatWorkflowTodoCount);
    Integer getStatWorkflowTodoCount();
    void setStatUrlResCount(Integer newStatUrlResCount);
    Integer getStatUrlResCount();
    void setStatInfoCount(Integer newStatInfoCount);
    Integer getStatInfoCount();

    void resetStatData();
}
