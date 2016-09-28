/*
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
package de.yaio.app.extension.autoformatter.formatter;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.formatter.FormatterImpl;
import de.yaio.app.extension.dms.services.ResDocumentService;
import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.services.metaextract.api.model.ExtractedMetaData;
import de.yaio.services.metaextract.api.model.ExtractedMetaDataFactory;
import de.yaio.services.metaextract.api.model.ExtractedMetaDataVersion;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.yaio.app.datatransfer.importer.parser.Parser.CONST_PATTERN_SEG_DESC;

/**
 * format nodes
 */
@Service
public class BaseNodeDescAutoFormatter extends FormatterImpl {
    @Autowired
    protected ResDocumentService resDocumentService;

    protected ExtractedMetaDataFactory extractedMetaDataFactory =
            ExtractedMetaDataFactory.createExtractedMetaDataFactory();

    public static String COMMAND_DO_AUTO_GENERATE_DESC = "DoAutoGenerateDesc";
    public static String COMMAND_DONE_AUTO_GENERATE_DESC = "DoneAutoGenerateDesc";
    public static String PROFILE_NODESC = "NoDesc";
    public static String PROFILE_NOCONTENT = "NoContent";
    public static String PROFILE_SHOWCONTENT = "ShowContent";

    protected static final Pattern CONST_PATTERN_COMMAND =
            Pattern.compile("^<\\!---" + COMMAND_DO_AUTO_GENERATE_DESC + "([A-Za-z0-9, ]+?)--->.*",
                    Pattern.UNICODE_CHARACTER_CLASS + Pattern.DOTALL + Pattern.CASE_INSENSITIVE);

    public static Integer CONTENT_SHOW_MAXLENGHT = 1000;
    public static Integer CONTENT_SHOW_MAXLENGHT_PUFFER = 100;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(BaseNodeDescAutoFormatter.class);

    @Override
    public Class<?> getTargetClass() {
        return BaseNode.class;
    }

    @Override
    public int getTargetOrder() {
        return -1;
    }

    @Override
    public void format(DataDomain node, StringBuffer nodeOutput, OutputOptions options) {
        try {
            genMetadataForNode((BaseNode) node);
        } catch (IOExceptionWithCause ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * auto generates desc for nodes
     * @param nodes                   nodes to format
     * @throws IOExceptionWithCause    possible exceptions
     */
    public void genMetadataForNodes(final Set<BaseNode> nodes) throws IOExceptionWithCause {
        for (BaseNode node : nodes) {
            genMetadataForNode(node);
        }
    }

    /**
     * auto generates desc for node
     * @param node                     node to format
     * @throws IOExceptionWithCause    possible exceptions
     */
    public void genMetadataForNode(final BaseNode node) throws IOExceptionWithCause {
        // get content
        if (node == null) {
            return;
        }

        // extract profiles
        String oldDesc = node.getNodeDesc();
        if (StringUtils.isEmpty(oldDesc)) {
            oldDesc = "";
        }
        Matcher matcher = CONST_PATTERN_COMMAND.matcher(oldDesc);
        if (!matcher.matches()) {
            LOGGER.info("SKIP: " + COMMAND_DO_AUTO_GENERATE_DESC + " not found for " + node.getNameForLogger());
            return;
        }

        // Skip
        List<String> profiles = extractProfiles(node, node.getNodeDesc());
        if (profiles.contains(PROFILE_NODESC.toUpperCase())) {
            node.setNodeDesc("<!---" + COMMAND_DONE_AUTO_GENERATE_DESC + " " + PROFILE_NODESC + "--->");
            LOGGER.info("DONE: " + PROFILE_NODESC + " is set for " + node.getNameForLogger());
            return;
        }
        if (!checkConditions(node, profiles)) {
            LOGGER.info("SKIP: preconditions failed for " + node.getNameForLogger());
            return;
        }


        StringBuilder nodeDesc = new StringBuilder();
        generateAutoGeneratedBlock(node, profiles, nodeDesc);
        generateNameBlock(node, nodeDesc);

        StringBuilder meta = new StringBuilder();
        generateMetaBlock(node, meta);

        StringBuilder additional = new StringBuilder();
        generateAdditionalBlocks(node, additional);

        nodeDesc.append(meta);
        nodeDesc.append(additional);

        String desc = nodeDesc.toString();
        desc = desc.replaceAll("(?!" + CONST_PATTERN_SEG_DESC + ").+", " ");
        node.setNodeDesc(desc);
        LOGGER.info("updated node metadata with headersources:" + node.getNameForLogger());
    }

    protected boolean checkConditions(final BaseNode node, List<String> profiles) {
        return true;
    }


    protected List<String> extractProfiles(final BaseNode node, final String desc) {
        List<String> profiles = new ArrayList<>();
        if (StringUtils.isEmpty(desc)) {
            return profiles;
        }

        Matcher matcher = CONST_PATTERN_COMMAND.matcher(desc);
        int matcherindex = 1;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Profiles: " + CONST_PATTERN_COMMAND + " "
                    + matcherindex + ":" + matcher.group(matcherindex));
        }
        if (matcher.matches() && matcher.group(matcherindex) != null) {
            profiles = Arrays.asList(matcher.group(matcherindex).toUpperCase().replaceAll(" ", "").split(","));
        }

        return profiles;
    }

