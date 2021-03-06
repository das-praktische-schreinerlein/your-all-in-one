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
package de.yaio.app.extension.datatransfer.wiki;

import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.datatransfer.common.ParserException;
import de.yaio.app.datatransfer.exporter.OutputOptions;
import de.yaio.app.datatransfer.exporter.OutputOptionsImpl;
import de.yaio.app.datatransfer.importer.ImportOptions;
import de.yaio.app.datatransfer.importer.ImportOptionsImpl;
import de.yaio.app.datatransfer.importer.ImporterImpl;
import de.yaio.app.extension.datatransfer.ppl.PPLExporter;
import de.yaio.app.extension.datatransfer.ppl.PPLImporter;
import de.yaio.app.extension.datatransfer.ppl.PPLService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * import of Nodes in Wiki-Format
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class WikiImporter extends ImporterImpl {

    protected static final String CONST_UE = "=";
    protected static final String CONST_LIST = "*";

    protected static final String CONST_PATTERN =
            "^[ ]*?([" + CONST_UE + "\\" + CONST_LIST + "]+)[ ]*(.*)";

    protected static final String testStr =
                    "= LATE - Projekt3" + PPLService.LINE_DELIMITER
                    + "=== OFFEN - Unterprojekt1"  + PPLService.LINE_DELIMITER
                    + "** OFFEN - Punkt0" + PPLService.LINE_DELIMITER
                    + "* OFFEN - Punkt1" + PPLService.LINE_DELIMITER
                    + "** OFFEN - Punkt1.1" + PPLService.LINE_DELIMITER
                    + "** OFFEN - Punkt1.2" + PPLService.LINE_DELIMITER
                    + "** Punkt1.2 [Ist: 20% 10h ] [Plan: 10h ]" + PPLService.LINE_DELIMITER
                    + "** Punkt1.2" + PPLService.LINE_DELIMITER
                    + "** Punkt1.2" + PPLService.LINE_DELIMITER
                    + "== RUNNING - Unterprojekt2" + PPLService.LINE_DELIMITER
                    + "adejigwtiojboiaqrjiqoajbhiaqrbn" + PPLService.LINE_DELIMITER
                    + "* RUNNING - Punkt1" + PPLService.LINE_DELIMITER
                    + "== LATE - Unterprojekt3" + PPLService.LINE_DELIMITER
                    + "=== LATE - Unterprojekt4" + PPLService.LINE_DELIMITER
                    + "adejigwtiojboiaqrjiqoajbhiaqrbn" + PPLService.LINE_DELIMITER
                    + "* RUNNING - Punkt1 [Plan: 10h 22.03.2014-23.05.2014]" + PPLService.LINE_DELIMITER
                    + "  adejigwtiojboiaqrjiqoajbhiaqrbn" + PPLService.LINE_DELIMITER
                    + "** LATE - Punkt1.1" + PPLService.LINE_DELIMITER
                    + "** DONE - Punkt1.2" + PPLService.LINE_DELIMITER;

    private static final Pattern CONST_WIKI =
            Pattern.compile(CONST_PATTERN, Pattern.UNICODE_CHARACTER_CLASS);


    private static final Logger LOGGER =
            Logger.getLogger(WikiImporter.class);

    /** 
     * helper single WikiStructLine
     * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
     */
    public class WikiStructLine {

        protected String text;
        protected String desc;
        private String wiki;
        private String idPraefix;
        private String hirarchy;

        /** 
         * create object of WikiStructLine
         * @param wiki                   the wiki-sourceline
         * @param text                   the text
         * @param desc                   the optional desc
         */
        public WikiStructLine(final String wiki, final String text, final String desc) {
            this.setWiki(wiki);
            this.text = text;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "WL: " + getWiki() + " " + text + " HI:" + getHirarchy();
        }

        /** 
         * get the wiki-hirarchy-line
         * @return                       wiki-hirarchy-line
         */
        public String getHirarchy() {
            return this.hirarchy;
        }

        /** 
         * get clean wiki-text (replace \* -> "", \t - <WLTAB> and escaped desc
         * @return                       clean Wiki-text
         */
        public String getCleanText() {
            // Standardzeile bereinigen
            String dummyText = text;
            dummyText = dummyText.replaceAll("\\*", "");
            dummyText = dummyText.replaceAll("\t", "<WLTAB>");

            // ggf. desc anhaengen
            dummyText += this.getEscapedDesc(" ProjektDesc:");

            return dummyText;
        }

        /** 
         * append to desc
         * @param desc                   desc to append
         */
        public void addDesc(final String desc) {
            // an bestehende desc anfuegen
            if (this.desc == null) {
                this.desc = desc;
            } else {
                this.desc += "\n" + desc;
            }
        }

        /** 
         * get escaped wiki-desc (\n -> <WLBR>, \t -> <WLTAB>
         * @param praefix                to set in front of the desc
         * @return                       escaped desc
         */
        public String getEscapedDesc(final String praefix) {
            String dummyText = "";

            // falls belegt, mit Praefix versehen
            if (desc != null && desc.length() > 0) {
                dummyText = praefix + desc;
            }

            // Sonderzeichen entfernen
            //            dummyText = dummyText.replaceAll("\\", "<WLESC>");
            dummyText = dummyText.replaceAll("\n", "<WLBR>");
            dummyText = dummyText.replaceAll("\t", "<WLTAB>");

            return dummyText;
        }

        /**
         * @return                       the {@link WikiStructLine#idPraefix}
         */
        public String getIdPraefix() {
            return idPraefix;
        }

        /**
         * @param idPraefix              the {@link WikiStructLine#idPraefix} to set
         */
        public void setIdPraefix(final String idPraefix) {
            this.idPraefix = idPraefix;
        }

        /**
         * @param hirarchy               the {@link WikiStructLine#hirarchy} to set
         */
        public void setHirarchy(final String hirarchy) {
            this.hirarchy = hirarchy;
        }

        /**
         * @return                       the {@link WikiStructLine#wiki}
         */
        public String getWiki() {
            return wiki;
        }

        /**
         * @param wiki                   the {@link WikiStructLine#wiki} to set
         */
        public void setWiki(final String wiki) {
            this.wiki = wiki;
        }
    }

    protected PPLImporter importer = new PPLImporter(new ImportOptionsImpl());
    protected PPLExporter exporter = new PPLExporter();

    /** 
     * Importer to import/parse nodes in Wiki-Format
     *  @param options                the importoptions for the parser...
     */
    public WikiImporter(final ImportOptions options) {
        super(options);
    }

    /** 
     * generate helper-OutputOptions for generation of the Wiki-area (show all, hide calcSum+desc)
     * @return                       OuputOptions for generation of the Wiki-area
     */
    public OutputOptions genOutputOptionsForBaseDataExport() {
        OutputOptions options = new OutputOptionsImpl();

        options.setFlgShowChildrenSum(false);
        options.setFlgShowDesc(false);

        return options;
    }

    
    
    /** 
     * search for pattern in haystack
     * @param pattern                needle to find
     * @param haystack               haystack
     * @return                       list of matching results
     */
    public static List<MatchResult> findMatches(final Pattern pattern, final CharSequence haystack) {
        List<MatchResult> results = null;

        for (Matcher m = pattern.matcher(haystack); m.find();) {
            if (results == null) {
                results = new ArrayList<MatchResult>();
            }
            results.add(m.toMatchResult());
        }

        return results;
    }

    
    /** 
     * extracts the list of WikiStructLine from Wiki nodeSrc (several Lines)
     * @param nodeSrc                nodeSrc to be parsed
     * @param inputOptions           ImportOptions for the parser
     * @return                       list of extracted WikiStructLine
     */
    public List<WikiStructLine> extractWikiStructLines(final String nodeSrc,
            final WikiImportOptions inputOptions) {
        List<WikiStructLine> wikiLines = new ArrayList<WikiStructLine>();

        if (nodeSrc == null) {
            return null;
        }
        String[] lines = nodeSrc.split("\n");
        if (lines != null) {
            WikiStructLine curWikiLine = null;

            // alle Zeilen durchlaufen
            for (int zaehler = 0; zaehler < lines.length; zaehler++) {
                // WikiZeilen identifizieren
                String line = lines[zaehler];
                line = line.replace("\r", "");
                line = line.replace("\n", "");
                List<MatchResult> matches = findMatches(getWikiPattern(), line);
                if (matches == null || matches.size() <= 0) {
                    // falls String keine Wikizeile ist, an aktuelle Wikizeile anhaengen
                    if (curWikiLine != null) {
                        curWikiLine.addDesc(line);
                    }
                    continue;
                }

                // alle Treffer iterieren
                for (MatchResult match : matches) {
                    // falls Wiki + text gefunden: Wikiline an Liste anhaengen
                    if (match.groupCount() == 2) {
                        // vorherige Wikizeile beenden, wenn vorhanden
                        if (curWikiLine != null) {
                            wikiLines.add(curWikiLine);
                        }


                        // neue Wikiline erzeugen
                        curWikiLine =
                                new WikiStructLine(match.group(1), match.group(2), null);
                    }
                }
            }

            // letzte Wikizeile beenden, wenn vorhanden
            if (curWikiLine != null) {
                wikiLines.add(curWikiLine);
            }

        }

        return wikiLines;
    }

    /** 
     * filter only known NodeTypes from WikiStructLines
     * @param lstWikiLines           lstWikiLines to filter
     * @param flgWFStatesOnly        filter only node with this isWFState = true
     * @param inputOptions           ImportOptions for the parser+filter
     * @return                       list of filtered WikiStructLine
     * @throws Exception             parser/format-Exceptions possible
     */
    public List<WikiStructLine> filterOnlyKnownNodeTypesFromWikiStructLines(
            final List<WikiStructLine> lstWikiLines, 
            final boolean flgWFStatesOnly, final WikiImportOptions inputOptions) {
        List<WikiStructLine> lstWikiTab = new ArrayList<WikiStructLine>();
        if (lstWikiLines != null) {

            // Filter konfigurieren
            Map<String, Object> mpStates = null;
            if (!StringUtils.isEmpty(inputOptions.getStrReadIfStatusInListOnly())) {
                mpStates = new HashMap<String, Object>();
                String[] arrStatusFilter = 
                        inputOptions.getStrReadIfStatusInListOnly().split(",");
                for (int zaehler = 0; zaehler < arrStatusFilter.length; zaehler++) {
                    mpStates.put(arrStatusFilter[zaehler], arrStatusFilter[zaehler]);
                }
            }

            // Identifier konfigurieren
            Map<String, Class<?>> hshNodeTypeIdentifier = 
                    this.importer.getNodeFactory().getHshNodeTypeIdentifier();
            Map<String, String> hshNodeTypes = this.importer.getHshNodeTypeIdentifierVariantMapping();

            // alle iterieren
            for (WikiStructLine curWk : lstWikiLines) {
                String state = BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN;
                String ue = curWk.getCleanText();

                // Statusdefinition durchlaufen unn pruefen ob Name so beginnt
                for (String stateDef : hshNodeTypeIdentifier.keySet()) {
                    if (ue.startsWith(stateDef)) {
                        state = (String) hshNodeTypes.get(stateDef);
                    }
                }

                // falls Statusfilter dass filtern
                if ((mpStates != null) && (mpStates.get(state) == null)) {
                    // SKIP: not in Filterlist
                    continue;
                }

                // gefunden
                if (BaseNodeService.CONST_NODETYPE_IDENTIFIER_UNKNOWN.equals(state)) {
                    // SKIP: UNKNOWN
                    continue;
                } else if (flgWFStatesOnly && !this.importer.isWFStatus(state)) {
                    // SKIP: INFO
                    continue;
                } else {
                    // gueltiger WF-Status
                    lstWikiTab.add(curWk);
                }
            }
        }

        return lstWikiTab;
    }

    /** 
     * normalizes and sorts the WikiStructLine
     * @param lstWikiLines           WikiStructLines to be normalized
     * @param inputOptions           ImportOptions for the parser
     * @return                       list of sorted and normalized WikiStructLine
     * @throws ParserException       parser/format-Exceptions possible
     */
    public List<WikiStructLine> normalizeWikiStructLines(final List<WikiStructLine> lstWikiLines, 
            final WikiImportOptions inputOptions) throws ParserException {
        // temporaere Arbeitslisten
        List<WikiStructLine> lstWikiTab = new ArrayList<WikiStructLine>();
        List<WikiStructLine> lastUe = new ArrayList<WikiStructLine>();
        List<WikiStructLine> lastUl = new ArrayList<WikiStructLine>();

        if (lstWikiLines != null) {
            // Dummy-Masternode
            BaseNode masterNode = null;
            masterNode = (BaseNode) importer.createNodeObjFromText(1, "Master", "Master", null);
            OutputOptions oOptions = new OutputOptionsImpl();

            // alle Wikilines iterieren
            for (WikiStructLine curWk : lstWikiLines) {
                // aktuelle Wikiline bearbeiten
                String wiki = curWk.getWiki();
                int ebene = wiki.length();
                String cleanText = curWk.getCleanText();

                // Node anlegen
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("gen new Node for:" + cleanText);
                }
                importer.extractNodeFromSrcLine(masterNode, cleanText, "\t");
                Object key = masterNode.getChildNodesByNameMap().keySet().toArray()[0];
                BaseNode curNode = (BaseNode) masterNode.getChildNodesByNameMap().get(key);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("got new Node:" + key + " Node:" + curNode.getClass() 
                            + " for " + cleanText);
                }

                // Id-Praefix des Vorgaengers extrahieren und an Masternode setzen
                String idPraefixVorgaenger = inputOptions.getStrDefaultMetaNodePraefix();
                idPraefixVorgaenger = StringUtils.isNotEmpty(idPraefixVorgaenger) 
                        ? idPraefixVorgaenger : ImportOptions.CONST_DEFAULT_META_NODE_PRAEFIX;
                WikiStructLine vorgaengerWk = null;
                if (wiki.startsWith(getWikiCharUe()) && ebene > 1) {
                    // Vorgaenger-UE vorhanden
                    int idxVorgaenger = ebene - 2;
                    if (lastUe.size() > idxVorgaenger) {
                        vorgaengerWk = (WikiStructLine) lastUe.get(idxVorgaenger);
                    }
                } else if (wiki.startsWith(getWikiCharList())) {

                    if (ebene > 1) {
                        // Nachfolger
                        int idxVorgaenger = ebene - 2;
                        if (lastUl.size() > idxVorgaenger) {
                            vorgaengerWk = (WikiStructLine) lastUl.get(idxVorgaenger);
                        }
                    } else if (lastUe.size() > 0) {
                        vorgaengerWk = (WikiStructLine) lastUe.get(lastUe.size() - 1);
                    }
                }
                if (vorgaengerWk != null) {
                    idPraefixVorgaenger = vorgaengerWk.getIdPraefix();
                }
                masterNode.setMetaNodePraefix(idPraefixVorgaenger);

                // Node initialisieren und aktuelles idPraefix belegen
                initNodeData(curNode);
                curWk.setIdPraefix(curNode.getMetaNodePraefix());
                
                // Node ohne Desc generieren
                oOptions = genOutputOptionsForBaseDataExport();
                StringBuffer baseRes = new StringBuffer();
                exporter.formatNodeDataDomains(curNode, baseRes, oOptions);
                cleanText = baseRes.toString();
                cleanText = cleanText.replaceAll("\\*", "");
                //cleanText = cleanText.replaceAll("\\", "<WLESC>");
                cleanText = cleanText.replaceAll("\t", "<WLTAB>");
                cleanText = cleanText.trim();

                // Desc anhaengen
                cleanText += curWk.getEscapedDesc(" ProjektDesc:");

                // Ueberschrift
                if (wiki.startsWith(getWikiCharUe()) && inputOptions.isFlgReadUe()) {
                    if (ebene == 1) {
                        // 1. Ebene belegen
                        lastUe = new ArrayList<WikiStructLine>();
                        curWk.setHirarchy(cleanText);
                    } else {
                        // Nachfolger
                        int idxVorgaenger = ebene - 2;
                        int idxMe = ebene - 1;

                        // falls leer BasisElement
                        if (lastUe.size() == 0) {
                            WikiStructLine newWK = new WikiStructLine(getWikiCharUe(), "DEFAULT", null);
                            newWK.setHirarchy(newWK.getCleanText());
                            lastUe.add(newWK);
                        }
                        // dann weiter auffuellen
                        if (lastUe.size() <= idxMe) {
                            // mit Dummy-WikiLines Auffuellen
                            for (int zaehler = lastUe.size() - 1; zaehler <= idxMe; zaehler++) {
                                vorgaengerWk = (WikiStructLine) lastUe.get(zaehler);
                                WikiStructLine newWK = new WikiStructLine(getWikiCharUe(), "DEFAULT", null);
                                newWK.setHirarchy(vorgaengerWk.getHirarchy()
                                        + PPLService.DEFAULT_ENTRY_DELIMITER
                                        + newWK.getCleanText());
                                lastUe.add(newWK);
                            }
                        }

                        // Hirarchy des Vorgaengers kopieren
                        vorgaengerWk = (WikiStructLine) lastUe.get(idxVorgaenger);
                        curWk.setHirarchy(vorgaengerWk.getHirarchy()
                                + PPLService.DEFAULT_ENTRY_DELIMITER
                                + cleanText);

                        // die Vorganeger kopieren
                        List<WikiStructLine> tmpList = lastUe.subList(0, idxVorgaenger + 1);
                        lastUe = tmpList;
                    }

                    // Unterlisten leeren
                    lastUl = new ArrayList<WikiStructLine>();

                    // mich selbst einfuegen an LastUe
                    lastUe.add(curWk);

                    // an Resultliste anfuegen
                    lstWikiTab.add(curWk);
                } else if (wiki.startsWith(getWikiCharList()) && inputOptions.isFlgReadList()) {
                    if (ebene == 1) {
                        // 1. Ebene belegen
                        lastUl = new ArrayList<WikiStructLine>();

                        String parent = "";
                        if (lastUe.size() > 0) {
                            WikiStructLine curUe = (WikiStructLine) lastUe.get(lastUe.size() - 1);
                            parent = curUe.getHirarchy()
                                    + PPLService.DEFAULT_ENTRY_DELIMITER;
                        }
                        curWk.setHirarchy(parent
                                + cleanText);
                    } else {
                        // Nachfolger

                        int idxVorgaenger = ebene - 2;
                        int idxMe = ebene - 1;

                        // falls leer BasisEleent
                        if (lastUl.size() == 0) {
                            WikiStructLine newWK = new WikiStructLine(getWikiCharList(), "DEFAULT", null);
                            String parent = "";
                            if (lastUe.size() > 0) {
                                WikiStructLine curUe = (WikiStructLine) lastUe.get(lastUe.size() - 1);
                                parent = curUe.getHirarchy()
                                        + PPLService.DEFAULT_ENTRY_DELIMITER;
                            }
                            newWK.setHirarchy(parent
                                    + newWK.getCleanText());
                            lastUl.add(newWK);
                        }
                        // dann weiter auffuellen
                        if (lastUl.size() <= idxMe) {
                            // mit Dummy-WikiLines Auffuellen
                            for (int zaehler = lastUl.size() - 1; zaehler <= idxMe; zaehler++) {
                                vorgaengerWk = (WikiStructLine) lastUl.get(zaehler);
                                WikiStructLine newWK = new WikiStructLine(getWikiCharUe(), "DEFAULT", null);
                                newWK.setHirarchy(vorgaengerWk.getHirarchy()
                                        + PPLService.DEFAULT_ENTRY_DELIMITER
                                        + newWK.getCleanText());
                                lastUl.add(newWK);
                            }
                        }

                        // Hirarchy des Vorgaengers kopieren
                        vorgaengerWk = (WikiStructLine) lastUl.get(idxVorgaenger);
                        curWk.setHirarchy(vorgaengerWk.getHirarchy()
                                + PPLService.DEFAULT_ENTRY_DELIMITER
                                + cleanText);

                        // die Vorganeger kopieren
                        List<WikiStructLine> tmpList = lastUl.subList(0, idxVorgaenger + 1);
                        lastUl = tmpList;
                    }

                    // mich selbst einfuegen an LastUl
                    lastUl.add(curWk);

                    // an Resultliste anfuegen
                    lstWikiTab.add(curWk);
                }

                // alle Kindselemente loeschen
                masterNode.getChildNodesByNameMap().clear();
            }
        }


        return lstWikiTab;
    }
    
    protected void initNodeData(final BaseNode curNode) {
        curNode.initMetaData();
        curNode.initSysData(true);
    }

    /** 
     * extracts the list of WikiStructLine from Wiki nodeSrc (several Lines)
     * normalize them with normalizeWikiStructLine and filter them if ImportOptions set
     * @param src                    src to be parsed
     * @param inputOptions           ImportOptions for the parser
     * @return                       list of extracted WikiStructLine
     * @throws ParserException       parser/format-Exceptions possible
     */
    public List<WikiStructLine> extractWikiStructLinesFromSrc(final String src,
            final WikiImportOptions inputOptions) throws ParserException {
        // Parameter pruefen
        if (src == null || src.trim().length() <= 0) {
            throw new IllegalArgumentException("Src must not be empty: '" + src + "'");
        }

        // Wikilines einlesen
        List<WikiStructLine> wikiLines = extractWikiStructLines(src, inputOptions);
        List<WikiStructLine> projektWikiLines = wikiLines;

        // Falls Filter gesetzt: nur zeilen mit Status filtern
        if (inputOptions.flgReadWithStatusOnly
                || inputOptions.flgReadWithWFStatusOnly
                || !StringUtils.isEmpty(inputOptions.getStrReadIfStatusInListOnly())) {
            projektWikiLines = filterOnlyKnownNodeTypesFromWikiStructLines(wikiLines, 
                    inputOptions.flgReadWithWFStatusOnly, inputOptions);
        }

        // Zeilen sortieren
        List<WikiStructLine> projektWikiEntries = normalizeWikiStructLines(projektWikiLines, inputOptions);

        return projektWikiEntries;
    }

    /** 
     * extracts the list of WikiStructLine from Wiki-file
     * @param fileName               fileName to be read and parsed
     * @param inputOptions           ImportOptions for the parser
     * @return                       list of extracted WikiStructLine
     * @throws ParserException       parser-Exceptions possible
     * @throws IOException           io-Exceptions possible
     */
    public List<WikiStructLine> extractWikiStructLinesFromFile(final String fileName,
            final WikiImportOptions inputOptions) throws IOException, ParserException {
        String fileContent = PPLImporter.readFromFile(fileName);
        List<WikiStructLine> wikiLines = extractWikiStructLinesFromSrc(fileContent, inputOptions);

        return wikiLines;
    }
    
    
    public Pattern getWikiPattern() {
        return CONST_WIKI;
    }

    public String getWikiCharUe() {
        return CONST_UE;
    }
    
    public String getWikiCharList() {
        return CONST_LIST;
    }
    
    /**
     * @param args                   the command line arguments
     */
    public static void main(final String[] args) {
        WikiImporter importer = new WikiImporter(new WikiImportOptions());
        try {

            // nur Statusnodes filtern
            boolean flgFilterStatusNotes = false;
            if (args.length > 1) {
                if ("1".equalsIgnoreCase(args[1]) || "true".equalsIgnoreCase(args[1])) {
                    flgFilterStatusNotes = true;
                }
            }

            WikiImportOptions inputOptions = new WikiImportOptions();
            inputOptions.flgReadWithStatusOnly = flgFilterStatusNotes;


            // aus Datei oder nur Test ??
            List<WikiStructLine> lstWikiLines;
            if (args.length > 0) {
                // aus datei
                lstWikiLines = importer.extractWikiStructLinesFromFile(args[0], inputOptions);
            } else {
                // nur Test
                lstWikiLines = importer.extractWikiStructLinesFromSrc(testStr, inputOptions);
            }

            // WikiListe ausgeben
            if (lstWikiLines != null) {
                for (WikiStructLine wk : lstWikiLines) {
                    System.out.println(wk.getHirarchy());
                }
            }
            //CHECKSTYLE.OFF: IllegalCatch - Much more readable than catching x exceptions
        } catch (Exception ex) {
            //CHECKSTYLE.ON: IllegalCatch
            ex.printStackTrace();
        }
    }
}
