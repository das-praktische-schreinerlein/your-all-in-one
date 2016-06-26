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
 * interface for DataDomain: ResLoc (Ref, Name -> for Urls, Files...) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface ResLocData extends DataDomain {
    int CONST_ORDER = 25;

    String getResLocRef();
    void setResLocRef(String resLocRef);
    String getResLocName();
    void setResLocName(String resLocName);
    String getResLocTags();
    void setResLocTags(String resLocTags);

    void resetResLocData();
}
