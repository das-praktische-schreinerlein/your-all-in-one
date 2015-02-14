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

import java.util.List;

import de.yaio.core.node.BaseNode;

/**
 * <h4>FeatureDomain:</h4>
 *     Webservice
 * <h4>FeatureDescription:</h4>
 *     responseobject for search-functions of the NodeController
 *     for RESTfull-services<br>
 *      
 * @package de.yaio.webapp.controller
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeSearchResponse {
    
    /** the resulting state of the request OK/ERROR **/
    public String state;
    /** the corresponding message to the state **/
    public String stateMsg;
    /** the list of nodes **/
    public List<BaseNode> nodes;
    /** index of the curPage **/
    public Long curPage;
    /** pageSize per page**/
    public Long pageSize;
    /** count the result of the search**/
    public Long count;

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     create a response-obj
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>a response-obj with the resulting nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     * @param state - the resulting state of the request OK/ERROR
     * @param stateMsg - the corresponding message to the state
     * @param nodes - the resulting nodes for the search page
     * @param curPage - current page to show
     * @param pageSize - max items per page
     * @param count - total of all items found
     */
    public NodeSearchResponse(final String state, final String stateMsg,
                           final List<BaseNode> nodes,
                           final Long curPage,
                           final Long pageSize,
                           final Long count) {
        super();
        this.state = state;
        this.stateMsg = stateMsg;
        this.nodes = nodes;
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.count = count;
    }

    /**
     * @return the {@link NodeSearchResponse#state}
     */
    public String getState() {
        return this.state;
    }

    /**
     * @param state the {@link NodeSearchResponse#state} to set
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * @return the {@link NodeSearchResponse#stateMsg}
     */
    public String getStateMsg() {
        return this.stateMsg;
    }

    /**
     * @param stateMsg the {@link NodeSearchResponse#stateMsg} to set
     */
    public void setStateMsg(final String stateMsg) {
        this.stateMsg = stateMsg;
    }

    /**
     * @return the {@link NodeSearchResponse#nodes}
     */
    public List<BaseNode> getNodes() {
        return this.nodes;
    }

    /**
     * @param nodes the {@link NodeSearchResponse#nodes} to set
     */
    public void setNodes(final List<BaseNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the {@link NodeSearchResponse#curPage}
     */
    public Long getCurPage() {
        return this.curPage;
    }

    /**
     * @param curPage the {@link NodeSearchResponse#curPage} to set
     */
    public void setCurPage(final Long curPage) {
        this.curPage = curPage;
    }

    /**
     * @return the {@link NodeSearchResponse#pageSize}
     */
    public Long getPageSize() {
        return this.pageSize;
    }

    /**
     * @param pageSize the {@link NodeSearchResponse#pageSize} to set
     */
    public void setPageSize(final Long pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the {@link NodeSearchResponse#count}
     */
    public Long getCount() {
        return this.count;
    }

    /**
     * @param count the {@link NodeSearchResponse#count} to set
     */
    public void setCount(final Long count) {
        this.count = count;
    }
}
