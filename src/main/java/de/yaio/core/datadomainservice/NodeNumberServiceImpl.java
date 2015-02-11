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
package de.yaio.core.datadomainservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import de.yaio.core.datadomain.MetaData;

/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for dataDomain: MetaData to manage Nodenumbers
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class NodeNumberServiceImpl implements NodeNumberService {
    
    /** the map with the nextNodeIds per nodePraefix */
    public static Map<String, Integer>  MAP_CUR_NEXTNODEID = null;
    
    @Override
    public synchronized Object getNextNodeNumber(final MetaData node) throws Exception {
        String praefix = node.getMetaNodePraefix();

        // pruefen ob VAR_VUR_UD initialisiert ist
        if (MAP_CUR_NEXTNODEID == null) {
            throw new Exception("Error: CurId not initialized");
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
    public synchronized void initNextNodeNumber(final String praefix, final Integer number) throws Exception {
        // falls nicht belegt: Map anlegen
        if (MAP_CUR_NEXTNODEID == null) {
            MAP_CUR_NEXTNODEID = new HashMap<String, Integer>();
        }
        MAP_CUR_NEXTNODEID.put(praefix, number);
    }

    @Override
    public Map<String, Integer> getNextNodeNumberMap() throws Exception {
        return new HashMap<String, Integer>(MAP_CUR_NEXTNODEID);
    }

    @Override
    public void exportNextNodeNumbersToFile(final String strPathIdDB)
            throws Exception {
        Properties props = new Properties();
        Map<String, Integer> nodeIds = this.getNextNodeNumberMap();  

        // alle weiteren Ids einlesen
        for (String key : nodeIds.keySet()) {
            props.put(key, nodeIds.get(key).toString());
        }

        // Properties exportieren
        FileOutputStream out = new FileOutputStream(strPathIdDB);
        props.store(out, null);
    }
    

    @Override
    public void initNextNodeNumbersFromFile(final String strPathIdDB) throws Exception {
        // ID-DB einlesen
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(strPathIdDB);
        props.load(in);
        in.close();

        // Default ID setzen
        this.initNextNodeNumber("UNKNOWN", new Integer(0));        

        // alle weiteren Ids einlesen
        for (Object key : props.keySet()) {
            this.initNextNodeNumber(key.toString(), 
                    new Integer(props.getProperty((String) key)));        
        }
    }
    
}
