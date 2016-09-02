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
package de.yaio.app.datatransfer.jpa;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.importer.ImporterImpl;
import org.springframework.beans.factory.annotation.Autowired;

/** 
 * import of Nodes from JPA
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class JPAImporter extends ImporterImpl {
    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    /** 
     * create object to import nodes from JPA
     *  @param options                the importoptions for the parser...
     */
    public JPAImporter(final ImportOptions options) {
        super(options);
    }
    

    /** 
     * read Basenode with sysUID and all childnodes from JPA
     *  @param sysUID                 the sysUID for the basenode to read from JPA
     *  @return                       the basenode with sysUID
     */
    public DataDomain getBaseNodeBySysUID(final String sysUID) {
        // look for this Basenode in DB
        BaseNode dbNode = baseNodeDBService.findBaseNode(sysUID);
        
        if (dbNode != null) {
            dbNode.initChildNodesFromDB(-1);
        }
        
        return dbNode;
    }
}
