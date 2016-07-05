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
package de.yaio.app.utils.io;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * utils for IO
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class IOUtils {
    // must be instantiated after LOGGER because it is used in constructor
    protected static final IOUtils instance = new IOUtils();

    private static final Logger LOGGER = Logger.getLogger(IOUtils.class);

    private IOUtils() {
    }
    
    /**
     * return the current static IOUtils-instance
     * @return                       the current IOUtils-instance
     */
    public static IOUtils getInstance() {
        return instance;
    }

    /**
     * open inoutStrwam for the given filepath (first by filesystem, if failed by classpath)
     * @param filePath               path to the file (filesystem or classresource)
     * @return                       inputStream
     */
    public InputStream inputStreamForFile(final String filePath) throws IOExceptionWithCause {
        try {
            // try filesystem
            return new FileInputStream(new File(filePath));
        } catch (IOException ex) {
            // try classpath
            try {
                return getInstance().getClass().getResourceAsStream(filePath);
            } catch (RuntimeException ex2) {
                throw new IOExceptionWithCause("cant open file", filePath,
                        new IOException("cant open file: " + filePath
                                + " Exception1:" + ex.getMessage()
                                + " Exception2:" + ex2.getMessage()));
            }
        }
    }

    /**
     * read the properties from the given filepath (first by filesystem, if failed by classpath)
     * @param filePath               path to the file (filesystem or classresource)
     * @return                       the properties read from propertyfile
     */
    public Properties readProperties(final String filePath) throws IOExceptionWithCause {
        Properties prop = new Properties();
        try {
            InputStream in = inputStreamForFile(filePath);
            prop.load(in);
            in.close();
        } catch (IOException ex) {
            throw new IOExceptionWithCause("cant read propertiesfile", filePath, ex);
        }
        return prop;
    }
}
