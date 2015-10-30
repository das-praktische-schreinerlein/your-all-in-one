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
package de.yaio.core.datadomain;


/** 
 * interface for DataDomain: DocLayout (Layout-Tags, Styles...) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface DocLayoutData extends DataDomain {
    int CONST_ORDER = 30;

    String getDocLayoutTagCommand();
    void setDocLayoutTagCommand(String docLayoutTagCommand);
    String getDocLayoutAddStyleClass();
    void setDocLayoutAddStyleClass(String docLayoutAddStyleClass);
    String getDocLayoutShortName();
    void setDocLayoutShortName(String docLayoutShortName);
    String getDocLayoutFlgCloseDiv();    
    void setDocLayoutFlgCloseDiv(String docLayoutFlgCloseDiv);
}
