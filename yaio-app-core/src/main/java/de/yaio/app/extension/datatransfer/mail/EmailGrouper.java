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

import de.yaio.app.core.datadomain.ResIndexData;
import de.yaio.app.core.dbservice.BaseNodeFilterFactory;
import de.yaio.app.core.dbservice.BaseNodeRepository;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.core.nodeservice.UrlResNodeService;
import de.yaio.app.utils.db.DBFilter;
import de.yaio.app.utils.db.DBFilterFactory;
import de.yaio.app.utils.db.DBFilterUtils;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.persistence.TypedQuery;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * group emails by ref-sysUID
 */
@Service
public class EmailGrouper {
    @Autowired
    protected BaseNodeRepository baseNodeDBService;

    protected static final String CONST_PATTERN_EMAIL_REF_DEF =
            "\\[YAIO:([A-Za-z0-9,]+?)\\]";
    protected static final Pattern CONST_PATTERN_EMAIL_REF =
            Pattern.compile("(.*)" + CONST_PATTERN_EMAIL_REF_DEF + "(.*)", Pattern.UNICODE_CHARACTER_CLASS);
    protected static final Pattern CONST_PATTERN_REF =
            Pattern.compile("([A-Za-z]+)([0-9]+)", Pattern.UNICODE_CHARACTER_CLASS);

    // Logger
    private static final Logger LOGGER = Logger.getLogger(EmailGrouper.class);

    /**
     * parse Emails for refs and return a map with validated parentSysUID and emails to add
     * @param defaultParentSysUID    defaultParent to add the emails as UrlResNode if not ref in mail
     * @param messages               emails to import
     * @param refBlackListPattern    refs-blacklist as space-separated metaName-pattern
     * @param refWhiteListPattern    refs-whitelist as space-separated metaName-pattern
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     * @return                       emails group by parentSysUID
     */
    public Map<String, Set<Message>> groupEmailsByRef(final String defaultParentSysUID,
                                                       final List<Message> messages,
                                                       final String refBlackListPattern,
                                                       final String refWhiteListPattern) throws IOExceptionWithCause {
        Map<String, List<Message>> emailsGroupByRef = parseEmailsForRef(messages);
        return validateEmailsGroupedByRef(defaultParentSysUID, emailsGroupByRef,
                refBlackListPattern, refWhiteListPattern);
    }

    /**
     * parse Emails for refs and return a map with ref ('' if nothing set on mail) and emails for this ref
     * @param messages               emails to import
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     * @return                       emails grouped by ref
     */
    public Map<String, List<Message>> parseEmailsForRef(List<Message> messages) throws IOExceptionWithCause {
        Map<String, List<Message>> emailsGroupByRef = new HashMap<>();
        for (Message msg : messages) {
            for (String refId : parseEmailForRefs(msg)) {
                if (!emailsGroupByRef.containsKey(refId)) {
                    emailsGroupByRef.put(refId, new ArrayList<Message>());
                }
                emailsGroupByRef.get(refId).add(msg);
            }
        }
        return emailsGroupByRef;
    }

