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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import de.yaio.core.datadomain.ExtendedWorkflowData;
import de.yaio.core.datadomainservice.MetaDataService;
import de.yaio.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.core.datadomainservice.SysDataService;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.core.nodeservice.TaskNodeService;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean for (projects, tasks, todos) with TaskNode-data like (Plan, is) 
 *     and matching businesslogic
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
public class TaskNode extends BaseNode implements ExtendedWorkflowData {
    // Daten
    public static final String CONST_NODETYPE_IDENTIFIER_OPEN = "OFFEN";
    public static final String CONST_NODETYPE_IDENTIFIER_LATE = "LATE";
    public static final String CONST_NODETYPE_IDENTIFIER_RUNNNING = "RUNNING";
    public static final String CONST_NODETYPE_IDENTIFIER_SHORT = "WARNING";
    public static final String CONST_NODETYPE_IDENTIFIER_DONE = "ERLEDIGT";
    public static final String CONST_NODETYPE_IDENTIFIER_CANCELED = "VERWORFEN";

    protected static SysDataService sysDataService = new SysDataServiceImpl();
    protected static MetaDataService metaDataService = new MetaDataServiceImpl();
    protected static NodeService nodeDataService = new TaskNodeService();

    public NodeService getNodeService() {
        return nodeDataService;
    }
    public static void setNodeDataService(NodeService newNodeDataService) {
        nodeDataService = newNodeDataService;
    }
    public static NodeService getConfiguredNodeService() {
        return nodeDataService;
    }

    // Status-Konstanten
    public static Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_OPEN, CONST_NODETYPE_IDENTIFIER_OPEN);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_RUNNNING, CONST_NODETYPE_IDENTIFIER_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_LATE, CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_SHORT, CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_DONE, CONST_NODETYPE_IDENTIFIER_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_CANCELED, CONST_NODETYPE_IDENTIFIER_CANCELED);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("OPEN", CONST_NODETYPE_IDENTIFIER_OPEN);
        CONST_MAP_NODETYPE_IDENTIFIER.put("OFFEN", CONST_NODETYPE_IDENTIFIER_OPEN);
        CONST_MAP_NODETYPE_IDENTIFIER.put("RUNNING", CONST_NODETYPE_IDENTIFIER_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("LAUFEND", CONST_NODETYPE_IDENTIFIER_RUNNNING);
        CONST_MAP_NODETYPE_IDENTIFIER.put("LATE", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("OVERDUE", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("VERSPÄTET", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("VERSPAETET", CONST_NODETYPE_IDENTIFIER_LATE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("SHORT", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("ÜBERFÄLLIG", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("UEBERFAELLIG", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("WARNING", CONST_NODETYPE_IDENTIFIER_SHORT);
        CONST_MAP_NODETYPE_IDENTIFIER.put("DONE", CONST_NODETYPE_IDENTIFIER_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("ERLEDIGT", CONST_NODETYPE_IDENTIFIER_DONE);
        CONST_MAP_NODETYPE_IDENTIFIER.put("CANCELED", CONST_NODETYPE_IDENTIFIER_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("VERWORFEN", CONST_NODETYPE_IDENTIFIER_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("GELOESCHT", CONST_NODETYPE_IDENTIFIER_CANCELED);
        CONST_MAP_NODETYPE_IDENTIFIER.put("ABGEBROCHEN", CONST_NODETYPE_IDENTIFIER_CANCELED);
    }

    @Override
    public Map<String, Object> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
    
    @Override
    public String getDataBlocks4CheckSum() throws Exception {
        // Content erzeugen
        StringBuffer data = new StringBuffer();
        
        data.append(super.getDataBlocks4CheckSum())
            .append(" istStand=").append(getIstStand())
            .append(" istStart=").append(getNewDate(getIstStart()))
            .append(" istEnde=").append(getNewDate(getIstEnde()))
            .append(" istAufwand=").append(getIstAufwand())
            .append(" istTask=").append(getIstTask())
            .append(" planStart=").append(getNewDate(getPlanStart()))
            .append(" planEnde=").append(getNewDate(getPlanEnde()))
            .append(" planAufwand=").append(getPlanAufwand())
            .append(" planTask=").append(getPlanTask())
            ;
        return data.toString();
    }
    
    
    protected Date getNewDate(Date oldDate) {
        return (oldDate != null ? new Date(oldDate.getTime()) : null);
    }
    
    @Override
    public Date getCurrentStart() {
        // wenn belegt IST-Daten benutzen, sonst Plandaten
        Date curStart = getIstStart();
        if (curStart != null) {
            return curStart;
        }

        return getPlanStart();
    }

    @Override
    public Date getCurrentEnde() {
        // wenn belegt IST-Daten benutzen, sonst Plandaten
        Date curEnde = getIstEnde();
        Date curPlanEnde = this.getPlanEnde();
        Date curIstStart = getIstStart();
        Date curIstEnde = getIstEnde();
        Double curIstStan = getIstStand();
        if (curEnde != null ) {
            if (curPlanEnde != null && curIstStart != null) {
                // wenn Istdaten vor Plandaten und noch nicht fertig, dann Plandaten
                if (curIstEnde.before(curPlanEnde) && curIstStan < 100) {
                    return curPlanEnde;
                }
            }
            return curIstEnde;
        }

        return curPlanEnde;
    }
    
}
