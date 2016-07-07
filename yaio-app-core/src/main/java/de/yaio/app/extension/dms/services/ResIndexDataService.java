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
package de.yaio.app.extension.dms.services;

import de.yaio.app.core.datadomain.ResIndexData;
import de.yaio.app.core.datadomain.ResLocData;
import de.yaio.app.core.datadomainservice.TriggeredDataDomainRecalc;
import de.yaio.commons.io.IOExceptionWithCause;

import java.io.IOException;
import java.io.InputStream;

/** 
 * businesslogic for dataDomain: ResIndexData (index url/file to dms)
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public interface ResIndexDataService extends TriggeredDataDomainRecalc {

    /** 
     * index the uploaded ResLoc-data to dms
     * @param node                   node
     * @throws IOExceptionWithCause  if something logical went wrong
     */
    void indexResLoc(ResLocData node) throws IOExceptionWithCause;


    /**
     * index the uploaded ResLoc-data to dms
     * @param datanode               node
     * @param fileName               original-filename
     * @param input                  the input to upload (uploadfile or url-webshot)
     * @throws IOExceptionWithCause  if something logical went wrong
     * @throws IOException           if something physical went wrong
     */
    void uploadResIndexToDMS(ResIndexData datanode, String fileName, InputStream input)
            throws IOExceptionWithCause, IOException;
}
