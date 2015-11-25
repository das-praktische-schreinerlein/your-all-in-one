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
package de.yaio.extension.dms.services;

import de.yaio.core.datadomain.ResLocData;

/** 
 * businesslogic for dataDomain: ResIndexData (index url/file to dms)
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.extension.dms
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface ResIndexDataService {

    /** the position in the recalc-order at which the recalcer will run */
    int CONST_RECALC_ORDER = 70;
    /** 
     * index the uploaded ResLoc-data to dms
     * @FeatureDomain                DMS
     * @FeatureResult                updates membervars -
     * @FeatureKeywords              DMS
     * @param node                   node
     * @throws Exception             parser/format-Exceptions possible
     */
    void indexResLoc(ResLocData node) throws Exception;
}
