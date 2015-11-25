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
package de.yaio.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/** 
 * utils for http-calls
 * 
 * @FeatureDomain                Utils
 * @package                      de.yaio.utils
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class HttpUtils {
    
    private static final Logger LOGGER =
                    Logger.getLogger(HttpUtils.class);
                
    /** 
     * execute the prepared Request
     * @FeatureDomain                Tools - URL-Handling
     * @FeatureResult                returnValues - HttpResponse
     * @FeatureKeywords              URL-Handling
     * @param request                request to call 
     * @param username               username for auth
     * @param password               password for auth
     * @return                       HttpResponse
     * @throws ClientProtocolException possible Exception if Request-state <200 > 299 
     * @throws IOException            possible Exception if Request-state <200 > 299
     */
    public static HttpResponse executeRequest(final HttpUriRequest request, final String username, 
                                              final String password) throws ClientProtocolException, IOException {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        HttpResponse response = client.execute(request);
        return response;
    }

    /** 
     * execute GET-Request for url with params
     * @FeatureDomain                Tools - URL-Handling
     * @FeatureResult                returnValues - ByteArray with the textresponse
     * @FeatureKeywords              URL-Handling
     * @param baseUrl                the url to call
     * @param username               username for auth
     * @param password               password for auth
     * @param params                 params for the request
     * @return                       Response-Text as ByteArray
     * @throws IOException           possible Exception if Request-state <200 > 299 
     */
    public static byte[] callGetUrl(final String baseUrl, final String username, final String password,
                                final Map<String, String> params) throws IOException {
        HttpResponse response = callGetUrlPure(baseUrl, username, password, params);
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() + " for urlcall:" + baseUrl);
        }
        
        HttpEntity entity = response.getEntity();
        return EntityUtils.toByteArray(entity);
    }

    /** 
     * execute GET-Request for url with params
     * @FeatureDomain                Tools - URL-Handling
     * @FeatureResult                returnValues - ByteArray with the textresponse
     * @FeatureKeywords              URL-Handling
     * @param baseUrl                the url to call
     * @param username               username for auth
     * @param password               password for auth
     * @param params                 params for the request
     * @return                       Response
     * @throws IOException           possible Exception if Request-state <200 > 299 
     */
    public static HttpResponse callGetUrlPure(final String baseUrl, final String username, final String password,
                                final Map<String, String> params) throws IOException {
        // map params
        String url = baseUrl;
        if (MapUtils.isNotEmpty(params)) {
            url += "?";
            for (String key : params.keySet()) {
                url += "&" + key + "=" + params.get(key);
            }
        }

        // create request
        HttpGet request = new HttpGet(url);
 
        // add request header
        request.addHeader("User-Agent", "YAIOCaller");
 
        // call url
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sending 'GET' request to URL : " + url);
        }
        HttpResponse response = HttpUtils.executeRequest(request, username, password);
        
        // get response
        int retCode = response.getStatusLine().getStatusCode();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Response Code : " + retCode);
        }
        return response;
    }
    
    /** 
     * execute POST-Request for url with params
     * @FeatureDomain                Tools - URL-Handling
     * @FeatureResult                returnValues - ByteArray with the textresponse
     * @FeatureKeywords              URL-Handling
     * @param baseUrl                the url to call
     * @param username               username for auth
     * @param password               password for auth
     * @param params                 params for the request
     * @param textFileParams         text-files to upload
     * @param binFileParams          bin-files to upload
     * @return                       Response-Text as ByteArray
     * @throws IOException           possible Exception if Request-state <200 > 299 
     */
    public static byte[] callPostUrl(final String baseUrl, final String username, final String password,
                                 final Map<String, String> params, 
                                 final Map<String, String> textFileParams,
                                 final Map<String, String> binFileParams) throws IOException {
        // create request
        HttpResponse response = callPostUrlPure(baseUrl, username, password, params, textFileParams, binFileParams);
        
        // get response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() + " for urlcall:" + baseUrl);
        }

        HttpEntity entity = response.getEntity();
        return EntityUtils.toByteArray(entity);
    }

    /** 
     * execute POST-Request for url with params
     * @FeatureDomain                Tools - URL-Handling
     * @FeatureResult                returnValues - HttpResponse
     * @FeatureKeywords              URL-Handling
     * @param baseUrl                the url to call
     * @param username               username for auth
     * @param password               password for auth
     * @param params                 params for the request
     * @param textFileParams         text-files to upload
     * @param binFileParams          bin-files to upload
     * @return                       HttpResponse
     * @throws IOException           possible Exception if Request-state <200 > 299 
     */
    public static HttpResponse callPostUrlPure(final String baseUrl, final String username, final String password,
                                 final Map<String, String> params, 
                                 final Map<String, String> textFileParams,
                                 final Map<String, String> binFileParams) throws IOException {
        // create request
        HttpPost request = new HttpPost(baseUrl);
 
        // map params
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (MapUtils.isNotEmpty(params)) {
            for (String key : params.keySet()) {
                builder.addTextBody(key, params.get(key), ContentType.TEXT_PLAIN);
            }
        }

        // map files
        if (MapUtils.isNotEmpty(textFileParams)) {
            for (String key : textFileParams.keySet()) {
                File file = new File(textFileParams.get(key));
                builder.addBinaryBody(key, file, ContentType.DEFAULT_TEXT, textFileParams.get(key));
            }
        }
        // map files
        if (MapUtils.isNotEmpty(binFileParams)) {
            for (String key : binFileParams.keySet()) {
                File file = new File(binFileParams.get(key));
                builder.addBinaryBody(key, file, ContentType.DEFAULT_BINARY, binFileParams.get(key));
            }
        }
        
        // set request
        HttpEntity multipart = builder.build();
        request.setEntity(multipart);
        
        // add request header
        request.addHeader("User-Agent", "YAIOCaller");
        
        // call url
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sending 'POST' request to URL : " + baseUrl);
        }
        HttpResponse response = executeRequest(request, username, password);
        
        // get response
        int retCode = response.getStatusLine().getStatusCode();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Response Code : " + retCode);
        }
        return response;
    }

    /** 
     * execute POST-Request for url with params
     * @FeatureDomain                Tools - URL-Handling
     * @FeatureResult                returnValues - HttpResponse
     * @FeatureKeywords              URL-Handling
     * @param baseUrl                the url to call
     * @param username               username for auth
     * @param password               password for auth
     * @param params                 params for the request
     * @param textFileParams         text-files to upload
     * @param binFileParams          bin-files to upload
     * @return                       HttpResponse
     * @throws IOException           possible Exception if Request-state <200 > 299 
     */
    public static HttpResponse callPatchUrlPure(final String baseUrl, final String username, final String password,
                                 final Map<String, String> params, 
                                 final Map<String, String> textFileParams,
                                 final Map<String, String> binFileParams) throws IOException {
        // create request
        HttpPatch request = new HttpPatch(baseUrl);
 
        // map params
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (MapUtils.isNotEmpty(params)) {
            for (String key : params.keySet()) {
                builder.addTextBody(key, params.get(key), ContentType.TEXT_PLAIN);
            }
        }

        // map files
        if (MapUtils.isNotEmpty(textFileParams)) {
            for (String key : textFileParams.keySet()) {
                File file = new File(textFileParams.get(key));
                builder.addBinaryBody(key, file, ContentType.DEFAULT_TEXT, textFileParams.get(key));
            }
        }
        // map files
        if (MapUtils.isNotEmpty(binFileParams)) {
            for (String key : binFileParams.keySet()) {
                File file = new File(binFileParams.get(key));
                builder.addBinaryBody(key, file, ContentType.DEFAULT_BINARY, binFileParams.get(key));
            }
        }
        
        // set request
        HttpEntity multipart = builder.build();
        request.setEntity(multipart);
        
        // add request header
        request.addHeader("User-Agent", "YAIOCaller");
        
        // call url
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sending 'PATCH' request to URL : " + baseUrl);
        }
        HttpResponse response = executeRequest(request, username, password);
        
        // get response
        int retCode = response.getStatusLine().getStatusCode();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Response Code : " + retCode);
        }
        return response;
    }
}
