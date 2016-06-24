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
package de.yaio.app.extension.datatransfer.ppl;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.importer.ImporterImpl;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/** 
 * import of Nodes in PPL-Format
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.extension.datatransfer..ppl
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class PPLImporter extends ImporterImpl {

    /** identify UTF8-BOM */
    public static final String UTF8_BOM = "\uFEFF";

    /** Logger */
    private static final Logger LOGGER =
            Logger.getLogger(PPLImporter.class);
    /** 
     * Importer to import/parse nodes in PPL-Format
     *  @param options                the importoptions for the parser...
     */
    public PPLImporter(final ImportOptions options) {
        super(options);
    }

    ////////////////
    // service-functions to generate Nodes from source
    ////////////////
    
    /** 
     * extracts the nodes from PPL nodeSrc (single Line) and appends them to masterNode
     * @param masterNode             node to append extracted Nodes
     * @param nodeSrc                PPL nodeSrc to be parsed
     * @param delimiter              delimiter of PPL node-hirarchy
     * @throws Exception             parser/format-Exceptions possible
     */
    public void extractNodeFromSrcLine(final DataDomain masterNode,
                                       final String nodeSrc, 
                                       final String delimiter) throws Exception {
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
        String[] lstNodeSrc = nodeSrc.split(delimiter);

        // die verschiedenen Node durchlaufen
        if (lstNodeSrc != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("extracted lstNodeSrc:" + lstNodeSrc.length + " from nodesrc:" + nodeSrc);
            }
            DataDomain curParentNode = masterNode;
            String curNodeSrc = null;
            for (int zaehler = 0; zaehler < lstNodeSrc.length; zaehler++) {
                // pruefen ob die Node schon am aktuellen Master existiert, wenn nicht neu anlegen
                String curName = lstNodeSrc[zaehler];
                
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("extracted name:" + curName + " from nodesrc:" + nodeSrc);
                }

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
                
                // validate node
                Set<ConstraintViolation<BaseNode>> violations = myNode.validateMe();
                if (violations.size() > 0) {
                    for (ConstraintViolation<?> cViolation : violations) {
                        LOGGER.error("error while validating newNode" + myNode.getNameForLogger() + " " 
                                        + " Path: " + cViolation.getPropertyPath().toString() 
                                        + " Message:" + cViolation.getMessage()
                                        + " MessageTemplate:" + cViolation.getMessageTemplate());
                    }
                    ConstraintViolationException ex = new ConstraintViolationException(
                                    "error while validating newNode" + myNode.getNameForLogger(), 
                                    new HashSet<ConstraintViolation<?>>(violations));
                    LOGGER.error("error while validating newNode" + myNode.getNameForLogger(), ex);
                    throw ex;
                }

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
     * extracts the nodes from PPL lstNodeSrc (list of lines) and appends them to masterNode
     * @param masterNode             node to append extracted Nodes
     * @param lstNodeSrc             list of PPL nodeSrc to be parsed
     * @param delimiter              delimiter of PPL node-hirarchy
     * @throws Exception             parser/format-Exceptions possible
     */
    public void extractNodesFromLines(final DataDomain masterNode, 
                                      final String[] lstNodeSrc, 
                                      final String delimiter) throws Exception {
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
     * extracts the nodes from PPL nodeSrc (several lines) and appends them to masterNode
     * @param masterNode             node to append extracted Nodes
     * @param pNodesSrc              PPL nodeSrc (several lines) to be parsed
     * @param delimiter              delimiter of PPL node-hirarchy
     * @throws Exception             parser/format-Exceptions possible
     */
    public void extractNodesFromLines(final DataDomain masterNode, 
                                      final String pNodesSrc, 
                                      final String delimiter) throws Exception {
        String nodesSrc = pNodesSrc;
        if (nodesSrc == null || nodesSrc.trim().length() <= 0) {
            throw new IllegalArgumentException("NodesSrc must not be empty: '" + nodesSrc + "'");
        }

        // extract cr + empty lines
        nodesSrc = nodesSrc.replaceAll("\n\r", PPLService.LINE_DELIMITER);
        nodesSrc = nodesSrc.replaceAll("\r\n", PPLService.LINE_DELIMITER);
        nodesSrc = nodesSrc.replaceAll(PPLService.LINE_DELIMITER + PPLService.LINE_DELIMITER, 
                        PPLService.LINE_DELIMITER);

        // split NodesSrc
        String[] lstNodeSrc = nodesSrc.split(PPLService.LINE_DELIMITER);

        // die verschiedenen Zeilen durchlaufen
        if (lstNodeSrc != null && lstNodeSrc.length > 0) {
            this.extractNodesFromLines(masterNode, lstNodeSrc, delimiter);
        }
    }


    /** 
     * extracts the nodes from file and appends them to masterNode
     * @param masterNode             node to append extracted Nodes
     * @param fileName               fileName with the PPL-nodeSrc
     * @param delimiter              delimiter of PPL node-hirarchy
     * @throws Exception             parser/format/io-Exceptions possible
     */
    public void extractNodesFromFile(final DataDomain masterNode, 
                                     final String fileName, 
                                     final String delimiter) throws Exception {
        String fileContent = readFromFile(fileName);
        this.extractNodesFromLines(masterNode, fileContent, delimiter);
    }

    /** 
     * read filecontent
     * @param fileName               fileName to read
     * @throws Exception             io-Exceptions possible
     * @return                       filecontent
     */
    public static String readFromFile(final String fileName) throws Exception {
        // Parameter pruefen
        if (fileName == null || fileName.trim().length() <= 0) {
            throw new IllegalArgumentException("FileName must not be empty: '" + fileName + "'");
        }
        File file = new File(fileName);
        return readFromInput(file);
    }

    /** 
     * Read filecontent, detect the best matching encoding from content 
     * and encode the file to that encoding.<br>
     * Maybe the file will read twice if encoding changes at the end of the file:<br>
     * 1 run: to detect<br>
     * 2 run: to read in the best matching encoding
     * @param file                   file to read
     * @throws Exception             io-Exceptions possible
     * @return                       filecontent - the file content as string in the best detcted encoding
     */
    public static String readFromInput(final File file) throws Exception {

        // init detector and converter
        CharsetDetector detector;
        detector = new CharsetDetector();

        // open input
        InputStream input = new FileInputStream(file);
        BufferedInputStream fileStream = new BufferedInputStream(input);

        // show detectable charsets
        if (LOGGER.isDebugEnabled()) {
            for (String match : CharsetDetector.getAllDetectableCharsets()) {
                LOGGER.debug("detectable charset: " + match);
            }
        }

        // we do it in little pieces, because CharsetDetector only checks the fist bytes :-(
        CharsetMatch match;

        // read bytes to buffer
        StringBuffer fileContent = new StringBuffer();
        byte[] readBuffer = new byte[1024];
        int bytesRead;
        String lastEncoding = null;
        String bestEncoding = "";
        int bestConfidence = 0;
        int run = 1;
        boolean flgEncodingChanged = false;
        
        String bufferStr = null;
        while ((bytesRead = fileStream.read(readBuffer)) >= 0) {
            // check only the bytesRead -> the buffer will have more !!!!
            byte[] checkBuffer = new byte[bytesRead];
            System.arraycopy(readBuffer, 0, checkBuffer, 0, bytesRead);
            
            // check the bytes
            detector.setText(checkBuffer);
            match = detector.detect();
            if (lastEncoding == null || !match.getName().equals(lastEncoding)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("run " + run + " match charset: " + match.getName()
                                + " lang:" + match.getLanguage() 
                                + " conf:" + match.getConfidence());
                    for (CharsetMatch possibleMatch : detector.detectAll()) {
                        LOGGER.debug("run " + run + " possible charset: " + possibleMatch.getName() 
                                        + " lang:" + possibleMatch.getLanguage() 
                                        + " conf:" + possibleMatch.getConfidence());
                    }
                }
                
                // check if we are better than before
                if (match.getConfidence() > bestConfidence) {
                    
                    if (match.getName().startsWith("ISO-8859") 
                        && bestEncoding.startsWith("windows-12")) {
                        // dirty hack but use windows-encoding: its 'better'!!!
                    } else {
                        // check if bestEncoding changed
                        if (run > 1 
                            && !match.getName().equals(bestEncoding)) {
                            // if we are not on first run, set changed flag
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("changed best charset from: " + bestEncoding
                                        + "=" + bestConfidence
                                        + " to " + match.getName() + "=" + match.getConfidence());
                            }
                            flgEncodingChanged = true;
                        }
                        bestConfidence = match.getConfidence();
                        bestEncoding = match.getName();
                    }
                }
                lastEncoding = match.getName();
            }
            
            // do it only if encoding is fixed: if not we read the file in any case
            if (!flgEncodingChanged) {
                bufferStr = match.getString();

                // check for UTF( with leading BOM in 1 run 
                if (run == 1) {
                    if (bufferStr.startsWith(UTF8_BOM)) {
                        // UTF8 - delete leading BOM 
                        bufferStr = bufferStr.substring(1);
                        bestConfidence = 100;
                        bestEncoding = StandardCharsets.UTF_8.name();
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("detectet charset UTF8 with BOM: " + bestEncoding);
                        }
                    }
                }

                // add to string
                fileContent.append(bufferStr);
            }
            
            run++;
        }
        fileStream.close();
        
        flgEncodingChanged = true;

        String result;
        if (flgEncodingChanged) {
            // if Encoding changed, read new and use the best encoding
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("read file " + file.getAbsoluteFile()
                        + " a second time because of changed best charset: "
                        + bestEncoding + "=" + bestConfidence);
            }
            input = new FileInputStream(file);
            fileStream = new BufferedInputStream(input);
            result = IOUtils.toString(fileStream, bestEncoding);
            
            if (bestEncoding.equalsIgnoreCase(StandardCharsets.UTF_8.name()) && result.startsWith(UTF8_BOM)) {
                result = result.substring(1);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("delete BOM from : " + bestEncoding);
                }
            }
        } else {
            // we used the best encoding :-)
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("read file " + file.getAbsoluteFile()
                        + " with charset: " + bestEncoding + "=" + bestConfidence);
            }
            result = fileContent.toString();
        }
        
//        // we want all in UTF8!
//        if (!bestEncoding.equalsIgnoreCase(StandardCharsets.UTF_8.name())) {
//            LOGGER.info("convert file " + file.getAbsoluteFile() 
//                        + " with charset: " + bestEncoding 
//                        + " to " + StandardCharsets.UTF_8.name());
//            
//            byte[] encoded = result.getBytes();
//            result = new String(encoded, StandardCharsets.UTF_8);
//        }
        
        return result;
    }

}
