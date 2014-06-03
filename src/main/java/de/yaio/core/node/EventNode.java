/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.core.node;
import java.util.HashMap;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import de.yaio.core.datadomainservice.MetaDataService;
import de.yaio.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.core.datadomainservice.SysDataService;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.nodeservice.EventNodeService;
import de.yaio.core.nodeservice.NodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean with EventNode-data (calendar-events) and matching businesslogic
 * 
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class EventNode extends TaskNode {
    
    // Daten
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_PLANED = "EVENT_PLANED";
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_CONFIRMED = "EVENT_CONFIRMED";
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_LATE = "EVENT_LATE";
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_SHORT = "EVENT_SHORT";
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING = "EVENT_RUNNING";
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_DONE = "EVENT_ERLEDIGT";
    public static final String CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED = "EVENT_VERWORFEN";
    
    protected static SysDataService sysDataService = new SysDataServiceImpl();
    protected static MetaDataService metaDataService = new MetaDataServiceImpl();
    protected static NodeService nodeDataService = new EventNodeService();

    public NodeService getNodeService() {
        return nodeDataService;
    }
    public static void setNodeDataService(NodeService newNodeDataService) {
        nodeDataService = newNodeDataService;
    }

    // Status-Konstanten
    public static Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_PLANED, CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING, CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_LATE, CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_SHORT, CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_DONE, CONST_NODETYPE_IDENTIFIER_EVENT_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED, CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_PLANED", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_GREPLANT", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_OPEN", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_OFFEN", CONST_NODETYPE_IDENTIFIER_EVENT_PLANED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_RUNNING", CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_LAUFEND", CONST_NODETYPE_IDENTIFIER_EVENT_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_LATE", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_OVERDUE", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_VERSPÄTET", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_VERSPAETET", CONST_NODETYPE_IDENTIFIER_EVENT_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_SHORT", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_ÜBERFÄLLIG", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_UEBERFAELLIG", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_WARNING", CONST_NODETYPE_IDENTIFIER_EVENT_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_DONE", CONST_NODETYPE_IDENTIFIER_EVENT_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_ERLEDIGT", CONST_NODETYPE_IDENTIFIER_EVENT_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_CANCELED", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_VERWORFEN", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_GELOESCHT", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("EVENT_ABGEBROCHEN", CONST_NODETYPE_IDENTIFIER_EVENT_CANCELED);
    }

    @Override
    public Map<String, Object> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
}
