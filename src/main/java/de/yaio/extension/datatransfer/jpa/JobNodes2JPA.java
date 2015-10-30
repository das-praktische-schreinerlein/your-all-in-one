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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import de.yaio.app.Configurator;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.extension.datatransfer.wiki.JobNodes2Wiki;

/** 
 * job for import of Nodes in PPL-Format and output to JPA-Provider
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.jpa
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobNodes2JPA extends JobNodes2Wiki {
    
    private static final Logger LOGGER =
        Logger.getLogger(JobNodes2JPA.class);

    /** 
     * job to import nodes and output to JPA
     * @FeatureDomain                Constructor
     * @FeatureResult                initialize the application
     * @FeatureKeywords              Constructor
     * @param args                   the command line arguments
     */
    public JobNodes2JPA(final String[] args) {
        super(args);
    }

    @Override
    public void createExporter() {
        exporter = new JPAExporter();
    }
    
    @Override
    public DataDomain createMasternode(final String name) throws Exception {
        DataDomain masterNode = null;

        // initApplicationContext
        Configurator.getInstance().getSpringApplicationContext();

        // check for sysUID
        String sysUID = Configurator.getInstance().getCommandLine().getOptionValue("addnodestosysuid");
        if (sysUID != null || !"".equalsIgnoreCase(sysUID)) {
            // if is set: read masternode from JPA
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("read Masternode from JPA:" + sysUID);
            }
            masterNode = BaseNode.findBaseNode(sysUID);
            if (masterNode == null) {
                throw new IllegalArgumentException("Masternode to add the new node with "
                                + "sysUID:" + sysUID + " not found!");
            }
            
            // create dummy masternode
            masterNode = super.createMasternode(name);
            ((BaseNode) masterNode).setSysUID(sysUID);
        } else {
            // create masterNode with name
            masterNode = super.createMasternode(name);
        }
        return masterNode;
    }
    
    @Override
    protected Options addAvailiableOutputCmdLineOptions(final Options availiableCmdLineOptions) throws Exception {
        // sysuid for export
        Option addnodestosysuidOption = new Option("", "addnodestosysuid", true,
                "SysUID of Masternode to add the new nodes");
        addnodestosysuidOption.setRequired(false);
        availiableCmdLineOptions.addOption(addnodestosysuidOption);

        return availiableCmdLineOptions;
    }

    /** 
     * get the Class-logger
     * @FeatureDomain                Logging
     * @FeatureResult                returnValue Logger - use it !!!!
     * @FeatureKeywords              Logging
     * @return                       logger - the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /** 
     * Main-method to start the application
     * @FeatureDomain                CLI
     * @FeatureResult                initialize the application
     * @FeatureKeywords              CLI
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2JPA me = new JobNodes2JPA(args);
        
        // start processing
        me.startJobProcessing();
    }
}
