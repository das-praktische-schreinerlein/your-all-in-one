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
package de.yaio.rest.controller;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     a violation-obj which can be used to inform the requesting client about 
 *     spring-violations, when performing update-actions on NodeController 
 *     the RESTful Web Services for BaseNodes<br>
 *     masquerading spring-violations which will cause exceptions because the 
 *     path is at response-time no more available..)
 *      
 * @package de.yaio.rest.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeViolation {
    /** the path to the resource which caused the violation - fieldname**/
    public String path;
    /** the userfriendly error-message **/
    public String message;
    /** the technical messagetemplate **/
    public String messageTemplate;
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     create a violation-obj (masquerading spring-violations which will
     *     cause exceptions because the path is at response-time no more available..)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>a violation-obj
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param path - the path to the resource which caused the violation (fieldname)
     * @param message - the userfriendly error-message
     * @param messageTemplate -the technical messagetemplate
     */
    public NodeViolation(String path, String message, String messageTemplate) {
        super();
        this.path = path;
        this.message = message;
        this.messageTemplate = messageTemplate;
    }

    /**
     * @return the {@link NodeViolation#path}
     */
    public String getPath() {
        return this.path;
    }


    /**
     * @param path the {@link NodeViolation#path} to set
     */
    public void setPath(String path) {
        this.path = path;
    }


    /**
     * @return the {@link NodeViolation#message}
     */
    public String getMessage() {
        return this.message;
    }


    /**
     * @param message the {@link NodeViolation#message} to set
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * @return the {@link NodeViolation#messageTemplate}
     */
    public String getMessageTemplate() {
        return this.messageTemplate;
    }


    /**
     * @param messageTemplate the {@link NodeViolation#messageTemplate} to set
     */
    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }


}