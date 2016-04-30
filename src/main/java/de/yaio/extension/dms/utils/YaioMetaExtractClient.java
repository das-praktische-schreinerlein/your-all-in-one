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

import de.yaio.commons.http.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** 
 * businesslogic for metaextract
 * 
 * @FeatureDomain                DMS
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class YaioMetaExtractClient {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(YaioMetaExtractClient.class);

    @Value("${yaio.dms-client.yaio-metaextract-service.baseurl}")
    protected String metaextracturl;

    @Value("${yaio.dms-client.yaio-metaextract-service.username}")
    protected String metaextractusername;

    @Value("${yaio.dms-client.yaio-metaextract-service.password}")
    protected String metaextractpassword;
    
    /**
     * create a metaextract of the url
     * @return                       returns the metaextract as JSON
     * @param url                    url to make a metaextract from
     * @throws IOException           if something went wrong
     */
    public byte[] getMetaExtractFromUrl(final String url) throws IOException {
        // get metadata from url
        Map<String, String> params = new HashMap<String, String>();
        params.put("lang", "de");
        params.put("url", url);

        // call url
        String baseUrl = metaextracturl + "/getByUrl";
        HttpResponse response = HttpUtils.callPostUrlPure(baseUrl,
                        metaextractusername, metaextractpassword, params, null, null);
        HttpEntity entity = response.getEntity();
        
        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() 
                            + " for baseurl:" + baseUrl + " with url:" + url 
                            + " response:" + EntityUtils.toString(entity));
        }

        return EntityUtils.toByteArray(entity);
    }

    /**
     * create a metaextract of the file
     * @return                       returns the metaextract as JSON
     * @param fileName               file to make a metaextract from
     * @throws IOException           if something went wrong
     */
    public byte[] getMetaExtractFromFile(final String fileName) throws IOException {
        // get metadata from file
        Map<String, String> params = new HashMap<String, String>();
        params.put("lang", "de");
        Map<String, String> binfileParams = new HashMap<String, String>();
        binfileParams.put("file", fileName);

        // call url
        String baseUrl = metaextracturl + "/getByFile";
        HttpResponse response = HttpUtils.callPostUrlPure(baseUrl,
                        metaextractusername, metaextractpassword, params, null, binfileParams);
        HttpEntity entity = response.getEntity();
        
        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() 
                            + " for baseurl:" + baseUrl + " with file:" + fileName 
                            + " response:" + EntityUtils.toString(entity));
        }

        return EntityUtils.toByteArray(entity);
    }
}
