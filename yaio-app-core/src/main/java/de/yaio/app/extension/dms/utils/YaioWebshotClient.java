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
import de.yaio.services.webshot.client.WebshotClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/** 
 * businesslogic for webshot
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Service
public class YaioWebshotClient {
    
    @Value("${yaio.dms-client.yaio-webshot-service.baseurl}")
    protected String webshoturl;

    @Value("${yaio.dms-client.yaio-webshot-service.username}")
    protected String webshotusername;

    @Value("${yaio.dms-client.yaio-webshot-service.password}")
    protected String webshotpassword;

    protected WebshotClient client;

    public YaioWebshotClient() {
        client = WebshotClient.createClient(webshoturl, webshotusername, webshotpassword);
    }

    /**
     * create a webshot of the url
     * @return                       returns the webshot as png-file
     * @param url                    url to make a webshot from
     * @throws IOExceptionWithCause  if something logical went wrong
     * @throws IOException           if something physical went wrong
     */
    public byte[] getWebShotFromUrl(final String url) throws IOExceptionWithCause, IOException {
        return client.getWebShotFromUrl(url);
    }
}