    protected void generateAutoGeneratedBlock(final BaseNode node, final List<String> profiles,
                                              final StringBuilder autoGenerated)
            throws IOExceptionWithCause {
        autoGenerated.append("<!---" + COMMAND_DONE_AUTO_GENERATE_DESC + " " + StringUtils.join(profiles, ",") + "--->\n");
    }

    protected void generateNameBlock(final BaseNode node, final StringBuilder name)
            throws IOExceptionWithCause {
        name.append("# Node: " + node.getName() + "\n");
    }

    protected void generateMetaBlock(final BaseNode node, final StringBuilder meta)
            throws IOExceptionWithCause {
    }

    protected void generateAdditionalBlocks(final BaseNode node, final StringBuilder additional)
            throws IOExceptionWithCause {
    }

    protected void generateContentBlock(final UrlResNode node, final boolean flgIndexedContent, final StringBuilder content)
            throws IOExceptionWithCause {
        if (flgIndexedContent) {
            try (InputStream is = resDocumentService.downloadResIndexFromDMS(node, 0)) {
                generateContentBlock(node, is, true, content);
            } catch (IOException ex) {
                throw new IOExceptionWithCause("could extract indexedcontent from file", node.getNameForLogger(), ex);
            }
        } else {
            try (InputStream is = resDocumentService.downloadResContentFromDMS(node, 0)) {
                generateContentBlock(node, is, false, content);
            } catch (IOException ex) {
                throw new IOExceptionWithCause("could extract content from file", node.getNameForLogger(), ex);
            }
        }
    }

    protected void generateContentBlock(final UrlResNode node, final InputStream is, final boolean flgIndexedContent,
                                        final StringBuilder content)
            throws IOException {
        if (is != null) {
            String id = node.getSysUID();

            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer);
            String indexedContent = writer.toString();
            if (flgIndexedContent) {
                // parse content from json
                ExtractedMetaData metaData = extractedMetaDataFactory.parseExtractedMetaDataFromJson(indexedContent);
                if (metaData == null || MapUtils.isEmpty(metaData.getVersions())) {
                    return;
                }
                ExtractedMetaDataVersion metaDataVersion =
                        (ExtractedMetaDataVersion)metaData.getVersions().values().toArray()[0];
                indexedContent = metaDataVersion.getContent();
            }

            if (StringUtils.isEmpty(indexedContent)) {
                return;
            }

            content.append("\n<!---BOX--->\n" +
                    "## Text <!---TOGGLER content" + id + ",text--->\n" +
                    "<!---CONTAINER content" + id + "--->\n\n" +
                    "```nocode\n");

            if (indexedContent.length() >  CONTENT_SHOW_MAXLENGHT + CONTENT_SHOW_MAXLENGHT_PUFFER) {
                indexedContent = indexedContent.substring(0, CONTENT_SHOW_MAXLENGHT);
                indexedContent = indexedContent.replaceAll("```", "'''");
                content.append(indexedContent);
                content.append("\n```\n");
                String url = (flgIndexedContent ? "yaiodmsidxembed" : "yaiodmsembed");
                content.append("\nCutted by " + CONTENT_SHOW_MAXLENGHT + " Chars [[see " + "...](" + url + ":" + node.getSysUID() + ")]\n\n");
            } else {
                indexedContent = indexedContent.replaceAll("```", "'''");
                content.append(indexedContent);
                content.append("\n```\n");
            }
            content.append(
                    "<!---/CONTAINER content" + id + "--->\n" +
                            "<!---/BOX--->\n");
        }
    }

    protected BaseNode getChildNodeByType(final  BaseNode node, final String type) {
        for (BaseNode subNode : node.getChildNodes()) {
            if (type.equals(subNode.getType())) {
                return subNode;
            }
        }

        return null;
    }
}