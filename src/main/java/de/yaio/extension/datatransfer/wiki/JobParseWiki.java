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
package de.yaio.extension.datatransfer.wiki;

import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.yaio.core.datadomainservice.NodeNumberService;
import de.yaio.extension.datatransfer.common.CommonImporter;
import de.yaio.extension.datatransfer.ppl.PPLImporter;
import de.yaio.extension.datatransfer.wiki.WikiImporter.WikiStructLine;
import de.yaio.utils.CmdLineJob;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *     job for import of Nodes in Wiki-format and output as PPL
 * 
 * @package de.yaio.extension.datatransfer.wiki
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JobParseWiki extends CmdLineJob {
    
    protected CommonImporter commonImporter;


    public JobParseWiki(String[] args) {
        super(args);
        createCommonImporter();
    }
    
    @Override
    protected Options genAvailiableCmdLineOptions() throws Throwable {
        Options availiableCmdLineOptions = new Options();
        
        // add Options
        commonImporter.addAvailiableCommonCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableWikiCmdLineOptions(availiableCmdLineOptions);
        commonImporter.addAvailiableProductiveImportCmdLineOptions(availiableCmdLineOptions);

        return availiableCmdLineOptions;
    }

    @Override
    public void doJob() throws Throwable {
        // init
        initApplicationContext();
        initCommonImporter();

        // parse PPL-source
        String pplSource = commonImporter.extractDataFromWiki();
        System.out.println(pplSource);
    }

    protected void createCommonImporter() {
        // create commonImporter
        commonImporter = new CommonImporter("ppl");
    }

    protected void initCommonImporter() {
        // init commonImporter
        commonImporter.setCmdLine(cmdLine);
    }

    protected void initApplicationContext() {
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("/META-INF/spring/applicationContext.xml");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JobParseWiki me = new JobParseWiki(args);
        me.startJobProcessing();
    }
}
