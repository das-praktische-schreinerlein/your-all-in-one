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
package de.yaio.datatransfer.importer.parser;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.datatransfer.importer.ImportOptions;
import de.yaio.datatransfer.importer.NodeFactory;

/** 
 * service-functions for parsing of dataDomain: DataDomain
 * 
 * @FeatureDomain                import
 * @package                      de.yaio.datatransfer.importer.parser
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseDataParserImpl  extends ParserImpl implements BaseDataParser {

    @Override
    public Class<?> getTargetClass() {
        return DataDomain.class;
    }

    @Override
    public int getTargetOrder() {
        return DataDomain.CONST_ORDER;
    }

    @Override
    public int parseFromName(final DataDomain node, final ImportOptions options) throws Exception {
        return parseBaseDataFromName(node, options);
    }

    @Override
    public int parseBaseDataFromName(final DataDomain node, final ImportOptions options) throws Exception {

        return 0;
    }

    /** 
     * hängt den Parser für das spätere Extrahieren der NodeDaten aus dem 
     * Namen (NodeFactory.parseNodeDataDomains) in die Parserliste
     * @param nodeFactory            instance of the nodeFactory which will use the parser
     */
    public static void configureDataDomainParser(final NodeFactory nodeFactory) {
        nodeFactory.addDataDomainParser(new BaseDataParserImpl());
    }

}
