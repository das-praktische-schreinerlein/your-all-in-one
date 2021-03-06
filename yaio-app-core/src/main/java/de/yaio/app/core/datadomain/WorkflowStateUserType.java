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

import de.yaio.app.utils.db.PersistentIntEnumUserType;

/**
 * support mapping of WorkflowState to hibernate
 */
public class WorkflowStateUserType extends PersistentIntEnumUserType<WorkflowState> {
    public WorkflowStateUserType() {
        super(WorkflowState.class);
    }

    @Override
    public Class<WorkflowState> returnedClass() {
        return WorkflowState.class;
    }
}
