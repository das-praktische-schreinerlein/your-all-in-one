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
 * interface for DataDomain: PlanChildrenSum (Start, Ende, Aufwand of me 
 * and all children) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface PlanChildrenSumData extends DataDomain {
    int CONST_ORDER = 85;

    Date getPlanChildrenSumStart();
    void setPlanChildrenSumStart(Date planStart);
    Date getPlanChildrenSumEnde();
    void setPlanChildrenSumEnde(Date planEnde);
    Double getPlanChildrenSumAufwand();
    void setPlanChildrenSumAufwand(Double planAufwand);

    void resetPlanChildrenSumData();
}
