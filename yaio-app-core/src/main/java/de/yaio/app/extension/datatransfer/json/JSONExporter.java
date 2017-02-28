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
package de.yaio.app.extension.datatransfer.json;

import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.extension.datatransfer.csv.CSVExporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/** 
 * export of Nodes in JSON-format
 * 
 * @FeatureDomain                DatenExport Praesentation
 * @package                      de.yaio.extension.datatransfer.ical
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class JSONExporter extends CSVExporter {
    
    protected final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    protected final DateFormat TF = new SimpleDateFormat("HH-mm-ss");

    /** 
     * service functions to export nodes as JSON
     */
    public JSONExporter() {
        super();
    }

    @Override
    public String getMasterNodeResult(final DataDomain masterNode, final OutputOptions oOptions)
           throws ConverterException {
        
//        // Parameter pruefen
//        if (masterNode == null) {
//            throw new IllegalArgumentException("Masternode must not be null: '" 
//        + masterNode + "'");
//        }
//
//        // Mastennode falls leer löschen
//        Map<String, DataDomain> masterChilds = masterNode.getChildNodesByNameMap();
//        if (masterChilds.size() == 1) {
//            masterNode = (DataDomain) masterChilds.values().toArray()[0];
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("set ParentNode empty for new Masternode " 
//                        + masterNode.getNameForLogger());
//            }
//            masterNode.setParentNode(null);
//        }
//
//        // recalcData
//        masterNode.recalcData(NodeService.RecalcRecurseDirection.CHILDREN);
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

    @Override
    public String getFieldDelimiter() {
        return "\", \"";
    }
    @Override
    public String getLineStart() {
        return "[\"";
    }
    @Override
    public String getLineEnd() {
        return "\"],\n";
    }

    @Override
    public String formatNodeDate(final BaseNode curNode, final Date src) {
        String res = "";
        if (src != null) {
            res = DF.format(src) + "T" + TF.format(src) + "Z";
        }
        return res;
    }
    @Override
    public String formatNodeNumber(final BaseNode curNode, final Double src,
                                   final int minStellen, final int maxStellen) {
        return baseFormatter.formatNumber(src, minStellen, maxStellen).replace(".", ",");
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        super.setTimeZone(timeZone);
        DF.setTimeZone(timeZone);
        TF.setTimeZone(timeZone);
    }
}
