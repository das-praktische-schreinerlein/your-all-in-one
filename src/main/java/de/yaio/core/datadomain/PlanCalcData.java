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
package de.yaio.core.datadomain;

import java.util.Date;

/** 
 * interface for DataDomain: PlanCalc (calced Start, Ende) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface PlanCalcData extends DataDomain {
    int CONST_ORDER = 65;

    Date getPlanCalcStart();
    void setPlanCalcStart(Date planStart);
    Date getPlanCalcEnde();
    void setPlanCalcEnde(Date planEnde);
    String getPlanCalcCheckSum();
    void setPlanCalcCheckSum(String planCalcCheckSum);

    void resetPlanCalcData();
}
