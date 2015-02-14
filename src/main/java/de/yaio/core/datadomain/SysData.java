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
package de.yaio.core.datadomain;

import java.util.Date;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 * <h4>FeatureDescription:</h4>
 *     interface for DataDomain: Sys (id, created, changed, checksum) of the Node
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface SysData extends DataDomain {
    int CONST_ORDER = 100;

    String getSysUID();
    void setSysUID(String sysUID);
    String getSysCurChecksum();
    void setSysCurChecksum(String sysCurChecksum);
    Integer getSysChangeCount();
    void setSysChangeCount(Integer sysChangeCount);
    Date getSysCreateDate();
    void setSysCreateDate(Date sysCreateDate);
    Date getSysChangeDate();
    void setSysChangeDate(Date sysChangeDate);

    String getDataBlocks4CheckSum() throws Exception;
    void initSysData() throws Exception;
}
