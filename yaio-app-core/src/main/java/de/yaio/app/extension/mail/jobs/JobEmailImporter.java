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
package de.yaio.app.extension.mail.jobs;

import de.yaio.app.extension.datatransfer.mail.EmailAccount;
import de.yaio.app.extension.datatransfer.mail.EmailAccountFactory;
import de.yaio.app.extension.datatransfer.mail.EmailImporter;
import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/** 
 * job to receive and import emails for configured accounts
 * 
 */
@Component
@ConditionalOnProperty("yaio.email-client.jobs.pull.enable")
public class JobEmailImporter {
    public static final String CONST_FILELOCATION_EMAILACCOUNTS = "yaio.email-client.jobs.pull.accounts.filelocation";

    @Autowired
    protected EmailImporter emailImporterService;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(JobEmailImporter.class);

    private static List<EmailAccount> accounts = new ArrayList<>();

    /**
     * to to import email for the configured accounts and append them to the configured sysUID
     */
    @Scheduled(
               fixedDelayString = "${yaio.email-client.jobs.pull.fixedDelay}",
//               fixedRateString = "${yaio.email-client.jobs.pull.fixedRate}",
               initialDelayString = "${yaio.email-client.jobs.pull.initialDelay}"
//               cron = "${yaio.email-client.jobs.pull.cron}"
               )
    public void doImportEmails() {
        LOGGER.info("start job doImportEmails");
        emailImporterService.importEmails(this.getAccounts(), true);
        LOGGER.info("end job doImportEmails");
    }

    /**
     * accounts to import emails from
     * @return   list of accounts
     */
    public List<EmailAccount> getAccounts() {
        return accounts;
    }

    @PostConstruct
    protected void configureAccounts() {
        Properties accountProps;
        try {
            accountProps = IOUtils.getInstance().readProperties(System.getProperty(CONST_FILELOCATION_EMAILACCOUNTS));
        } catch (IOExceptionWithCause ex) {
            throw new IllegalArgumentException("cant read propertyFile for Accounts", ex);
        }
        this.getAccounts().addAll(EmailAccountFactory.createFromProperties(accountProps));
    }
}
