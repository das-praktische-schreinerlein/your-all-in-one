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
package de.yaio.extension.dms.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.yaio.services.dms.storage.StorageResourceVersion;

/** 
 * businesslogic for dms
 * 
 * @FeatureDomain                DMS
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface DMSClient {

    /**
     * uploads the input to the configured dms
     * @FeatureDomain                DMS
     * @FeatureResult                response
     * @FeatureKeywords              DMS
     * @return                       response
     * @param id                     id of the content in dms
     * @param origFileName           original filename
     * @param input                  the data to store in dms
     * @throws IOException           if something went wrong
     */
    byte[] addContentToDMS(String id, String origFileName, 
                                                 InputStream input) throws IOException;

    /**
     * updates the input in the configured dms
     * @FeatureDomain                DMS
     * @FeatureResult                response
     * @FeatureKeywords              DMS
     * @return                       response
     * @param id                     id of the content in dms
     * @param origFileName           original filename
     * @param input                  the data to store in dms
     * @throws IOException           if something went wrong
     */
    byte[] updateContentInDMS(String id, String origFileName, 
                                                 InputStream input) throws IOException;

    /**
     * read the content from configured dms
     * @FeatureDomain                DMS
     * @FeatureResult                inputstream wirh the binary content
     * @FeatureKeywords              DMS
     * @param id                     id of the content in dms
     * @param version                version of the content
     * @throws IOException           if something went wrong
     * @return                       inputstream wirh the binary content
     */
    InputStream getContentFromDMS(final String id, final Integer version) throws IOException;

    /**
     * read the content from configured dms
     * @FeatureDomain                DMS
     * @FeatureResult                file wirh the binary content
     * @FeatureKeywords              DMS
     * @param id                     id of the content in dms
     * @param version                version of the content
     * @param useOriginalExtension   if checked then there will be a second call to get the orginial fileextension for the tmp-file
     * @throws IOException           if something went wrong
     * @return                       file the binary content
     */
    File getContentFileFromDMS(final String id, final Integer version, 
                               final boolean useOriginalExtension) throws IOException;

    /**
     * read the metadata for content from configured dms
     * @FeatureDomain                DMS
     * @FeatureResult                StorageResourceVersion
     * @FeatureKeywords              DMS
     * @param id                     id of the content in dms
     * @param version                version of the content
     * @return                       StorageResourceVersion
     * @throws IOException           if something went wrong
     */
    StorageResourceVersion getMetaDataForContentFromDMS(final String id, final Integer version) throws IOException;
}
