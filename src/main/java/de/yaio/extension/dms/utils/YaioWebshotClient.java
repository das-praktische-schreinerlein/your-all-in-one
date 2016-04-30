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
 * businesslogic for webshot
 * 
 * @FeatureDomain                DMS
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class YaioWebshotClient {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(YaioWebshotClient.class);

    @Value("${yaio.dms-client.yaio-webshot-service.baseurl}")
    protected String webshoturl;

    @Value("${yaio.dms-client.yaio-webshot-service.username}")
    protected String webshotusername;

    @Value("${yaio.dms-client.yaio-webshot-service.password}")
    protected String webshotpassword;
    
    /**
     * create a webshot of the url
     * @return                       returns the webshot as png-file
     * @param url                    url to make a webshot from
     * @throws IOException           if something went wrong
     */
    public byte[] getWebShotFromUrl(final String url) throws IOException {
        // get image from url
        Map<String, String> params = new HashMap<String, String>();
        params.put("url", url);

        // call url
        String baseUrl = webshoturl + "/url2png";
        HttpResponse response = HttpUtils.callPostUrlPure(baseUrl,
                        webshotusername, webshotpassword, params, null, null);
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
}
