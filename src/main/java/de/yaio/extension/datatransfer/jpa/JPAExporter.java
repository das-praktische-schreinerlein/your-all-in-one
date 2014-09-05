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

import java.util.Date;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.formatter.BaseDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.DescDataFormatterImpl;
import de.yaio.datatransfer.exporter.formatter.FormatterImpl;
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
    public String getMasterNodeResult(DataDomain masterNode, OutputOptions oOptions)
            throws Exception {
        
        // iterate 
        super.getMasterNodeResult(masterNode, oOptions);
        
        return null;
    }

    @Override
    public StringBuffer getNodeResult(DataDomain node,  String praefix,
            OutputOptions oOptions) throws Exception {
        // merge BaseNode with db and all recursivly
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("save node:" + node);
        }
        
        // look for this Basenode in DB
        BaseNode newNode = ((BaseNode)node);
        BaseNode dbNode = BaseNode.findBaseNode(((BaseNode)node).getSysUID());
        if (dbNode != null) {
            // delete the old dbNode with all children
            dbNode.remove();
        }
        
        // save the newNode with new children
        newNode.persist();
        
        return new StringBuffer("saved node: " + node.getName() + "\n");
    }
}
