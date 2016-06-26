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

import de.yaio.app.core.datadomain.SysData;

/** 
 * businesslogic for dataDomain: SysData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface SysDataService {
    
    /** the position in the recalc-order at which the recalcer will run */
    int CONST_RECALC_ORDER = 2;

    /** 
     * inititialize the SysData-Fields of the node (sysChange, Checksum) - updates memberVariable node.sys*
     * @param node                   node to initialize
     * @param flgForceUpdate         not only init empty values but force update
     * @throws Exception             parser/format/io-Exceptions possible
     */
    void initSysData(SysData node, boolean flgForceUpdate) throws Exception;

    /**
     * update the SysData-Fields of the node (sysChange, Checksum) - updates memberVariable node.sys*
     * @param node                   node to initialize
     * @throws Exception             parser/format/io-Exceptions possible
     */
    void updateSysData(SysData node) throws Exception;

    /**
     * calcs the checksum of the node 
     * @param node                   node to get the checksum
     * @return                       the checksum
     * @throws Exception             parser/format/io-Exceptions possible
     */
    String getCheckSum(SysData node) throws Exception;
}
