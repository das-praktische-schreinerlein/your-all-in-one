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

import de.yaio.app.utils.db.PersistentIntEnumable;

/**
 * enum to define the workflows and their corresponding integer-value (for hibernate and state-calculation)
 */
public enum WorkflowState implements PersistentIntEnumable {
    // the order is important for the calculation of workflow!!!!
    NOWORKFLOW(10),

    NOTPLANED(20), CANCELED(30), DONE(40),

    OPEN(50), CONFIRMED(60),

    RUNNING(70),

    LATE(80), WARNING(90);

    private Integer value;

    WorkflowState(Integer value) {
        this.value = value;
    }

    /**
     * get integer-value for workflowstate
     * @return      Integer-value of the workflowstate
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * get workflowstate for value
     * @param value value to convert to state
     * @return      workflowstate of the Integer-value
     */
    public Enum getEnumFromValue(Integer value) {
        return PersistentIntEnumable.EnumableHelper.getEnumFromValue(this, value, null);
    }
}
