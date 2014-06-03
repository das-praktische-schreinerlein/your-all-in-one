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

    public boolean flgReadUe = true;
    public boolean flgReadList = true;
    public boolean flgReadWithStatusOnly = false;
    public boolean flgReadWithWFStatusOnly = false;
    public String strReadIfStatusInListOnly = null;

    public String toString() {
        return "WikiImportOptions" 
                + " flgReadUe:" + flgReadUe
                + " flgReadList:" + flgReadList
                + " flgReadWithStatusOnly:" + flgReadWithStatusOnly
                + " strReadIfStatusInListOnly:" + strReadIfStatusInListOnly
                ;

    }

    public boolean isFlgReadUe() {
        return flgReadUe;
    }

    public void setFlgReadUe(boolean flgReadUe) {
        this.flgReadUe = flgReadUe;
    }

    public boolean isFlgReadList() {
        return flgReadList;
    }

    public void setFlgReadList(boolean flgReadList) {
        this.flgReadList = flgReadList;
    }

    public boolean isFlgReadWithStatusOnly() {
        return flgReadWithStatusOnly;
    }

    public void setFlgReadWithStatusOnly(boolean flgReadWithStatusOnly) {
        this.flgReadWithStatusOnly = flgReadWithStatusOnly;
    }

    public boolean isFlgReadWithWFStatusOnly() {
        return flgReadWithWFStatusOnly;
    }

    public void setFlgReadWithWFStatusOnly(boolean flgReadWithWFStatusOnly) {
        this.flgReadWithWFStatusOnly = flgReadWithWFStatusOnly;
    }

    public String getStrReadIfStatusInListOnly() {
        return strReadIfStatusInListOnly;
    }

    public void setStrReadIfStatusInListOnly(String strReadIfStatusInListOnly) {
        this.strReadIfStatusInListOnly = strReadIfStatusInListOnly;
    }
}
