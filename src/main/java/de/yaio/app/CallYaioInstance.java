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
package de.yaio.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

/**
 * <h4>FeatureDomain:</h4>
 *     Administration
 * <h4>FeatureDescription:</h4>
 *     job to call admin-int5erface of yaio-instances
 * 
 * @package de.yaio.app
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class CallYaioInstance extends CmdLineJob {

    private static final Logger LOGGER =
        Logger.getLogger(CallYaioInstance.class);
    
    protected String password;
    protected String username;
    protected String yaioinstance;
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     job to call yaio-instances for admin-purposes
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the application
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param args the command line arguments
     */
    public CallYaioInstance(final String[] args) {
        super(args);
    }

    @Override
    protected Options addAvailiableCmdLineOptions() throws Exception {
        Options availiableCmdLineOptions = 
                        Configurator.getNewOptionsInstance();

        // add default-Options
        Option yaioinstanceOption = new Option(null, "yaioinstance", true,
                        "Url of the yaio-instance (http://yaio-prod.local");
        yaioinstanceOption.setRequired(true);
        availiableCmdLineOptions.addOption(yaioinstanceOption);

        Option usernameOption = new Option(null, "username", true,
                        "admin-username for login");
        usernameOption.setRequired(true);
        availiableCmdLineOptions.addOption(usernameOption);

        Option passwordOption = new Option(null, "password", true,
                        "admin-password for login");
        passwordOption.setRequired(true);
        availiableCmdLineOptions.addOption(passwordOption);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addAvailiableCmdLineOptions: " + availiableCmdLineOptions);
        }
        
        return availiableCmdLineOptions;
    }
    
    protected void initJob() throws Exception {
        password = Configurator.getInstance().getCommandLine().getOptionValue("password");
        username = Configurator.getInstance().getCommandLine().getOptionValue("username");
        yaioinstance = Configurator.getInstance().getCommandLine().getOptionValue("yaioinstance");
    }
    
    protected HttpContext prepareHttpContext() {
        HttpHost targetHost = new HttpHost(yaioinstance, 80, "http");
        
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(username, password));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        return context;
    }
    
    
    protected HttpResponse execureRequest(final HttpUriRequest request) throws ClientProtocolException, IOException {
        @SuppressWarnings("deprecation")
        HttpClient client = new DefaultHttpClient();
        HttpContext context = prepareHttpContext();
        HttpResponse response = null;
        response = client.execute(request, context);
        return response;    
    }
    
    
    protected StringBuffer callGetUrl(final String baseUrl, final Map<String, String> params) throws IOException {
        // map params
        String url = "http://" + yaioinstance + baseUrl;
        if (params != null && params.size() > 0) {
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
        HttpResponse response = execureRequest(request);
        
        // get response
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Response Code : " + response.getStatusLine().getStatusCode());
        }
        BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line).append("\n");
        }
 
        return result;
    }

    
    
    protected StringBuffer callPostUrl(final String baseUrl, 
                                       final Map<String, String> params, 
                                       final Map<String, String> fileParams) throws IOException {
        // create request
        String url = "http://" + yaioinstance + baseUrl;
        HttpPost request = new HttpPost(url);
 
        // map params
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.addTextBody(key, params.get(key), ContentType.TEXT_PLAIN);
            }
        }

        // map files
        if (fileParams != null && fileParams.size() > 0) {
            for (String key : fileParams.keySet()) {
                File file = new File(fileParams.get(key));
                builder.addBinaryBody("file", file, ContentType.TEXT_PLAIN, fileParams.get(key));
            }
        }
        
        // set request
        HttpEntity multipart = builder.build();
        request.setEntity(multipart);
        
        // add request header
        request.addHeader("User-Agent", "YAIOCaller");
        
        // call url
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sending 'POST' request to URL : " + url);
        }
        HttpResponse response = execureRequest(request);
        
        // get response
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Response Code : " + response.getStatusLine().getStatusCode());
        }
        BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
 
        return result;
    }
}
