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
package de.yaio.app.utils.config;


public class ConfigurationOption {
    protected String name;
    protected Object value;

    public ConfigurationOption(final String name, final Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getStringValue() {
        return value == null ? null : value.toString();
    }

    public String toString() {
        return "ConfigurationOption " + getName() + "=" + getValue();
    }

    public static Object valueOf(final ConfigurationOption option) {
        return option == null ? null : option.getValue();
    }

    public static String stringValueOf(final ConfigurationOption option) {
        return option == null ? null : option.getValue().toString();
    }

    public static Integer integerValueOf(final ConfigurationOption option) {
        return option == null ? null : new Integer(option.getValue().toString());
    }
}
