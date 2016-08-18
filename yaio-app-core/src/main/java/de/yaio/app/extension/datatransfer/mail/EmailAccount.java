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

/**
 * configuration of an email-account
 */
public class EmailAccount {
    private String key;
    private String protocol;
    private String host;
    private String port;
    private String userName;
    private String password;
    private String inboxSysUID;

    public EmailAccount(String key, String protocol, String host, String port, String userName, String password, String inboxSysUID) {
        this.key = key;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.inboxSysUID = inboxSysUID;
    }

    public String getKey() {
        return key;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getInboxSysUID() {
        return inboxSysUID;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("EmailAccount{")
           .append(" key:").append(getKey())
           .append(" protocol:").append(getProtocol())
           .append(" host:").append(getHost())
           .append(" port:").append(getPort())
           .append(" userName:").append(getUserName())
           .append(" inboxSysUID:").append(getInboxSysUID())
           .append("}").toString();
    }
}
