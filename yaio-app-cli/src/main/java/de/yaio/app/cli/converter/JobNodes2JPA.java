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
package de.yaio.app.cli.converter;

import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.JobConfig;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.jpa.JPAExporter;
import de.yaio.commons.config.ConfigurationOption;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/** 
 * job for import of Nodes in PPL-Format and output to JPA-Provider
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class JobNodes2JPA extends JobNodes2Wiki {

    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    private static final Logger LOGGER =
        Logger.getLogger(JobNodes2JPA.class);

    /** 
     * job to import nodes and output to JPA
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
    protected void configureContext() {
        ContextHelper.getInstance().addSpringConfig(JobConfig.class);
    }

    @Override
    public DataDomain createMasternode(final String name) throws ParserException {
        DataDomain masterNode;

        // initApplicationContext
        ContextHelper.getInstance().getSpringApplicationContext();
        ContextHelper.getInstance().autowireService(this);
        ContextHelper.getInstance().autowireService(exporter);

        // check for sysUID
        String sysUID = ConfigurationOption.stringValueOf(this.getConfiguration().getCliOption("addnodestosysuid"));
        if (!StringUtils.isEmpty(sysUID)) {
            // if is set: read masternode from JPA
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("read Masternode from JPA:" + sysUID);
            }
            masterNode = baseNodeDBService.findBaseNode(sysUID);
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
    protected Options addAvailiableOutputCmdLineOptions(final Options availiableCmdLineOptions) {
        // sysuid for export
        Option addnodestosysuidOption = new Option("", "addnodestosysuid", true,
                "SysUID of Masternode to add the new nodes");
        addnodestosysuidOption.setRequired(false);
        availiableCmdLineOptions.addOption(addnodestosysuidOption);

        return availiableCmdLineOptions;
    }

    /** 
     * get the Class-logger
     * @return                       logger - the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        JobNodes2JPA me = new JobNodes2JPA(args);
        
        // start processing
        me.startJobProcessing();
    }
}
