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

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;

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
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(DataUtils.class);
    protected static MessageDigest objMD5Coder;
    static { 
        try {
            objMD5Coder = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    

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
    public static String htmlEscapeText(String src) {
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
    public static Map<String, String> initMapFromCsvString(String csvString) {
        Map<String, String> mpStates = null;
        if (   csvString != null 
            && csvString.length() > 0) {
            mpStates = new HashMap<String, String>();
            String [] arrStatusFilter =
                            csvString.split(",");
            for (int zaehler = 0; zaehler < arrStatusFilter.length; zaehler++) {
                mpStates.put(arrStatusFilter[zaehler], arrStatusFilter[zaehler]);
            }
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
    public static String generateCheckSum(String data) {
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
            praefix = (value < 16 ? "0" : "");
            strbuf.append(praefix);
            strbuf.append(Integer.toHexString(value).toUpperCase());
        }        

        return strbuf.toString();
    }
    
    public static Date getNewDate(Date oldDate) {
        return (oldDate != null ? new Date(oldDate.getTime()) : null);
    }
}
