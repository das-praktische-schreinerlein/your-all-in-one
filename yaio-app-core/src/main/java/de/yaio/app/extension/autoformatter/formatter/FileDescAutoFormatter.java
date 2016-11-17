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
package de.yaio.app.extension.autoformatter.formatter;

import de.yaio.app.core.datadomain.ResIndexData;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.node.UrlResNode;
import de.yaio.app.extension.dms.services.ResDocumentService;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * format files
 */
@Service
public class FileDescAutoFormatter extends BaseNodeDescAutoFormatter {
    @Override
    protected boolean checkConditions(final BaseNode node, List<String> profiles) {
        // Skip
        if (!profiles.contains(PROFILE_NODESC.toUpperCase()) &&
                profiles.contains(PROFILE_SHOWCONTENT.toUpperCase()) &&
                UrlResNode.class.isInstance(node)) {
            UrlResNode contentNode = (UrlResNode)node;
            if (contentNode.getResIndexDMSState() != ResIndexData.IndexWorkflowState.INDEX_DONE &&
                    contentNode.getResIndexDMSState() != ResIndexData.IndexWorkflowState.NOINDEX) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void generateNameBlock(final BaseNode node, final StringBuilder name)
            throws IOExceptionWithCause{
        UrlResNode contentNode = (UrlResNode) node;
        name.append("# File: " + contentNode.getResLocRef() + "\n");
    }

    @Override
    protected void generateMetaBlock(final BaseNode node, final StringBuilder meta)
            throws IOExceptionWithCause{
        UrlResNode contentNode = (UrlResNode) node;
        String id = node.getSysUID();
        meta.append("\n<!---BOX--->\n" +
                "## Meta <!---TOGGLER meta" + id + ",text--->\n" +
                "<!---CONTAINER meta" + id + "--->\n\n");
        meta.append("- filename: " + contentNode.getResLocRef() + "\n");
        meta.append("<!---/CONTAINER meta" + id + "--->\n" +
                "<!---/BOX--->\n");
    }

    @Override
    protected void generateAdditionalBlocks(final BaseNode node, final StringBuilder additional)
            throws IOExceptionWithCause{
        UrlResNode contentNode = (UrlResNode) node;
        String id = node.getSysUID();
        List<String> profiles = extractProfiles(node, node.getNodeDesc());

        // parts
        StringBuilder parts = new StringBuilder();
        StringBuilder content = new StringBuilder();
        parts.append("\n<!---BOX--->\n" +
                "## Attachments <!---TOGGLER attachments" + id + ",text--->\n" +
                "<!---CONTAINER attachments" + id + "--->\n");
        parts.append("- [Content](yaiodmsembed:" + contentNode.getSysUID() + ")\n");
        if (contentNode.getResIndexDMSState() == ResIndexData.IndexWorkflowState.INDEX_DONE) {
            parts.append("- [Extract](yaiodmsidxembed:" + contentNode.getSysUID() + ")\n");
        }
        parts.append("<!---/CONTAINER attachments" + id + "--->\n" +
                "<!---/BOX--->\n");

        // content
        if (!profiles.contains(PROFILE_NODESC.toUpperCase()) &&
                profiles.contains(PROFILE_SHOWCONTENT.toUpperCase())) {
            generateContentBlock(contentNode, true, content);
        }

        additional.append(parts.toString());
        additional.append(content.toString());
    }
}