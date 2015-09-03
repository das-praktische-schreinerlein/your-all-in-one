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

package de.yaio.extension.datatransfer.json;

import java.util.List;

import de.yaio.core.datadomain.DataDomain;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     abstract structure of the basic json-response
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class CommonJSONResponse {
    /** the resulting state of the request OK/ERROR **/
    protected String state;
    /** the corresponding message to the state **/
    protected String stateMsg;
    /** the resulting node for the request **/
    protected DataDomain node;
    /** the list of the parentSysUID for the node **/
    protected List<String> parentIdHierarchy;

    public CommonJSONResponse() {
    }

    public CommonJSONResponse(final String state, final String stateMsg, 
                        final DataDomain node, final List<String> parentIdHierarchy) {
        super();
        this.state = state;
        this.stateMsg = stateMsg;
        this.node = node;
        this.parentIdHierarchy = parentIdHierarchy;
    }

    /**
     * @return the {@link CommonJSONResponse#state}
     */
    public final String getState() {
        return this.state;
    }

    /**
     * @param state the {@link CommonJSONResponse#state} to set
     */
    public final void setState(final String state) {
        this.state = state;
    }

    /**
     * @return the {@link CommonJSONResponse#stateMsg}
     */
    public final String getStateMsg() {
        return this.stateMsg;
    }

    /**
     * @param stateMsg the {@link CommonJSONResponse#stateMsg} to set
     */
    public final void setStateMsg(final String stateMsg) {
        this.stateMsg = stateMsg;
    }

    /**
     * @return the {@link CommonJSONResponse#node}
     */
    public final DataDomain getNode() {
        return this.node;
    }

    /**
     * @param node the {@link CommonJSONResponse#node} to set
     */
    public final void setNode(final DataDomain node) {
        this.node = node;
    }

    /**
     * @return the {@link CommonJSONResponse#parentIdHierarchy}
     */
    public final List<String> getParentIdHierarchy() {
        return this.parentIdHierarchy;
    }

    /**
     * @param parentIdHierarchy the {@link CommonJSONResponse#parentIdHierarchy} to set
     */
    public final void setParentIdHierarchy(final List<String> parentIdHierarchy) {
        this.parentIdHierarchy = parentIdHierarchy;
    }
}