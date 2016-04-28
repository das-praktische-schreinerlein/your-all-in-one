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

import java.util.Date;
import java.util.Map;

/** 
 * interface for options for search of Nodes
 * 
 * @FeatureDomain                DatenExport
 * @package                      de.yaio.datatransfer.exporter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface SearchOptions {
    int getMaxEbene();
    void setMaxEbene(Integer maxEbene);
    String getStrNotNodePraefix();
    void setStrNotNodePraefix(String strNotNodePraefix);
    int getFlgConcreteToDosOnly();
    void setFlgConcreteToDosOnly(Integer flgConcreteToDosOnly);
    Date getIstStartLE();
    Date getIstStartGE();
    Date getIstEndeLE();
    Date getIstEndeGE();
    Date getPlanStartLE();
    Date getPlanStartGE();
    Date getPlanEndeLE();
    Date getPlanEndeGE();
    String getIstStartIsNull();
    String getIstEndeIsNull();
    String getPlanStartIsNull();
    String getPlanEndeIsNull();

    Map<String, WorkflowState> getMapWorkflowStateFilter();
    Map<String, String> getMapClassFilter();
    Map<String, String> getMapTypeFilter();
    Map<String, String> getMapStateFilter();
    Map<String, String> getMapMetaNodeSubTypeFilter();
    Map<String, String> getMapMetaNodeTypeTagsFilter();
}
