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
package de.yaio.app.extension.datatransfer.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * create email-accounts
 */
public class EmailAccountFactory {
    /**
     * create accounts from properties of form: key=protocol,host,port,username,password,importSysUID
     * @param props     props to convert
     * @return          list of parsed accounts
     */
    public static List<EmailAccount> createFromProperties(Properties props) {
        List<EmailAccount> accounts = new ArrayList<EmailAccount>();
        for (String key: props.stringPropertyNames()) {
            String [] values = props.getProperty(key).split(",");
            accounts.add(new EmailAccount(key, values[0], values[1], values[2], values[3], values[4], values[5]));
        }

        return accounts;
    }
}
