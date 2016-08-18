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

import de.yaio.app.core.dbservice.BaseNodeDBService;
import de.yaio.app.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import java.util.List;

/** 
 * receive and import emails for accounts
 * 
 */
@Service
public class EmailImporter {
    @Autowired
    private EmailReceiver emailReceiverService;
    @Autowired
    private EmailConverter emailConverterService;
    @Autowired
    protected EmailFormatter emailFormatterService;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(EmailImporter.class);

    /**
     * receive, import email for this accounts and append them to the configured sysUID (IOExceptionWithCause will be catched per account)
     * @param accounts        accounts to import the emails from
     */
    public void importEmails(List<EmailAccount> accounts) {
        for (EmailAccount account : accounts) {
            try {
                this.importEmails(account);
            } catch (IOExceptionWithCause ex) {
                LOGGER.info("error while loading emails for account:" + account, ex);
            }
        }
    }

    /**
     /**
     * receive, import email for this account and append them to the configured sysUID
     * @param account                account to import the emails from
     * @throws IOExceptionWithCause  if account is blocked or so on
     */
    public void importEmails(EmailAccount account) throws IOExceptionWithCause {
        BaseNodeDBService nodeService = BaseNodeDBServiceImpl.getInstance();
        BaseNode parent = BaseNode.findBaseNode(account.getInboxSysUID());

        List<Message> messages = emailReceiverService.downloadEmails(account);
        emailConverterService.importEmailsToParent(parent, messages);
        emailFormatterService.genMetadataForEmailNodes(parent.getChildNodes());

        nodeService.saveChildNodesToDB(parent, NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN, false);
        nodeService.updateMeAndMyParents(parent);
        LOGGER.info("loaded " + messages.size() + " emails for account:" + account);
    }
}

