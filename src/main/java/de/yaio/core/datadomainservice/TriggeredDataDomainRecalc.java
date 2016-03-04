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
package de.yaio.core.datadomainservice;

import java.util.List;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.dbservice.DBFilter;


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
     * @throws Exception             parser/format-Exceptions possible
     */
    void doSearchAndTrigger() throws Exception;

    /** 
     * recalcs the DataDomain connected to the Recalcer if it is triggered by db
     * @param node                   node to recalc
     * @throws Exception             parser/format-Exceptions possible
     */
    void doRecalcWhenTriggered(DataDomain node) throws Exception;

    /** 
     * returns the trigger-filter 
     * @return                       list of filters to get nodes to recalc
     * @throws Exception             parser/format-Exceptions possible
     */
    List<DBFilter> getDBTriggerFilter() throws Exception;

    /** 
     * returns the class of the DataDomain for which the service runs
     * @return                       DataDomain for which the service runs
     */
    Class<?> getRecalcTargetClass();
}
