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
import de.yaio.services.metaextract.client.MetaExtractClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/** 
 * businesslogic for metaextract
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class YaioMetaExtractClient {
    @Value("${yaio.dms-client.yaio-metaextract-service.baseurl}")
    protected String metaextracturl;

    @Value("${yaio.dms-client.yaio-metaextract-service.username}")
    protected String metaextractusername;

    @Value("${yaio.dms-client.yaio-metaextract-service.password}")
    protected String metaextractpassword;

    protected MetaExtractClient client;

    public YaioMetaExtractClient() {
        client = MetaExtractClient.createClient(metaextracturl, metaextractusername, metaextractpassword);
    }
    
    /**
     * create a metaextract of the url
     * @return                       returns the metaextract as JSON
     * @param url                    url to make a metaextract from
     * @throws IOException           if something went wrong
     */
    public byte[] getMetaExtractFromUrl(final String url) throws IOException, IOExceptionWithCause {
        return client.getMetaExtractFromUrl(url);
    }

    /**
     * create a metaextract of the file
     * @return                       returns the metaextract as JSON
     * @param fileName               file to make a metaextract from
     * @throws IOException           if something went wrong
     */
    public byte[] getMetaExtractFromFile(final String fileName) throws IOException, IOExceptionWithCause {
        return client.getMetaExtractFromFile(fileName);
    }
}
