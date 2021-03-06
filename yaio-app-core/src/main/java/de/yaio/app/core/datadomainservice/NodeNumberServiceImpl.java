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
package de.yaio.app.core.datadomainservice;

import de.yaio.app.core.datadomain.MetaData;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** 
 * businesslogic for dataDomain: MetaData to manage Nodenumbers
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeNumberServiceImpl implements NodeNumberService {
    
    /** the map with the nextNodeIds per nodePraefix */
    public static Map<String, Integer>  MAP_CUR_NEXTNODEID = null;
    
    private static NodeNumberServiceImpl instance = new NodeNumberServiceImpl();
    
    private String lastFileName = null;

    /** 
     * return the main instance of this service
     * @return                       the main instance of this service
     */
    public static NodeNumberServiceImpl getInstance() {
        return instance;
    }

    @Override
    public synchronized Object getNextNodeNumber(final MetaData node) {
        String praefix = node.getMetaNodePraefix();

        // pruefen ob VAR_VUR_UD initialisiert ist
        if (MAP_CUR_NEXTNODEID == null) {
            throw new IllegalStateException("MAP_CUR_NEXTNODEID not initialized");
        }
        // aktuellen Wert abfragen
        Integer curId = (Integer) MAP_CUR_NEXTNODEID.get(praefix);
        
        // setzen und und inkrementieren
        if (curId == null) {
            curId = new Integer(0);
        }
        curId++;
        
        // speichern
        MAP_CUR_NEXTNODEID.put(praefix, curId);
        
        return curId;
    }
    
    @Override
    public synchronized void initNextNodeNumber(final String praefix, final Integer number) {
        // falls nicht belegt: Map anlegen
        if (MAP_CUR_NEXTNODEID == null) {
            MAP_CUR_NEXTNODEID = new HashMap<String, Integer>();
        }
        MAP_CUR_NEXTNODEID.put(praefix, number);
    }

    @Override
    public Map<String, Integer> getNextNodeNumberMap() {
        return new HashMap<String, Integer>(MAP_CUR_NEXTNODEID);
    }

    @Override
    public boolean isInitialised() {
        return MAP_CUR_NEXTNODEID != null;
    }

    @Override
    public void exportNextNodeNumbersToFile(final String strPathIdDB) {
        Properties props = new Properties();
        Map<String, Integer> nodeIds = this.getNextNodeNumberMap();  

        // alle weiteren Ids einlesen
        for (String key : nodeIds.keySet()) {
            props.put(key, nodeIds.get(key).toString());
        }

        // Properties exportieren
        try {
            FileOutputStream out = new FileOutputStream(strPathIdDB);
            props.store(out, null);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("nextnodenumberfile not exists", ex);
        } catch (IOException ex) {
            throw new IllegalArgumentException("cant write to nextnodenumberfile", ex);
        }
    }
    

    @Override
    public void initNextNodeNumbersFromFile(final String strPathIdDB, final boolean forceReload) {
        if (!StringUtils.isEmpty(lastFileName) && !forceReload) {
            // already read and no force
            return;
        }
        
        // ID-DB einlesen
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(strPathIdDB);
            props.load(in);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("nextnodenumberfile not exists", ex);
        } catch (IOException ex) {
            throw new IllegalArgumentException("cant write to nextnodenumberfile", ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // set filename
        lastFileName = strPathIdDB;

        // Default ID setzen
        this.initNextNodeNumber("UNKNOWN", new Integer(0));

        // alle weiteren Ids einlesen
        for (Object key : props.keySet()) {
            this.initNextNodeNumber(key.toString(), 
                    new Integer(props.getProperty((String) key)));
        }
    }
    
}
