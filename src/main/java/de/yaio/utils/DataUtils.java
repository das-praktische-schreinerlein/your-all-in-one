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
package de.yaio.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * <h4>FeatureDomain:</h4>
 *     Utils
 * <h4>FeatureDescription:</h4>
 *     Utils for managing data
 * @package de.yaio.utils
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category Utils
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class DataUtils {
    protected static MessageDigest objMD5Coder;
    static { 
        try {
            objMD5Coder = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DataUtils.class);
    
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - Converter
     * <h4>FeatureDescription:</h4>
     *     escape html entities
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue html-escaped string
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Converter
     * @param src - the string to escape all html
     * @return the html-escaped string
     */
    public static String htmlEscapeText(final String src) {
        String text = src;
        
        // test ob beide belegt
        if (text != null) {
            text = text.replace("&", "&amp;");
            text = text.replace("<", "&lt;");
            text = text.replace(">", "&gt;");
            text = text.replace("\"", "&quot;");
            text = text.replace("'", "&#x27;");
            text = text.replace("/", "&#x2F;");
        }

        return text;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - Converter
     * <h4>FeatureDescription:</h4>
     *     lazy escape html entities
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue html-escaped string
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Converter
     * @param src - the string to escape all html
     * @return the html-escaped string
     */
    public static String htmlEscapeTextLazy(final String src) {
        String text = src;
        
        // test ob beide belegt
        if (text != null) {
            text = text.replace("<", "&lt;");
            text = text.replace(">", "&gt;");
        }

        return text;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - Converter
     * <h4>FeatureDescription:</h4>
     *     convert commaseparated string to map
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue map
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Converter
     * @param csvString - the string to split
     * @return the map of string
     */
    public static Map<String, String> initMapFromCsvString(final String csvString) {
        Map<String, String> mpStates = null;
        if (!StringUtils.isEmpty(csvString)) {
            mpStates = new HashMap<String, String>();
            String [] arrStatusFilter = csvString.split(",");
            for (int zaehler = 0; zaehler < arrStatusFilter.length; zaehler++) {
                mpStates.put(arrStatusFilter[zaehler], arrStatusFilter[zaehler]);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("map for csvString:" + csvString 
                            + " = " + mpStates);
        }
        
        return mpStates;
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     BusinessLogic
     * <h4>FeatureDescription:</h4>
     *     calcs the checksum of the data
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue String - checksum of the node
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     BusinessLogic
     * @param data - data to get the checksum
     * @return the checksum
     * @throws Exception - parser/format/io-Exceptions possible
     */
    public static String generateCheckSum(final String data) {
        // Checksumme
        objMD5Coder.update(data.getBytes(), 0, data.length());
        final byte[] digest = objMD5Coder.digest();

        // menschenlesbar
        StringBuffer strbuf = new StringBuffer();
        String praefix = "";
        int b = 0;
        int value = 0;
        for (int i = 0; i < digest.length; i++) {
            // als Hex
            b = digest[i];
            value = (b & 0x7F) + (b < 0 ? 128 : 0);
            praefix = value < 16 ? "0" : "";
            strbuf.append(praefix);
            strbuf.append(Integer.toHexString(value).toUpperCase());
        }        

        return strbuf.toString();
    }
    
    /** 
     * copy date
     * @param oldDate - date to copy
     * @return new Date
     */
    public static Date getNewDate(final Date oldDate) {
        return oldDate != null ? new Date(oldDate.getTime()) : null;
    }
    
    /** 
     * @return dateformat-instance for german date dd.MM.yyyy 
     */
    public static DateFormat getDF() {
        return  new SimpleDateFormat("dd.MM.yyyy");
    }
    
    /**
     * @return dateformat-instance for german time HH:mm
     */
    public static DateFormat getTF() {
        return new SimpleDateFormat("HH:mm");
    }

    /**
     * @return dateformat-instance for german datetime dd.MM.yyyy HH:mm
     * */
    public static DateFormat getDTF() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm");
    }
    
    /**
     * @return dateformat-instance for UID yyyyMMddHHmmssSSS
     */
    public static DateFormat getUIDF() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS");
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - URL-Handling
     * <h4>FeatureDescription:</h4>
     *     extract url from string (defaults if not set scheme:http host:localhost port:80)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValues - the extracted url
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     URL-Handling
     * @param value                  - string to extract url from
     * @return                       - the extracted url
     * @throws MalformedURLException - possible invalid URL
     */
    public static URL extractWebUrl(final String value) throws MalformedURLException {
        if (StringUtils.isEmpty(value)) {
            LOGGER.warn("cant parse url from empty value");
            return null;
        }
        
        // define url-pattern
        String urlPattern = "(https?://)?([-a-zA-Z\\.]+)(:[0-9]+)?(\\?.*)?$";
        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        URL url = null;
        
        // extract url-parts if matches
        if (matcher.matches()) {
            int matcherindex = 1;
            String protocol = "http";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                protocol = matcher.group(matcherindex).toLowerCase().trim();
            }
            
            matcherindex = 2;
            String host = "localhost";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                host = matcher.group(matcherindex).toLowerCase().trim();
            }

            matcherindex = 3;
            int port = 80;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                port = Integer.parseInt(matcher.group(matcherindex).trim());
            }

            matcherindex = 4;
            String context = "";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Pattern: " + pattern + " " 
                    + matcherindex + ":" + matcher.group(matcherindex));
            }
            if (matcher.group(matcherindex) != null) {
                context = matcher.group(matcherindex).trim();
            }
            
            url = new URL(protocol, host, port, context);
            LOGGER.debug("parsed url:" + url + " from value:" + value);
            return url;
        }
        
        // return not found
        LOGGER.warn("cant parse url from value:" + value);
        return null;
    }    
}
