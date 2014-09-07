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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.extension.datatransfer.wiki.JobNodes2Wiki;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in PPL-Format and output to JPA-Provider
 * 
 * @package de.yaio.extension.datatransfer.jpa
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2JPA extends JobNodes2Wiki {
    
    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(JobNodes2JPA.class);

    public JobNodes2JPA(String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        exporter = new JPAExporter();
    }
    
    @Override
    protected void initTransaction() throws Exception {
    }

    @Override
    protected void commitTransaction() throws Exception {
    }

    
    
    @Override
    public DataDomain createMasternode(String name) throws Throwable {
        DataDomain masterNode = null;

        // check for sysUID
        String sysUID = this.cmdLine.getOptionValue("addnodestosysuid");
        if (sysUID != null || ! "".equalsIgnoreCase(sysUID)) {
            // if is set: read masternode from JPA
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("read Masternode from JPA:" + sysUID);
            }
            masterNode = (DataDomain)BaseNode.findBaseNode(sysUID);
            if (masterNode == null) {
                throw new IllegalArgumentException("Masternode to add the new node with sysUID:" + sysUID + " not found!");
            }
        } else {
            // create masternOde with name
            masterNode = super.createMasternode(name);
        }
        return masterNode;
    }
    
    @Override
    protected Options addAvailiableOutputCmdLineOptions(Options availiableCmdLineOptions) throws Throwable {
        // sysuid for export
        Option addnodestosysuidOption = new Option("", "addnodestosysuid", true,
                "SysUID of Masternode to add the new nodes");
        addnodestosysuidOption.setRequired(false);
        availiableCmdLineOptions.addOption(addnodestosysuidOption);

        return availiableCmdLineOptions;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        JobNodes2JPA me = new JobNodes2JPA(args);
        me.startJobProcessing();
    }
}
