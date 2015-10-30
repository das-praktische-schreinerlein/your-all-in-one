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
package de.yaio.rest.controller;

/** 
 * a violation-obj which can be used to inform the requesting client about 
 * spring-violations, when performing update-actions on NodeController 
 * the RESTful Web Services for BaseNodes<br>
 * masquerading spring-violations which will cause exceptions because the 
 * path is at response-time no more available..)
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeViolation {
    /** the path to the resource which caused the violation - fieldname**/
    public String path;
    /** the userfriendly error-message **/
    public String message;
    /** the technical messagetemplate **/
    public String messageTemplate;
    
    /** 
     * create a violation-obj (masquerading spring-violations which will
     * cause exceptions because the path is at response-time no more available..)
     * @FeatureDomain                Constructor
     * @FeatureResult                a violation-obj
     * @FeatureKeywords              Constructor
     * @param path                   the path to the resource which caused the violation (fieldname)
     * @param message                the userfriendly error-message
     * @param messageTemplate        the technical messagetemplate
     */
    public NodeViolation(final String path, final String message, final String messageTemplate) {
        super();
        this.path = path;
        this.message = message;
        this.messageTemplate = messageTemplate;
    }

    /**
     * @return                       the {@link NodeViolation#path}
     */
    public String getPath() {
        return this.path;
    }


    /**
     * @param path                   the {@link NodeViolation#path} to set
     */
    public void setPath(final String path) {
        this.path = path;
    }


    /**
     * @return                       the {@link NodeViolation#message}
     */
    public String getMessage() {
        return this.message;
    }


    /**
     * @param message                the {@link NodeViolation#message} to set
     */
    public void setMessage(final String message) {
        this.message = message;
    }


    /**
     * @return                       the {@link NodeViolation#messageTemplate}
     */
    public String getMessageTemplate() {
        return this.messageTemplate;
    }


    /**
     * @param messageTemplate        the {@link NodeViolation#messageTemplate} to set
     */
    public void setMessageTemplate(final String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }


}
