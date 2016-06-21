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
package de.yaio.app.cli;

import de.yaio.app.utils.CmdLineHelper;
import de.yaio.app.utils.CmdLineJob;


/** 
 * baseclass for commandlinejobs<br>
 * must be implemented:<br>
 * <ul>
 * <li>validateCmdLine
 * </ul>
 * to configure:<br>
 * <ul>
 * <li>genAvailiableCmdLineOptions
 * </ul>
 *
 * @FeatureDomain                Tools - CLI-Handling
 * @package                      de.yaio.utils
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @copyright                    Copyright (c) 2013, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public abstract class YaioCmdLineJob extends CmdLineJob {

    /**
     * baseclass for CommandLineJobs
     * @param args                   the command line arguments
     */
    public YaioCmdLineJob(final String[] args) {
        // set args
        super(args);
    }

    protected void cleanUpAfterJob() throws Exception {
        // TODO: hack to close HSLDB-connection -> Hibernate doesn't close the 
        //       database and so the content is not written to file
        org.hsqldb.DatabaseManager.closeDatabases(0);
    }

    protected CmdLineHelper getCmdLineHelper() {
        return this.getYaioCmdLineHelper();
    }

    protected YaioCmdLineHelper getYaioCmdLineHelper() {
        return YaioCmdLineHelper.getInstance();
    }
}
