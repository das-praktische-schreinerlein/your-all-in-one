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
package de.yaio.extension.datatransfer.ppl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.ImporterImpl;

/**
 * <h4>FeatureDomain:</h4>
*     import
 * <h4>FeatureDescription:</h4>
 *     import of Nodes in PPL-Format
 * 
 * @package de.yaio.extension.datatransfer..ppl
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PPLImporter extends ImporterImpl {

    /**
     * <h4>FeatureDomain:</h4>
     *     Constructor
     * <h4>FeatureDescription:</h4>
     *     Importer to import/parse nodes in PPL-Format
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>initialize the importer
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Constructor
     *  @param options - the importoptions for the parser...
     */
    public PPLImporter(ImportOptions options) {
        super(options);
    }

    ////////////////
    // service-functions to generate Nodes from source
    ////////////////
    
    /**
     * <h4>FeatureDomain:</h4>
     *     import
     * <h4>FeatureDescription:</h4>
     *     extracts the nodes from PPL nodeSrc (single Line) and appends them to masterNode
     * <h4>FeatureResult:</h4>
     *     <li>updates masterNode - appends from nodeSrc extracted Nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param masterNode - node to append extracted Nodes
     * @param nodeSrc - PPL nodeSrc to be parsed
     * @param delimiter - delimiter of PPL node-hirarchy
     * @throws Exception - parser/format-Exceptions possible
     */
    public void extractNodeFromSrcLine(DataDomain masterNode, String nodeSrc, String delimiter) throws Exception {
        // Parameter pruefen
        if (masterNode == null) {
            throw new IllegalArgumentException("Masternode must not be null: '" + masterNode + "'");
        }
        if (nodeSrc == null || nodeSrc.trim().length() <= 0) {
            throw new IllegalArgumentException("NodeSrc must not be empty: '" + nodeSrc + "'");
        }
        if (delimiter == null || delimiter.length() <= 0) {
            throw new IllegalArgumentException("Delimiter must not be empty: '" + delimiter + "'");
        }

        // NodeSrc trennen
        String [] lstNodeSrc = nodeSrc.split(delimiter);

        // die verschiedenen Node durchlaufen
        if (lstNodeSrc != null) {
            DataDomain curParentNode = masterNode;
            String curNodeSrc = null;
            for (int zaehler = 0; zaehler < lstNodeSrc.length; zaehler++) {
                // pruefen ob die Node schon am aktuellen Master existiert, wenn nicht neu anlegen
                String curName = lstNodeSrc[zaehler];

                // CurNodeSrc belegen
                if (curNodeSrc == null) {
                    // 1. Element
                    curNodeSrc = curName;
                } else {
                    // Folgeelement
                    curNodeSrc += delimiter + curName;
                }

                // Node anhand des Konstruktors anlegen
                DataDomain myNode = this.createNodeObjFromText(curId++, curNodeSrc, 
                        curName, null);
                String refName = myNode.getIdForChildByNameMap();

                // Pruefung ob schon existiert
                DataDomain curNode = curParentNode.getChildNodesByNameMap().get(refName);
                if (curNode != null) {
                    // existiert schon: nichts zu tun - zum naechsten
                } else {
                    // existiert noch nicht: neu anlegen
                    curNode = this.createNodeObjFromText(curId++, curNodeSrc, 
                            curName, curParentNode);
                }

                curParentNode = curNode;
            }
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     import
     * <h4>FeatureDescription:</h4>
     *     extracts the nodes from PPL lstNodeSrc (list of lines) and appends them to masterNode
     * <h4>FeatureResult:</h4>
     *     <li>updates masterNode - appends from nodeSrc extracted Nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param masterNode - node to append extracted Nodes
     * @param lstNodeSrc - list of PPL nodeSrc to be parsed
     * @param delimiter - delimiter of PPL node-hirarchy
     * @throws Exception - parser/format-Exceptions possible
     */
    public void extractNodesFromLines(DataDomain masterNode, String [] lstNodeSrc, String delimiter) throws Exception {
        // Parameter pruefen
        if (lstNodeSrc == null || lstNodeSrc.length <= 0) {
            throw new IllegalArgumentException("LstNodeSrc must not be empty: '" + lstNodeSrc + "'");
        }

        // die verschiedenen Zeilen durchlaufen
        for (int zaehler = 0; zaehler < lstNodeSrc.length; zaehler++) {
            // pruefen ob die Node schon am aktuellen Master existiert, wenn nicht neu anlegen
            String curLine = lstNodeSrc[zaehler];
            this.extractNodeFromSrcLine(masterNode, curLine, delimiter);
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     import
     * <h4>FeatureDescription:</h4>
     *     extracts the nodes from PPL nodeSrc (several lines) and appends them to masterNode
     * <h4>FeatureResult:</h4>
     *     <li>updates masterNode - appends from nodeSrc extracted Nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param masterNode - node to append extracted Nodes
     * @param nodesSrc - PPL nodeSrc (several lines) to be parsed
     * @param delimiter - delimiter of PPL node-hirarchy
     * @throws Exception - parser/format-Exceptions possible
     */
    public void extractNodesFromLines(DataDomain masterNode, String nodesSrc, String delimiter) throws Exception {
        if (nodesSrc == null || nodesSrc.trim().length() <= 0) {
            throw new IllegalArgumentException("NodesSrc must not be empty: '" + nodesSrc + "'");
        }

        // NodesSrc trennen
        nodesSrc.replaceAll("\n\r", PPLService.LINE_DELIMITER);
        nodesSrc.replaceAll("\r\n", PPLService.LINE_DELIMITER);
        String [] lstNodeSrc = nodesSrc.split(PPLService.LINE_DELIMITER);

        // die verschiedenen Zeilen durchlaufen
        if (lstNodeSrc != null && lstNodeSrc.length > 0) {
            this.extractNodesFromLines(masterNode, lstNodeSrc, delimiter);
        }
    }


    /**
     * <h4>FeatureDomain:</h4>
     *     import
     * <h4>FeatureDescription:</h4>
     *     extracts the nodes from file and appends them to masterNode
     * <h4>FeatureResult:</h4>
     *     <li>updates masterNode - appends from nodeSrc extracted Nodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Parser
     * @param masterNode - node to append extracted Nodes
     * @param fileName - fileName with the PPL-nodeSrc
     * @param delimiter - delimiter of PPL node-hirarchy
     * @throws Exception - parser/format/io-Exceptions possible
     */
    public void extractNodesFromFile(DataDomain masterNode, String fileName, String delimiter) throws Exception {
        String fileContent = readFromFile(fileName);
        this.extractNodesFromLines(masterNode, fileContent, delimiter);
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     import
     * <h4>FeatureDescription:</h4>
     *     read filecontent
     * <h4>FeatureResult:</h4>
     *     <li>returnValue String - filecontent
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Tools
     * @param fileName - fileName to read
     * @throws Exception - io-Exceptions possible
     * @return filecontent
     */
    public static String readFromFile(String fileName) throws Exception {
        // Parameter pruefen
        if (fileName == null || fileName.trim().length() <= 0) {
            throw new IllegalArgumentException("FileName must not be empty: '" + fileName + "'");
        }

        // Datei einlesen
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fileReader);
        StringBuffer fileContent = new StringBuffer();
        String line = null;
        while((line = bReader.readLine()) != null){
            fileContent.append(line).append("\n");
        }
        bReader.close();

        return fileContent.toString();
    }

}
