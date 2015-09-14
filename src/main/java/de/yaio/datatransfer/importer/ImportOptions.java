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
package de.yaio.datatransfer.importer;

import java.util.Map;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *    interface with options for import of Nodes
 * 
 * @package de.yaio.datatransfer.importer
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface ImportOptions {
    
    static final String CONST_DEFAULT_META_NODE_PRAEFIX = "UNKNOWN";
    
    
    boolean isFlgParseDesc();
    void setFlgParseDesc(boolean flgParseDesc);
    boolean isFlgParseDocLayout();
    void setFlgParseDocLayout(boolean flgParseDocLayout);
    boolean isFlgParseIst();
    void setFlgParseIst(boolean flgParseIst);
    boolean isFlgParseMetaData();
    void setFlgParseMetaData(boolean flgParseNoMetaData);
    boolean isFlgParsePlan();
    void setFlgParsePlan(boolean flgParsePlan);
    boolean isFlgParsePlanCalc();
    void setFlgParsePlanCalc(boolean flgParsePlanCalc);
    boolean isFlgParseResLoc();
    void setFlgParseResLoc(boolean flgParseResLoc);
    boolean isFlgParseSymLink();
    void setFlgParseSymLink(boolean flgParseSymLink);
    boolean isFlgParseSysData();
    void setFlgParseSysData(boolean flgParseNoSysData);

    String getStrReadIfStatusInListOnly();
    void setStrReadIfStatusInListOnly(String strReadIfStatusInListOnly);
    String getStrDefaultMetaNodePraefix();
    void setStrDefaultMetaNodePraefix(String strDefaultMetaNodePraefix);
    String getStrClassFilter();
    void setStrClassFilter(String strClassFilter);
    String getStrTypeFilter();
    void setStrTypeFilter(String strTypeFilter);

    void setAllFlgParse(boolean value);
    
    Map<String, String> getMapClassFilter();
    Map<String, String> getMapTypeFilter();
    Map<String, String> getMapStateFilter();
    void initFilterMaps();

}
