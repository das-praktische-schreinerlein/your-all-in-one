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
package de.yaio.app.cli.importer;

import de.yaio.app.config.ContextHelper;
import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.jpa.JPAImporter;
import de.yaio.app.utils.config.ConfigurationOption;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/** 
 * class for import of Nodes
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.common
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CommonImporter {
    
    // Logger
    private static final Logger LOGGER = Logger.getLogger(CommonImporter.class);

    protected String defaultSourceType = "jpa";
    
    /** 
     * create importer-object to import nodes
     * @param defaultSourceType      the default sourcetype if commandline-option not set
     */
    public CommonImporter(final String defaultSourceType) {
        this.defaultSourceType = defaultSourceType;
    }

    /*
     * ##############
     * avaliable Commandline-Options
     * ##############
     */
    
    /** 
     * add common import-options to the availiableCmdLineOptions
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiableCommonCmdLineOptions(final Options availiableCmdLineOptions) {
        // sourceType
        Option sourceType = new Option("", "sourcetype", true, "Source to read from (jpa).");
        sourceType.setRequired(false);
        availiableCmdLineOptions.addOption(sourceType);
    
        // Hirarchy-Delimiter  
        Option delimiterOption = new Option("", "delimiter", true, "Hirarchy-Delimiter (default TAB)");
        delimiterOption.setRequired(false);
        availiableCmdLineOptions.addOption(delimiterOption);
    }
    

    /** 
     * add JPA-import-options to the availiableCmdLineOptions
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiableJPACmdLineOptions(final Options availiableCmdLineOptions) {
        // exportsysuid
        Option exportSysUid = new Option("", "exportsysuid", true, "SysUId of the masterNode to export.");
        exportSysUid.setRequired(false);
        availiableCmdLineOptions.addOption(exportSysUid);
    }

    /** 
     * add Production-import-options to the availiableCmdLineOptions
     * @param availiableCmdLineOptions the container with the availiableCmdLineOptions
     */
    public void addAvailiableProductiveImportCmdLineOptions(final Options availiableCmdLineOptions) {
        // Id-File
        Option pathIdDB = new Option("", "pathiddb", true, "Pfad zur ID-Datenbank");
        pathIdDB.setRequired(true);
        availiableCmdLineOptions.addOption(pathIdDB);
    }

    /*
     * ##############
     * import-logic
     * ##############
     */

    /** 
     * import the data from JPA configured by cmdline-options and add 
     * them to the masterNode 
     * @param masterNode             the masternode on which all other nodes are added
     * @throws Exception             parse/io-Exceptions possible
     */
    public void importDataToMasterNodeFromJPA(final DataDomain masterNode) throws Exception {
        // initApplicationContext
        ContextHelper.getInstance().getSpringApplicationContext();

        // check exportsysuid
        String exportSysUID = YaioConfiguration.getInstance().getCliOption("exportsysuid", null).getStringValue();
        if (StringUtils.isEmpty(exportSysUID)) {
            throw new IllegalArgumentException("For sourcetype=jpa a exportsysuid is expected");
        }
        
        // create own importer
        JPAImporter jpaImporter = new JPAImporter(null);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("read from JPA exportsysuid: '" + exportSysUID + "'");
        }

        // read data
        DataDomain jpaNode = jpaImporter.getBaseNodeBySysUID(exportSysUID);
        BaseNode baseNode = (BaseNode) jpaNode;
        if (baseNode == null) {
            throw new IllegalArgumentException("node not found sysUID=" + exportSysUID);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("result from JPA  for sysUID: '" + exportSysUID + "' " + 
                            baseNode.getNameForLogger());
        }
        
        // add to masternode
        baseNode.setParentNode(masterNode);
    }
    
    /** 
     * import the data from source configured by cmdline-options and add 
     * them to the masterNode 
     * @param masterNode             the masternode on which all other nodes are added
     * @throws Exception             parse/io-Exceptions possible
     */
    public void importDataToMasterNode(final DataDomain masterNode) throws Exception {
        // check datasource
        String sourceType = ConfigurationOption.stringValueOf(
                YaioConfiguration.getInstance().getCliOption("sourcetype", defaultSourceType));
        if ("jpa".equalsIgnoreCase(sourceType)) {
            // from jpa
            this.importDataToMasterNodeFromJPA(masterNode);
        } else {
            throw new IllegalArgumentException("if sourcetype is set ist must be jpa,ppl,wiki or excel");
        }
    }
}
