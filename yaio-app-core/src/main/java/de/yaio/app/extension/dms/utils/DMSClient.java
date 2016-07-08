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
package de.yaio.app.extension.dms.utils;

import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.services.dms.api.model.StorageResource;
import de.yaio.services.dms.api.model.StorageResourceVersion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
     * @return                       StorageResource
     * @param id                     id of the content in dms
     * @param origFileName           original filename
     * @param input                  the data to store in dms
     * @throws IOException           if something went wrong
     */
    StorageResource addContentToDMS(String id, String origFileName, 
                                                 InputStream input) throws IOExceptionWithCause, IOException;

    /**
     * updates the input in the configured dms
     * @return                       StorageResource
     * @param id                     id of the content in dms
     * @param origFileName           original filename
     * @param input                  the data to store in dms
     * @throws IOException           if something went wrong
     */
    StorageResource updateContentInDMS(String id, String origFileName,
                                       InputStream input) throws IOExceptionWithCause, IOException;

    /**
     * read the content from configured dms
     * @param id                     id of the content in dms
     * @param version                version of the content
     * @throws IOException           if something went wrong
     * @return                       inputstream wirh the binary content
     */
    InputStream getContentFromDMS(final String id, final Integer version) throws IOExceptionWithCause, IOException;

    /**
     * read the content from configured dms
     * @param id                     id of the content in dms
     * @param version                version of the content
     * @param useOriginalExtension   if checked then there will be a second call to get the orginial fileextension for the tmp-file
     * @throws IOException           if something went wrong
     * @return                       file the binary content
     */
    File getContentFileFromDMS(final String id, final Integer version, 
                               final boolean useOriginalExtension) throws IOExceptionWithCause, IOException;

    /**
     * read the metadata for content from configured dms
     * @param id                     id of the content in dms
     * @param version                version of the content
     * @return                       StorageResourceVersion
     * @throws IOException           if something went wrong
     */
    StorageResourceVersion getMetaDataForContentFromDMS(final String id, final Integer version)
            throws IOExceptionWithCause, IOException;
}