    /**
     * validate emails grouped by ref that ref exists in db
     * @param defaultParentSysUID    defaultParent to add the emails as UrlResNode if not ref in mail
     * @param emailsGroupByRef       emails grouped by ref
     * @param refBlackListPattern    refs-blacklist as space-separated metaName-pattern
     * @param refWhiteListPattern    refs-whitelist as space-separated metaName-pattern
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     * @return                       validated emails grouped by sysUID to add the mail
     */
    public Map<String, Set<Message>> validateEmailsGroupedByRef(final String defaultParentSysUID,
                                                                 final Map<String, List<Message>> emailsGroupByRef,
                                                                 final String refBlackListPattern,
                                                                 final String refWhiteListPattern) throws IOExceptionWithCause {
        Map<String, Set<Message>> validatedEmailsGroupByRef = new HashMap<>();
        String newSysUID;
        String blackListPattern = refBlackListPattern.replace("*", "%");
        String whiteListPattern = refWhiteListPattern.replace("*", "%");

        for (String refId : emailsGroupByRef.keySet()) {
            if (StringUtils.isEmpty(refId)) {
                newSysUID = defaultParentSysUID;
            } else {
                // extract refId
                String prefix = "DONOTFIND";
                String suffix = "DONOTFIND";
                Pattern pattern = CONST_PATTERN_REF;
                Matcher matcher = pattern.matcher(refId);
                int matcherindex = 0;
                if (matcher.matches()) {
                    matcherindex = 1;
                    if (matcher.group(matcherindex) != null) {
                        prefix = matcher.group(matcherindex);
                    }
                    matcherindex = 2;
                    if (matcher.group(matcherindex) != null) {
                        suffix = matcher.group(matcherindex);
                    }
                }

                // check for email
                List<DBFilter> dbFilters = new ArrayList<>();
                dbFilters.addAll(DBFilterFactory.createMapStringFilter("meta_node_praefix",
                        Collections.singleton(prefix)));
                dbFilters.addAll(DBFilterFactory.createMapStringLikeFilter("meta_node_praefix",
                        Collections.singleton(whiteListPattern), false));
                dbFilters.addAll(BaseNodeFilterFactory.createNotNodePraefixFilter(blackListPattern));
                dbFilters.addAll(DBFilterFactory.createMapStringFilter("meta_node_nummer",
                        Collections.singleton(suffix)));

                TypedQuery<BaseNode> query =  baseNodeDBService.createTypedQuery(
                        BaseNode.class, dbFilters, "order by ebene");
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("search node for: '" + refId + "' " +
                            "prefix:" + prefix + " suffix:" + suffix + " " +
                            "with " + DBFilterUtils.generateFilterSql(dbFilters));
                }

                List<BaseNode> nodes = query.getResultList();
                if (nodes.size() == 1) {
                    // unique
                    newSysUID = nodes.get(0).getSysUID();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("emailref: '" + refId + "' found." + " use sysUID:'" + newSysUID + "'");
                    }
                } else if (nodes.size() <= 0) {
                    // not found
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("emailref: '" + refId + "' not found." +
                                " use default sysUID:'" + defaultParentSysUID + "'");
                    }
                    newSysUID = defaultParentSysUID;
                } else {
                    // not unique
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("emailref: '" + refId + "' not unique, found " + nodes.size() +
                                " entries -  use default sysUID:'" + defaultParentSysUID + "'");
                    }
                    newSysUID = defaultParentSysUID;
                }
            }

            if (!validatedEmailsGroupByRef.containsKey(newSysUID)) {
                validatedEmailsGroupByRef.put(newSysUID, new HashSet<Message>());
            }

            validatedEmailsGroupByRef.get(newSysUID).addAll(emailsGroupByRef.get(refId));
        }
        return validatedEmailsGroupByRef;
    }

    /**
     * parse email for refs (if nothing found use '')
     * @param msg                    emails to convert
     * @return                       converted email
     * @throws IOExceptionWithCause  possible IOExceptions or email not parsable
     */
    protected List<String> parseEmailForRefs(final Message msg) throws IOExceptionWithCause {
        List<String> refs = new ArrayList<>();
        try {
            String subject = msg.getSubject();
            if (!StringUtils.isEmpty(subject)) {
                Pattern pattern = CONST_PATTERN_EMAIL_REF;
                Matcher matcher = pattern.matcher(subject);
                int matcherindex = 0;
                if (matcher.matches()) {
                    matcherindex = 2;
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Pattern: " + pattern + " "
                                + matcherindex + ":" + matcher.group(matcherindex));
                    }
                    if (matcher.group(matcherindex) != null) {
                        String refIds = matcher.group(matcherindex);
                        for (String refId : refIds.split(",")) {
                            refs.add(refId);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("parsed email ' " + msg.getSubject() + "'" +
                                        " ref found:" + refId);
                            }
                        }
                        return refs;
                    }
                }
            }

            refs.add("");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("parsed email ' " + msg.getSubject() + "'" +
                        " no ref found - use default:''");
            }
            return refs;

        } catch (MessagingException ex) {
            throw new IOExceptionWithCause("could extract content from multipart", msg, ex);
        }
    }
}