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

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImporterImpl;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *     import of Nodes from JPA
 * 
 * @package de.yaio.extension.datatransfer.jpa
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JPAImporter extends ImporterImpl {

    private static final Logger LOGGER =
            Logger.getLogger(JPAImporter.class);

    public JPAImporter(ImportOptions options) {
        super(options);
    }
    
    public DataDomain getBaseNodeBySysUID(String sysUID) {
        // look for this Basenode in DB
        BaseNode dbNode = BaseNode.findBaseNode(sysUID);
        
        if (dbNode != null) {
            dbNode.initChildNodesFromDB(-1);
        }
        
        return dbNode;
    }
}
