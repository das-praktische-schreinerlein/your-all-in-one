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

import java.util.Map;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 * <h4>FeatureDescription:</h4>
 *     interface for options for search of Nodes
 * 
 * @package de.yaio.datatransfer.exporter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface SearchOptions {
    int getMaxEbene();
    void setMaxEbene(Integer maxEbene);
    String getStrNotNodePraefix();
    void setStrNotNodePraefix(String strNotNodePraefix);
    int getFlgConcreteToDosOnly();
    void setFlgConcreteToDosOnly(Integer flgConcreteToDosOnly);

    Map<String, WorkflowState> getMapWorkflowStateFilter();
    Map<String, String> getMapClassFilter();
    Map<String, String> getMapTypeFilter();
    Map<String, String> getMapStateFilter();
}
