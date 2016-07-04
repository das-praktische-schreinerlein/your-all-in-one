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
package de.yaio.app.core.datadomainservice;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.utils.db.DBFilter;

import java.util.List;


/** 
 * interface for baseservice with businesslogic of datadomains
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface TriggeredDataDomainRecalc {
    
    /** 
     * recalcs the DataDomain connected to the Recalcer if it is triggered by db
     */
    void doSearchAndTrigger();

    /** 
     * recalcs the DataDomain connected to the Recalcer if it is triggered by db
     * @param node                   node to recalc
     */
    void doRecalcWhenTriggered(DataDomain node);

    /** 
     * returns the trigger-filter 
     * @return                       list of filters to get nodes to recalc
     */
    List<DBFilter> getDBTriggerFilter();

    /** 
     * returns the class of the DataDomain for which the service runs
     * @return                       DataDomain for which the service runs
     */
    Class<?> getRecalcTargetClass();
}
