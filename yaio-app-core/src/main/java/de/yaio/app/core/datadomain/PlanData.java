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
package de.yaio.app.core.datadomain;

import java.util.Date;

/** 
 * interface for DataDomain: Plan (Start, Ende, Aufwand) of the Node
 * 
 * @FeatureDomain                DataDefinition
 * @package                      de.yaio.core.datadomain
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface PlanData extends DataDomain {
    int CONST_ORDER = 50;

    Date getPlanStart();
    void setPlanStart(Date planStart);
    Date getPlanEnde();
    void setPlanEnde(Date planEnde);
    Double getPlanAufwand();
    void setPlanAufwand(Double planAufwand);
    String getPlanTask();
    void setPlanTask(String planTask);

    void resetPlanData();
}
