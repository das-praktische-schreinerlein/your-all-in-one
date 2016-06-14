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
package de.yaio.webapp.restcontroller;

import de.yaio.core.node.BaseNode;

import java.util.List;

/** 
 * responseobject for statistic-functions of the NodeController
 * for RESTfull-services<br>
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.webapp.restcontroller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class StatisticsResponse {

    /** the resulting state of the request OK/ERROR **/
    public String state;
    /** the corresponding message to the state **/
    public String stateMsg;
    /** the list of values **/
    public List values;

    /**
     * create a response-obj
     * @param state                  the resulting state of the request OK/ERROR
     * @param stateMsg               the corresponding message to the state
     * @param values                 the resulting values
     */
    public StatisticsResponse(final String state, final String stateMsg,
                              final List values) {
        super();
        this.state = state;
        this.stateMsg = stateMsg;
        this.values = values;
    }

    /**
     * @return                       the {@link StatisticsResponse#state}
     */
    public String getState() {
        return this.state;
    }

    /**
     * @param state                  the {@link StatisticsResponse#state} to set
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * @return                       the {@link StatisticsResponse#stateMsg}
     */
    public String getStateMsg() {
        return this.stateMsg;
    }

    /**
     * @param stateMsg               the {@link StatisticsResponse#stateMsg} to set
     */
    public void setStateMsg(final String stateMsg) {
        this.stateMsg = stateMsg;
    }

    /**
     * @return                       the {@link StatisticsResponse#values}
     */
    public List getValues() {
        return this.values;
    }

    /**
     * @param values                  the {@link StatisticsResponse#values} to set
     */
    public void setValues(final List values) {
        this.values = values;
    }
}
