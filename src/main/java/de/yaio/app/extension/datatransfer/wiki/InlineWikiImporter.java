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

import de.yaio.app.core.datadomain.MetaData;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.datatransfer.importer.ImportOptions;

import java.util.regex.Pattern;

/** 
 * import of Nodes in InlineWiki-Format
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.extension.datatransfer.wiki
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class InlineWikiImporter extends WikiImporter {

    protected static final String CONST_UE = "-";
    protected static final String CONST_LIST = "-";

    protected static final String CONST_PATTERN =
            "^[ ]*?([" + CONST_LIST + "]+)[ ]*(.*)";

    private static final Pattern CONST_WIKI =
            Pattern.compile(CONST_PATTERN, Pattern.UNICODE_CHARACTER_CLASS);

    protected int localId = 1;
    protected String strMetaIdPraefix = "Inline";
    
    /** 
     * Importer to import/parse nodes in Wiki-Format
     * @param options                the importoptions for the parser...
     * @param strMetaIdPraefix       default meta id praefix 
     */
    public InlineWikiImporter(final ImportOptions options, final String strMetaIdPraefix) {
        super(options);
        this.strMetaIdPraefix = strMetaIdPraefix;
    }

    @Override
    protected void initNodeData(final BaseNode curNode) throws Exception {
        // set metadata to local values so that they change not the global scope
        if (MetaData.class.isInstance(curNode)) {
            MetaData metaCurNode = (MetaData) curNode;
            metaCurNode.setMetaNodeNummer(new Integer(localId++).toString());
            metaCurNode.setMetaNodePraefix(strMetaIdPraefix);
        }
        // initsysdata
        curNode.initSysData(true);
    }

    @Override
    public Pattern getWikiPattern() {
        return CONST_WIKI;
    }

    @Override
    public String getWikiCharUe() {
        return CONST_UE;
    }
    
    @Override
    public String getWikiCharList() {
        return CONST_LIST;
    }
}
