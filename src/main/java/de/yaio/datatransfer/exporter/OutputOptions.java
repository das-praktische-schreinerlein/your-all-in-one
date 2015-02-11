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
    boolean isFlgDoIntend();
    void setFlgDoIntend(boolean flgDoIntend);

    boolean isFlgIntendSum();
    void setFlgIntendSum(boolean flgIntendSum);


    boolean isFlgShowState();
    void setFlgShowState(boolean flgShowState);
    boolean isFlgShowType();
    void setFlgShowType(boolean flgShowType);
    boolean isFlgShowName();
    void setFlgShowName(boolean flgShowName);
    boolean isFlgShowResLoc();
    void setFlgShowResLoc(boolean flgShowResLoc);
    boolean isFlgShowSymLink();
    void setFlgShowSymLink(boolean flgShowSymLink);
    boolean isFlgShowDocLayout();
    void setFlgShowDocLayout(boolean flgShowDocLayout);
    boolean isFlgShowIst();
    void setFlgShowIst(boolean flgShowIst);
    boolean isFlgShowPlan();
    void setFlgShowPlan(boolean flgShowPlan);
    boolean isFlgShowPlanCalc();
    void setFlgShowPlanCalc(boolean flgShowPlanCalc);
    boolean isFlgShowChildrenSum();
    void setFlgShowChildrenSum(boolean flgShowChildrenSum);
    boolean isFlgShowMetaData();
    void setFlgShowMetaData(boolean flgShowNoMetaData);
    boolean isFlgShowSysData();
    void setFlgShowSysData(boolean flgShowNoSysData);
    boolean isFlgShowDesc();
    void setFlgShowDesc(boolean flgShowDesc);
    
    boolean isFlgRecalc();
    void setFlgRecalc(boolean flgRecalc);
    boolean isFlgProcessDocLayout();
    void setFlgProcessDocLayout(boolean flgProcessDocLayout);
    boolean isFlgProcessMarkdown();
    void setFlgProcessMarkdown(boolean flgProcessMarkdown);

    int getMaxEbene();
    void setMaxEbene(Integer maxEbene);
    int getMaxUeEbene();
    void setMaxUeEbene(Integer maxUeEbene);
    int getIntend();
    void setIntend(Integer intend);
    int getIntendLi();
    void setIntendLi(Integer intendLi);
    int getIntendSys();
    void setIntendSys(Integer intendSys);
    boolean isFlgTrimDesc();
    void setFlgTrimDesc(boolean flgTrimDesc);

    String getStrReadIfStatusInListOnly();
    void setStrReadIfStatusInListOnly(String strReadIfStatusInListOnly);

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
