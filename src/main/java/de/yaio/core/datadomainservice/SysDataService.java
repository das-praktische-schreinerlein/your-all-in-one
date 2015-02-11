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
package de.yaio.core.datadomainservice;

import de.yaio.core.datadomain.SysData;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for dataDomain: SysData
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface SysDataService {
    
    /** the position in the recalc-order at which the recalcer will run */
    int CONST_RECALC_ORDER = DataDomainRecalc.CONST_ORDER_NOOP;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     inititialize the SysData-Fields of the node (sysChange, Checksum) 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberVariable node.sys*
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param node - node to initialize
     * @throws Exception - parser/format/io-Exceptions possible
     */
    void initSysData(SysData node) throws Exception;

    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     calcs the checksum of the node 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - checksum of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param node - node to get the checksum
     * @return the checksum
     * @throws Exception - parser/format/io-Exceptions possible
     */
    String getCheckSum(SysData node) throws Exception;
}