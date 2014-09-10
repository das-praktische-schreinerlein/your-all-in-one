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
package de.yaio.extension.datatransfer.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.extension.datatransfer.csv.CSVExporter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes in JSON-format
 * 
 * @package de.yaio.extension.datatransfer.ical
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JSONExporter extends CSVExporter {
    
    protected static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    protected static DateFormat TF = new SimpleDateFormat("HH-mm-ss");

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     service functions to export nodes as JSON
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the exporter
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     */
    public JSONExporter() {
        super();
    }

    @Override
    public String getMasterNodeResult(DataDomain masterNode, OutputOptions oOptions)
            throws Exception {
        
//        // Parameter pruefen
//        if (masterNode == null) {
//            throw new IllegalArgumentException("Masternode must not be null: '" 
//        + masterNode + "'");
//        }
//
//        // Mastennode falls leer löschen
//        Map<String, DataDomain> masterChilds = masterNode.getChildNodesByNameMap();
//        if (masterChilds.size() == 1) {
//            masterNode = (DataDomain)masterChilds.values().toArray()[0];
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("set ParentNode empty for new Masternode " 
//                        + masterNode.getNameForLogger());
//            }
//            masterNode.setParentNode(null);
//        }
//
//        // recalcData
//        masterNode.recalcData(NodeService.CONST_RECURSE_DIRECTION_CHILDREN);
//
//        // generate
//        String res = this.getNodeResult(masterNode, "", oOptions).toString();
        
        String res = super.getMasterNodeResult(masterNode, oOptions);

        // delete last ","
        int pos = res.lastIndexOf(",\n");
        res = res.substring(0, pos);
        
        // comment for header
        res = "\n// " + res;
        
        return res;
    }

    public String getFieldDelimiter() {
        return "\", \"";
    }
    public String getLineStart() {
        return "[\"";
    }
    public String getLineEnd() {
        return "\"],\n";
    }
    
    public String formatNodeDate(BaseNode curNode, Date src) {
        String res = "";
        if (src != null) {
            res = DF.format(src) + "T" + TF.format(src) + "Z";
        }
        return res;
    }
    public String formatNodeNumber(BaseNode curNode, Double src, int minStellen, int maxStellen) {
        return baseFormatter.formatNumber(src, minStellen, maxStellen).replace(".", ",");
    }

}
