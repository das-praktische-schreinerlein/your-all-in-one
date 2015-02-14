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
package de.yaio.extension.datatransfer.wiki;

import de.yaio.datatransfer.importer.ImportOptionsImpl;


/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *     options for import of Nodes in Wiki-Format
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class WikiImportOptions extends ImportOptionsImpl {

    protected boolean flgReadUe = true;
    protected boolean flgReadList = true;
    protected boolean flgReadWithStatusOnly = false;
    protected boolean flgReadWithWFStatusOnly = false;
    protected String strReadIfStatusInListOnly = null;

    public String toString() {
        return "WikiImportOptions" 
                + " flgReadUe:" + flgReadUe
                + " flgReadList:" + flgReadList
                + " flgReadWithStatusOnly:" + flgReadWithStatusOnly
                + " strReadIfStatusInListOnly:" + strReadIfStatusInListOnly;

    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     get the flag for WikiParser that Wiki-UE will be read or ignored
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ResturnValue true/false = read/ignore
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @return flgReadUe - true/false = read/ignore
     */
    public boolean isFlgReadUe() {
        return flgReadUe;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     set the flag for WikiParser that Wiki-UE will be read or ignored
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @param flgReadUe - true/false = read/ignore
     */
    public void setFlgReadUe(final boolean flgReadUe) {
        this.flgReadUe = flgReadUe;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     get the flag for WikiParser that Wiki-LIST will be read or ignored
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ResturnValue true/false = read/ignore
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @return flgReadList - true/false = read/ignore
     */
    public boolean isFlgReadList() {
        return flgReadList;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     set the flag for WikiParser that Wiki-LIST will be read or ignored
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @param flgReadList - true/false = read/ignore
     */
    public void setFlgReadList(final boolean flgReadList) {
        this.flgReadList = flgReadList;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     get the flag for WikiParser that Nodes with status will 
     *     be read or all others too
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ResturnValue true/false = read only with Status/ read all
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @return flgReadWithStatusOnly - true/false = read only with Status/ read all
     */
    public boolean isFlgReadWithStatusOnly() {
        return flgReadWithStatusOnly;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     set the flag for WikiParser that Nodes with status will 
     *     be read or all others too
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @param flgReadWithStatusOnly - true/false = read only with Status/ read all
     */
    public void setFlgReadWithStatusOnly(final boolean flgReadWithStatusOnly) {
        this.flgReadWithStatusOnly = flgReadWithStatusOnly;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     get the flag for WikiParser that Nodes with Workflowstatus will 
     *     be read or all others too
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ResturnValue true/false = read only with WFStatus/ read all
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @return flgReadWithWFStatusOnly - true/false = read only with WFStatus/ read all
     */
    public boolean isFlgReadWithWFStatusOnly() {
        return flgReadWithWFStatusOnly;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     set the flag for WikiParser that only Nodes with Workflowstatus will 
     *     be read or all others too
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @param flgReadWithWFStatusOnly - true/false = read only with WFStatus/ read all
     */
    public void setFlgReadWithWFStatusOnly(final boolean flgReadWithWFStatusOnly) {
        this.flgReadWithWFStatusOnly = flgReadWithWFStatusOnly;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     get the commaseparated string of node-stati for WikiImporter to filter 
     *     on import
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>ResturnValue string = commaseparated list of node-stati to filter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @return strReadIfStatusInListOnly - commaseparated list of node-stati to filter
     */
    public String getStrReadIfStatusInListOnly() {
        return strReadIfStatusInListOnly;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     DataImport
     * <h4>FeatureDescription:</h4>
     *     set the commaseparated string of node-stati for WikiImporter to filter 
     *     on import
     * <h4>FeatureKeywords:</h4>
     *     DataImport ParserOptions
     * @param strReadIfStatusInListOnly - commaseparated list of node-stati to filter
     */
    public void setStrReadIfStatusInListOnly(final String strReadIfStatusInListOnly) {
        this.strReadIfStatusInListOnly = strReadIfStatusInListOnly;
    }
}
