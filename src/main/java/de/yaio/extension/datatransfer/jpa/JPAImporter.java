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

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     create object to import nodes from JPA
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the importer
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     *  @param options - the importoptions for the parser...
     */
    public JPAImporter(ImportOptions options) {
        super(options);
    }
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     read Basenode with sysUID and all childnodes from JPA
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue - the basenode with sysUID
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     *  @param sysUID - the sysUID for the basenode to read from JPA
     *  @return - the basenode with sysUID
     */
    public DataDomain getBaseNodeBySysUID(String sysUID) {
        // look for this Basenode in DB
        BaseNode dbNode = BaseNode.findBaseNode(sysUID);
        
        if (dbNode != null) {
            dbNode.initChildNodesFromDB(-1);
        }
        
        return dbNode;
    }
}
