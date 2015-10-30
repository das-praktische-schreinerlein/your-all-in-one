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
package de.yaio.core.dbservice;

import java.util.ArrayList;
import java.util.List;

/** 
 * implementation of dbfilter
 * 
 * @FeatureDomain                Persistence
 * @package                      de.yaio.core.node
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class DBFilter {
    /**
     * inner class for parameters
     */
    public static class Parameter {
        protected String name;
        protected Object value;
        /**
         * @param name                   name of the parameter
         * @param value                  value of the parameter
         */
        public Parameter(final String name, final Object value) {
            super();
            this.name = name;
            this.value = value;
        }

        /**
         * @return                       the {@link DBFilter.Parameter#name}
         */
        public final String getName() {
            return this.name;
        }
        /**
         * @return                       the {@link DBFilter.Parameter#value}
         */
        public final Object getValue() {
            return this.value;
        }
    }
    
    protected String sql;
    protected List<Parameter> parameters = new ArrayList<Parameter>();

    /**
     * @param sql                    sql of the filter
     * @param parameters             parametrs of the filter
     */
    public DBFilter(final String sql, final List<Parameter> parameters) {
        super();
        this.sql = sql;
        this.parameters = parameters;
    }

    /**
     * @return                       the {@link DBFilter#sql}
     */
    public final String getSql() {
        return this.sql;
    }
    /**
     * @return                       the {@link DBFilter#parameters}
     */
    public final List<Parameter> getParameters() {
        return this.parameters;
    }
}
