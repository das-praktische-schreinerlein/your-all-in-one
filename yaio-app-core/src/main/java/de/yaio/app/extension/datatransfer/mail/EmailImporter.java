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

import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * receive and import emails for accounts
 *
 */
@Service
public class EmailImporter {
    @Autowired
    private EmailReceiver emailReceiverService;
    @Autowired
    private EmailGrouper emailGroupService;
    @Autowired
    private EmailConverter emailConverterService;
    @Autowired
    protected EmailFormatter emailFormatterService;
    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(EmailImporter.class);

    /**
     * receive, import email for this accounts and append them to the configured inbox
     * (IOExceptionWithCause will be catched per account)
     * @param accounts        accounts to import the emails from
     * @param checkForRefs    check for refs and append email to ref instead of inbox (inbox is fallback)
     */
    public void importEmails(final List<EmailAccount> accounts, final boolean checkForRefs) {
        for (EmailAccount account : accounts) {
            try {
                this.importEmails(account, checkForRefs);
            } catch (IOExceptionWithCause ex) {
                LOGGER.info("error while loading emails for account:" + account, ex);
            }
        }
    }

    /**
     * receive, import email for this account and append them to the configured inbox
     * @param account                account to import the emails from
     * @param checkForRefs           check for refs and append email to ref instead of inbox (inbox is fallback)
     * @return                       count of imported messages
     * @throws IOExceptionWithCause  if account is blocked or so on
     */
    public int importEmails(final EmailAccount account, final boolean checkForRefs) throws IOExceptionWithCause {
        List<Message> messages = emailReceiverService.downloadEmails(account);
        this.importEmails(account.getInboxSysUID(), messages, checkForRefs,
                account.getRefBlackListPattern(), account.getRefWhiteListPattern());
        LOGGER.info("loaded " + messages.size() + " emails for account:" + account);
        return messages.size();
    }

    /**
     * import email from file and append them to parsed refs (if checkForRefs) or if no ref found to defaultParentSysUID
     * @param defaultParentSysUID     sysUID of the basenode to which emails will be appended if no ref found
     * @param files                  files to import as emails
     * @param checkForRefs           check for refs and append email to ref instead of inbox (inbox is fallback)
     * @param refBlackListPattern    refs-blacklist as space-separated metaName-pattern
     * @param refWhiteListPattern    refs-whitelist as space-separated metaName-pattern
     * @return                       count of imported messages
     * @throws IOExceptionWithCause  if reading of emails went wrong
     */
    public int importMailFiles(final String defaultParentSysUID, final List<File> files, final boolean checkForRefs,
                               final String refBlackListPattern,
                               final String refWhiteListPattern)
            throws IOExceptionWithCause {
        List<Message> messages = new ArrayList<>();
        for (File file : files) {
            try {
                // load local file
                FileInputStream fin = new FileInputStream(file);

                // create copy of msg
                messages.add(new MimeMessage(null, fin));

                // close local file
                fin.close();
            } catch (MessagingException ex) {
                throw new IOExceptionWithCause("could convert message from file", file, ex);
            } catch (IOException ex) {
                throw new IOExceptionWithCause("could not load message from local file", file, ex);
            }
        }

        this.importEmails(defaultParentSysUID, messages, checkForRefs, refBlackListPattern, refWhiteListPattern);
        LOGGER.info("loaded " + messages.size() + " emails from files:" + files);
        return messages.size();
    }

    /**
     * import emails and append them to parsed refs (if checkForRefs) or if no ref found to defaultParentSysUID
     * @param defaultParentSysUID     sysUID of the basenode to which emails will be appended if no ref found
     * @param messages                messages to import
     * @param checkForRefs            check for refs and append email to ref instead of inbox (inbox is fallback)
     * @param refBlackListPattern     refs-blacklist as space-separated metaName-pattern
     * @param refWhiteListPattern     refs-whitelist as space-separated metaName-pattern
     * @throws IOExceptionWithCause   if reading of emails went wrong
     */
    public void importEmails(final String defaultParentSysUID, final List<Message> messages, final boolean checkForRefs,
                             final String refBlackListPattern,
                             final String refWhiteListPattern)
            throws IOExceptionWithCause {
        BaseNode parent = baseNodeDBService.findBaseNode(defaultParentSysUID);
        if (parent == null) {
            throw new IOExceptionWithCause("defaultParentSysUID not found", defaultParentSysUID,
                    new NoSuchElementException());
        }

        Map<String, Set<Message>> emailsGroupByRef;
        if (checkForRefs) {
            emailsGroupByRef = emailGroupService.groupEmailsByRef(defaultParentSysUID, messages,
                    refBlackListPattern, refWhiteListPattern);
        } else {
            emailsGroupByRef = new HashMap<>();
            Set<Message> uniqueMesssages = new HashSet<>();
            uniqueMesssages.addAll(messages);
            emailsGroupByRef.put(defaultParentSysUID, uniqueMesssages);
        }

        for (String sysUID : emailsGroupByRef.keySet()) {
            importEmails(sysUID, emailsGroupByRef.get(sysUID));
        }
    }

    /**
     * import emails and append them to sysUID
     * @param parentSysUID            sysUID of the basenode to which emails will be append
     * @param messages                messages to import
     * @throws IOExceptionWithCause   if reading of emails went wrong
     */
    public void importEmails(final String parentSysUID, final Set<Message> messages) throws IOExceptionWithCause {
        BaseNode parent = baseNodeDBService.findBaseNode(parentSysUID);
        if (parent == null) {
            throw new IOExceptionWithCause("parentSysUID not found", parentSysUID, new NoSuchElementException());
        }

        emailConverterService.importEmailsToParent(parent, messages);
        emailFormatterService.genMetadataForEmailNodes(parent.getChildNodes());

        baseNodeDBService.saveChildNodesToDB(parent, NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN, false);
        baseNodeDBService.updateMeAndMyParents(parent);
    }
}

