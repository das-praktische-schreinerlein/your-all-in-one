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
package de.yaio.extension.datatransfer.jpa;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImporterImpl;

/** 
 * import of Nodes from JPA
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.extension.datatransfer.jpa
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JPAImporter extends ImporterImpl {

    /** 
     * create object to import nodes from JPA
     * @FeatureDomain                Constructor
     * @FeatureResult                initialize the importer
     * @FeatureKeywords              Constructor
     *  @param options                the importoptions for the parser...
     */
    public JPAImporter(final ImportOptions options) {
        super(options);
    }
    

    /** 
     * read Basenode with sysUID and all childnodes from JPA
     * @FeatureDomain                Persistence
     * @FeatureResult                returnValue - the basenode with sysUID
     * @FeatureKeywords              Constructor
     *  @param sysUID                 the sysUID for the basenode to read from JPA
     *  @return                       the basenode with sysUID
     */
    public DataDomain getBaseNodeBySysUID(final String sysUID) {
        // look for this Basenode in DB
        BaseNode dbNode = BaseNode.findBaseNode(sysUID);
        
        if (dbNode != null) {
            dbNode.initChildNodesFromDB(-1);
        }
        
        return dbNode;
    }
}
