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
package de.yaio.extension.datatransfer.mindmap;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.node.BaseNode;
import de.yaio.datatransfer.exporter.OutputOptions;
import de.yaio.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.extension.datatransfer.exporter.formatter.WorkflowFormatConfigurator;
import de.yaio.extension.datatransfer.wiki.WikiExporter;

/**
 * <h4>FeatureDomain:</h4>
 *     DatenExport
 *     Praesentation
 * <h4>FeatureDescription:</h4>
 *     export of Nodes in CSV-format
 * 
 * @package de.yaio.extension.datatransfer.ical
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class MindMapExporter extends WikiExporter {
    
    public static int CONST_MIN4CLOUD = 2;
    
    public static WorkflowFormatConfigurator workflowFormatConfigurator = 
            WorkflowFormatConfigurator.getWorkflowFormatConfigurator();
    
    public MindMapExporter() {
        super();
    }

    // Logger
    private static final Logger LOGGER =
        Logger.getLogger(MindMapExporter.class);
    

    @Override
    public void initDataDomainFormatter() {
        super.initDataDomainFormatter();
    };

    @Override
    public String getMasterNodeResult(DataDomain masterNode, OutputOptions oOptions)
            throws Exception {
        StringBuffer res = new StringBuffer();
        
        // show Header
        res.append("<map version=\"0.9.0\">");
        
        // show details
        res.append(super.getMasterNodeResult(masterNode, oOptions));
        
        // show footer
        res.append("</map>");
        
        return res.toString();
    }
    
    @Override
    public OutputOptions genOutputOptionsForNameArea(OutputOptions baseOOptions) {
        OutputOptions options = new OutputOptionsImpl(baseOOptions);

        // alle Show ausschalten
        options.setAllFlgShow(false);

        // Name setzen, Status anhand Defaultwert
        options.setFlgShowName(true);
        options.setFlgShowState(baseOOptions.isFlgShowState());
        options.setFlgShowType(baseOOptions.isFlgShowType());
        options.setFlgShowPlan(baseOOptions.isFlgShowPlan());
        options.setFlgShowIst(baseOOptions.isFlgShowIst());
        options.setFlgShowDocLayout(baseOOptions.isFlgShowDocLayout());

        return options;
    }
    

    @Override
    public StringBuffer getNodeResult(DataDomain node,  String praefix,
            OutputOptions oOptions) throws Exception {
        StringBuffer res = new StringBuffer();

        // Template-Nodes ignorieren
//        if (TemplateNode.class.isInstance(curNode)) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("SKIP TemplateNode node:" + curNode.getNameForLogger());
//            }
//            return res;
//        }
        
        BaseNode curNode = (BaseNode)node;

        // Anfang
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("node: start processing" + curNode.getNameForLogger());
        }

        // max. Ebene pruefen
        if (curNode.getEbene() > oOptions.getMaxEbene()) {
            return res;
        }


        
        // generate text
        StringBuffer tmpName = new StringBuffer();
        this.formatNodeDataDomains(curNode, tmpName, genOutputOptionsForNameArea(oOptions));
        String name = tmpName.toString();

        // ungültige Zeichen entfernen
        name = name.replaceAll("\"", "'");
        name = name.replaceAll("[&\\\"]", " ");

        // aktuelle Node ausgeben
        res.append("<node "
               + " id=\"" + curNode.getWorkingId() + "\""
               + " text=\"" + name + "\"");

        // Hintergrund-Farbe
        String color = workflowFormatConfigurator.getStateColor(curNode.getState());
        res.append(" background_color=\"" + color + "\"");
        res.append(" >\n");

        // Icons
        String icons = workflowFormatConfigurator.getStateIcon(curNode.getState());
        if (icons != null) {
            res.append(icons + "\n");
        }


        // iterate children
        if (curNode.getEbene() < oOptions.getMaxEbene()) {
            if (curNode.getChildNodes().size() >= CONST_MIN4CLOUD
                    && curNode.getParentNode() != null) {
                res.append("<cloud/>\n");
            }
            for (String nodeName : curNode.getChildNodesByNameMap().keySet()) {
                DataDomain childNode = curNode.getChildNodesByNameMap().get(nodeName);
                res.append(this.getNodeResult(childNode, "", oOptions));
            }
        }

        // close node
        res.append("</node>\n");

        return res;
    }
    
}
