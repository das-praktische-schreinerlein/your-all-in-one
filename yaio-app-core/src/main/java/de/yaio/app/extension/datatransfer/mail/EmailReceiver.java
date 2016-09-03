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

import de.yaio.commons.io.IOExceptionWithCause;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * receive emails for account
 *
 */
@Service
public class EmailReceiver {

    /**
     * Downloads new messages and fetches details for each message.
     * @param account                account to fetch emails from
     * @return                       List of messages fetched
     * @throws IOExceptionWithCause  possible problem to connect or fetch emails
     */
    public List<Message> downloadEmails(final EmailAccount account) throws IOExceptionWithCause {
        Properties properties = getServerProperties(account.getProtocol(), account.getHost(), account.getPort());
        Session session = Session.getDefaultInstance(properties);
        List<Message> messages = new ArrayList<Message>();

        try {
            // connects to the message store
            Store store = session.getStore(account.getProtocol());
            store.connect(account.getUserName(), account.getPassword());

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] lstMessages = folderInbox.getMessages();
            for (Message origMsg : lstMessages) {
                // copy msg to localfile
                File tempFile = File.createTempFile("myfile", ".eml");
                FileOutputStream fout = new FileOutputStream(tempFile);
                origMsg.writeTo(fout);
                fout.close();

                // load local file
                FileInputStream fin = new FileInputStream(tempFile);

                // create copy of msg
                messages.add(new MimeMessage(null, fin));

                // close local file
                fin.close();
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            throw new IOExceptionWithCause("error fetching mails from server",
                    properties.toString() + " username: " + account.getUserName(), ex);
        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could not connect to the message store",
                    properties.toString() + " username: " + account.getUserName(), ex);
        } catch (IOException ex) {
            throw new IOExceptionWithCause("could not load message into/from local file",
                    properties.toString() + " username: " + account.getUserName(), ex);
        }
        return messages;
    }

    /**
     * Returns a Properties object which is configured for a POP3/IMAP server
     *
     * @param protocol              either "imap" or "pop3"
     * @param host                  host to connect
     * @param port                  port
     * @return                      a Properties object
     */
    private Properties getServerProperties(final String protocol, final String host, final String port) {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

        return properties;
    }
}