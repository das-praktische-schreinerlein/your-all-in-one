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


/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 * <h4>FeatureDescription:</h4>
 *     interface for DataDomain: DocLayout (Layout-Tags, Styles...) of the Node
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface DocLayoutData extends DataDomain {
    public static final int CONST_ORDER = 30;

    public String getDocLayoutTagCommand();
    public void setDocLayoutTagCommand(String docLayoutTagCommand);
    public String getDocLayoutAddStyleClass();
    public void setDocLayoutAddStyleClass(String docLayoutAddStyleClass);
    public String getDocLayoutShortName() ;
    public void setDocLayoutShortName(String docLayoutShortName);
    public String getDocLayoutFlgCloseDiv();    
    public void setDocLayoutFlgCloseDiv(String docLayoutFlgCloseDiv);
}
