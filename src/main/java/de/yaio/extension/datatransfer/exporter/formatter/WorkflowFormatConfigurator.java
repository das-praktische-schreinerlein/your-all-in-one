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
package de.yaio.extension.datatransfer.exporter.formatter;

import de.yaio.core.nodeservice.*;

import java.util.HashMap;
import java.util.Map;

/** 
 * configurator for formatting workflow nodes
 * 
 * @FeatureDomain                export
 * @package                      de.yaio.extension.datatransfer.exporter.formatter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class WorkflowFormatConfigurator {

    protected static WorkflowFormatConfigurator me = new WorkflowFormatConfigurator();
    
    protected static final Map<String, String> CONST_STATI_COLOR = new HashMap<String, String>();
    static {
        CONST_STATI_COLOR.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, "#fffafa");

        CONST_STATI_COLOR.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_PLANED, "#ADFF2F");
        CONST_STATI_COLOR.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_LATE, "#FFD700");
        CONST_STATI_COLOR.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING, "#ADFF2F");
        CONST_STATI_COLOR.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_SHORT, "#FF6347");
        CONST_STATI_COLOR.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_DONE, "#E0F2FC");
        CONST_STATI_COLOR.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED, "#F5F5F5");
        CONST_STATI_COLOR.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, "#FFFFFF");

        CONST_STATI_COLOR.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_INFO, "#AFEEEE");
        CONST_STATI_COLOR.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_IDEE, "#AFEEEE");
        CONST_STATI_COLOR.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_DOKU, "#AFEEEE");
        CONST_STATI_COLOR.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_HOWTO, "#AFEEEE");
        CONST_STATI_COLOR.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, "#fffafa");

        CONST_STATI_COLOR.put(SymLinkNodeService.CONST_NODETYPE_IDENTIFIER_SYMLINK, "#AFEEEE");

        CONST_STATI_COLOR.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_OPEN, "#ADFF2F");
        CONST_STATI_COLOR.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_LATE, "#FFD700");
        CONST_STATI_COLOR.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_RUNNNING, "#ADFF2F");
        CONST_STATI_COLOR.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_SHORT, "#FF6347");
        CONST_STATI_COLOR.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_DONE, "#E0F2FC");
        CONST_STATI_COLOR.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_CANCELED, "#F5F5F5");
        CONST_STATI_COLOR.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, "#FFFFFF");

        CONST_STATI_COLOR.put(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_URLRES, "#AFEEEE");
        CONST_STATI_COLOR.put(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_FILERES, "#AFEEEE");
        CONST_STATI_COLOR.put(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILRES, "#AFEEEE");
        }

    protected static final Map<String, String> CONST_STATI_ICON = new HashMap<String, String>();
    static {
        CONST_STATI_ICON.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, null);

        CONST_STATI_ICON.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_PLANED, 
                        "<icon BUILTIN=\"bell\"/>");
        CONST_STATI_ICON.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_LATE, 
                        "<icon BUILTIN=\"messagebox_warning\"/><icon BUILTIN=\"bell\"/>");
        CONST_STATI_ICON.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING, 
                        "<icon BUILTIN=\"bell\"/>");
        CONST_STATI_ICON.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_SHORT, 
                        "<icon BUILTIN=\"messagebox_warning\"/><icon BUILTIN=\"bell\"/>");
        CONST_STATI_ICON.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_DONE, "");
        CONST_STATI_ICON.put(EventNodeService.CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED, "");
        CONST_STATI_ICON.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, null);

        CONST_STATI_ICON.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_INFO, null);
        CONST_STATI_ICON.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_IDEE, null);
        CONST_STATI_ICON.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_DOKU, null);
        CONST_STATI_ICON.put(InfoNodeService.CONST_NODETYPE_IDENTIFIER_HOWTO, null);
        CONST_STATI_ICON.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, null);

        CONST_STATI_ICON.put(SymLinkNodeService.CONST_NODETYPE_IDENTIFIER_SYMLINK, null);

        CONST_STATI_ICON.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_OPEN, null);
        CONST_STATI_ICON.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_LATE, "<icon BUILTIN=\"messagebox_warning\"/>");
        CONST_STATI_ICON.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_RUNNNING, null);
        CONST_STATI_ICON.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_SHORT, "<icon BUILTIN=\"messagebox_warning\"/>");
        CONST_STATI_ICON.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_DONE, null);
        CONST_STATI_ICON.put(TaskNodeService.CONST_NODETYPE_IDENTIFIER_CANCELED, null);
        CONST_STATI_ICON.put(BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN, null);

        CONST_STATI_ICON.put(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_URLRES, null);
        CONST_STATI_ICON.put(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_FILERES, null);
        CONST_STATI_ICON.put(UrlResNodeService.CONST_NODETYPE_IDENTIFIER_EMAILRES, null);
    }
    
    /** 
     * return the global WorkflowFormatConfigurator
     * @return                       the global WorkflowFormatConfigurator
     */
    public static WorkflowFormatConfigurator getWorkflowFormatConfigurator() {
        return me;
    }

    /** 
     * return the Mapping of color for which state
     * @return                       the StateToColorMapping
     */
    public Map<String, String> getConfigStateColor() {
        return CONST_STATI_COLOR;
    }

    /** 
     * return the Mapping of icon for which state
     * @return                       the StateToIconMapping
     */
    public Map<String, String> getConfigStateIcon() {
        return CONST_STATI_ICON;
    }

    /** 
     * return the mapped color for the state
     * @param state                  the state to get the mapped color
     * @return                       the mapped color
     */
    public String getStateColor(final String state) {
        String color = (String) this.getConfigStateColor().get(state);
        return color;
    }

    /** 
     * return the mapped icon for the state
     * @param state                  the state to get the mapped icon
     * @return                       the mapped icon
     */
    public String getStateIcon(final String state) {
        String icon = (String) this.getConfigStateIcon().get(state);
        return icon;
    }

}
