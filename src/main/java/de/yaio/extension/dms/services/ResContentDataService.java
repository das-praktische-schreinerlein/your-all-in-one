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
package de.yaio.extension.dms.services;

import java.io.IOException;
import java.io.InputStream;

import de.yaio.core.datadomain.ResContentData;
import de.yaio.core.datadomain.ResLocData;
import de.yaio.core.datadomainservice.TriggeredDataDomainRecalc;

/** 
 * businesslogic for dataDomain: ResContentData (upload url/file to dms)
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface ResContentDataService extends TriggeredDataDomainRecalc {

    /** 
     * uploads the ResLoc-data to dms
     * @FeatureDomain                DMS
     * @FeatureResult                updates membervars
     * @FeatureKeywords              DMS
     * @param datanode               node
     * @throws IOException           Exceptions possible
     */
    void uploadResLocToDMS(ResLocData datanode) throws IOException;

    /**
     * uploads the ResLoc-data to dms
     * @FeatureDomain                DMS
     * @FeatureResult                updates membervars
     * @FeatureKeywords              DMS
     * @param datanode               node
     * @param fileName               original-filename
     * @param input                  the input to upload (uploadfile or url-webshot)
     * @throws IOException           Exceptions possible
     */
    void uploadResContentToDMS(ResContentData datanode, String fileName, InputStream input) throws IOException;
}
