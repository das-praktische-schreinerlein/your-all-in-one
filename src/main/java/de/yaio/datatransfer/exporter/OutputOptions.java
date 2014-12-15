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
package de.yaio.datatransfer.exporter;

import java.util.Map;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 * <h4>FeatureDescription:</h4>
 *     interface for options for export of Nodes
 * 
 * @package de.yaio.datatransfer.exporter
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface OutputOptions {
    public boolean isFlgDoIntend();
    public void setFlgDoIntend(boolean flgDoIntend);

    public boolean isFlgIntendSum();
    public void setFlgIntendSum(boolean flgIntendSum);


    public boolean isFlgShowState();
    public void setFlgShowState(boolean flgShowState);
    public boolean isFlgShowType();
    public void setFlgShowType(boolean flgShowType);
    public boolean isFlgShowName();
    public void setFlgShowName(boolean flgShowName);
    public boolean isFlgShowResLoc();
    public void setFlgShowResLoc(boolean flgShowResLoc);
    public boolean isFlgShowSymLink();
    public void setFlgShowSymLink(boolean flgShowSymLink);
    public boolean isFlgShowDocLayout();
    public void setFlgShowDocLayout(boolean flgShowDocLayout);
    public boolean isFlgShowIst();
    public void setFlgShowIst(boolean flgShowIst);
    public boolean isFlgShowPlan();
    public void setFlgShowPlan(boolean flgShowPlan);
    public boolean isFlgShowChildrenSum();
    public void setFlgShowChildrenSum(boolean flgShowChildrenSum);
    public boolean isFlgShowMetaData();
    public void setFlgShowMetaData(boolean flgShowNoMetaData);
    public boolean isFlgShowSysData();
    public void setFlgShowSysData(boolean flgShowNoSysData);
    public boolean isFlgShowDesc();
    public void setFlgShowDesc(boolean flgShowDesc);
    
    public boolean isFlgRecalc();
    public void setFlgRecalc(boolean flgRecalc);
    public boolean isFlgProcessDocLayout();
    public void setFlgProcessDocLayout(boolean flgProcessDocLayout);
    public boolean isFlgProcessMarkdown();
    public void setFlgProcessMarkdown(boolean flgProcessMarkdown);

    public int getMaxEbene();
    public void setMaxEbene(Integer maxEbene);
    public int getMaxUeEbene();
    public void setMaxUeEbene(Integer maxUeEbene);
    public int getIntend();
    public void setIntend(Integer intend);
    public int getIntendLi();
    public void setIntendLi(Integer intendLi);
    public int getIntendSys();
    public void setIntendSys(Integer intendSys);
    public boolean isFlgTrimDesc();
    public void setFlgTrimDesc(boolean flgTrimDesc);

    public String getStrReadIfStatusInListOnly();
    public void setStrReadIfStatusInListOnly(String strReadIfStatusInListOnly);

    public String getStrClassFilter();
    public void setStrClassFilter(String strClassFilter);
    
    public String getStrTypeFilter();
    public void setStrTypeFilter(String strTypeFilter);

    public int getIntendFuncArea();
    public void setIntendFuncArea(Integer intendPlanToPos);

    public boolean isFlgShowBrackets();
    public void setFlgShowBrackets(boolean flgShowBrackets);

    public void setAllFlgShow(boolean value);

    public boolean isFlgReEscapeDesc();
    public void setFlgReEscapeDesc(boolean flgReEscapeDesc);
    public boolean isFlgShowDescWithUe();
    public void setFlgShowDescWithUe(boolean flgShowDescWithUe);
    public boolean isFlgShowDescInNextLine();
    public void setFlgShowDescInNextLine(boolean flgShowDescInNextLine);
    
    public Map<String, String> getMapClassFilter();
    public Map<String, String> getMapTypeFilter();
    public Map<String, String> getMapStateFilter();
    public void initFilterMaps();
}
