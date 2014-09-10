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
package de.yaio.extension.datatransfer.jpa;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.extension.datatransfer.wiki.WikiExporter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes to JPA
 * 
 * @package de.yaio.extension.datatransfer.ical
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JPAExporter extends WikiExporter {
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service functions to export nodes to JPA
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the exporter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     */
    public JPAExporter() {
        super();
    }

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(JPAExporter.class);

    @Override
    public void initDataDomainFormatter() {
    };

    @Override
    @Transactional()
    public String getMasterNodeResult(DataDomain masterNode, OutputOptions oOptions)
            throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("save node:" + masterNode);
        }
        
        // look for this Basenode in DB
        BaseNode newNode = ((BaseNode)masterNode);
        BaseNode dbNode = BaseNode.findBaseNode(((BaseNode)masterNode).getSysUID());
        if (dbNode != null) {
            // delete the old dbNode with all children
            dbNode.removeChildNodesFromDB();
            dbNode.remove();
        }
        
        // save the newNode
        newNode.persist();
        
        // save the children
        newNode.persistChildNodesToDB(NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN);

        return "saved node: " + masterNode.getName() + "\n";
    }

    @Override
    public StringBuffer getNodeResult(DataDomain node,  String praefix,
            OutputOptions oOptions) throws Exception {
        throw new IllegalStateException("This function should not be used, but you used it for " + node);
    }
}
