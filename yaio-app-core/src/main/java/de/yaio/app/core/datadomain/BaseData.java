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
 * interface for DataDomain: BaseData (name, id...) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface BaseData extends DataDomain {

    String getFullSrc();
    void setFullSrc(String fullSrc);
    String getSrcName();    
    void setSrcName(String srcName);

    Long getImportTmpId();
    void setImportTmpId(Long id);
    String getType();
    void setType(String type);
    @Override
    String getName();
    @Override
    void setName(String name);
    String getParentNameHirarchry(String delimiter, boolean directionForward);

    String getWorkingId();
}
