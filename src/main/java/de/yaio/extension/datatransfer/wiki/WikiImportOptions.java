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
package de.yaio.extension.datatransfer.wiki;

import de.yaio.datatransfer.importer.ImportOptionsImpl;


/** 
 * options for import of Nodes in Wiki-Format
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class WikiImportOptions extends ImportOptionsImpl {

    protected boolean flgReadUe = true;
    protected boolean flgReadList = true;
    protected boolean flgReadWithStatusOnly = false;
    protected boolean flgReadWithWFStatusOnly = false;
    protected String strReadIfStatusInListOnly = null;

    @Override
    public String toString() {
        return "WikiImportOptions" 
                + " flgReadUe:" + flgReadUe
                + " flgReadList:" + flgReadList
                + " flgReadWithStatusOnly:" + flgReadWithStatusOnly
                + " strReadIfStatusInListOnly:" + strReadIfStatusInListOnly;
    }

    /** 
     * get the flag for WikiParser that Wiki-UE will be read or ignored
     * @return                       flgReadUe - true/false = read/ignore
     */
    public boolean isFlgReadUe() {
        return flgReadUe;
    }

    /** 
     * set the flag for WikiParser that Wiki-UE will be read or ignored
     * @param flgReadUe              true/false = read/ignore
     */
    public void setFlgReadUe(final boolean flgReadUe) {
        this.flgReadUe = flgReadUe;
    }

    /** 
     * get the flag for WikiParser that Wiki-LIST will be read or ignored
     * @return                       flgReadList - true/false = read/ignore
     */
    public boolean isFlgReadList() {
        return flgReadList;
    }

    /** 
     * set the flag for WikiParser that Wiki-LIST will be read or ignored
     * @param flgReadList            true/false = read/ignore
     */
    public void setFlgReadList(final boolean flgReadList) {
        this.flgReadList = flgReadList;
    }

    /** 
     * get the flag for WikiParser that Nodes with status will 
     * be read or all others too
     * @return                       flgReadWithStatusOnly - true/false = read only with Status/ read all
     */
    public boolean isFlgReadWithStatusOnly() {
        return flgReadWithStatusOnly;
    }

    /** 
     * set the flag for WikiParser that Nodes with status will 
     * be read or all others too
     * @param flgReadWithStatusOnly  true/false = read only with Status/ read all
     */
    public void setFlgReadWithStatusOnly(final boolean flgReadWithStatusOnly) {
        this.flgReadWithStatusOnly = flgReadWithStatusOnly;
    }

    /** 
     * get the flag for WikiParser that Nodes with Workflowstatus will 
     * be read or all others too
     * @return                       flgReadWithWFStatusOnly - true/false = read only with WFStatus/ read all
     */
    public boolean isFlgReadWithWFStatusOnly() {
        return flgReadWithWFStatusOnly;
    }

    /** 
     * set the flag for WikiParser that only Nodes with Workflowstatus will 
     * be read or all others too
     * @param flgReadWithWFStatusOnly true/false = read only with WFStatus/ read all
     */
    public void setFlgReadWithWFStatusOnly(final boolean flgReadWithWFStatusOnly) {
        this.flgReadWithWFStatusOnly = flgReadWithWFStatusOnly;
    }

    /** 
     * get the commaseparated string of node-stati for WikiImporter to filter 
     * on import
     * @return                       strReadIfStatusInListOnly - commaseparated list of node-stati to filter
     */
    public String getStrReadIfStatusInListOnly() {
        return strReadIfStatusInListOnly;
    }

    /** 
     * set the commaseparated string of node-stati for WikiImporter to filter 
     * on import
     * @param strReadIfStatusInListOnly commaseparated list of node-stati to filter
     */
    public void setStrReadIfStatusInListOnly(final String strReadIfStatusInListOnly) {
        this.strReadIfStatusInListOnly = strReadIfStatusInListOnly;
    }
}
