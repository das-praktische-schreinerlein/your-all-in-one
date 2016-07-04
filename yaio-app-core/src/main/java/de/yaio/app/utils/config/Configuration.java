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


import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Configuration {
    protected Map<String, ConfigurationOption> args;
    protected Map<String, ConfigurationOption> options;
    protected Map<String, ConfigurationOption> properties;

    protected Configuration() {
        args = new LinkedHashMap<>();
        options = new LinkedHashMap<>();
        properties = new LinkedHashMap<>();
    }

    public void putArg(final ConfigurationOption option) {
        args.put(option.getName(), option);
    }

    public boolean hasArg(final String name) {
        return args.containsKey(name);
    }

    public ConfigurationOption getArg(final String name) {
        return args.get(name);
    }

    public ConfigurationOption getArg(int index) {
        return args.get(Integer.toString(index));
    }


    public ConfigurationOption getArg(final String name, final Object defaultValue) {
        if (args.containsKey(name)) {
            return args.get(name);
        }
        return new ConfigurationOption(name, defaultValue);
    }

    public Set getArgNames() {
        return args.keySet();
    }

    public void putArgs(String[] args) {
        int count = 0;
        for (String arg: Arrays.asList(args)) {
            this.putArg(new ConfigurationOption(new Integer(count++).toString(), arg));
        }
    }
    public void putCliOption(final ConfigurationOption option) {
        options.put(option.getName(), option);
    }

    public boolean hasCliOption(final String name) {
        return options.containsKey(name);
    }

    public ConfigurationOption getCliOption(final String name) {
        return options.get(name);
    }

    public ConfigurationOption getCliOption(final String name, final Object defaultValue) {
        if (options.containsKey(name)) {
            return options.get(name);
        }
        return new ConfigurationOption(name, defaultValue);
    }

    public Set getCliOptionNames() {
        return options.keySet();
    }

    public void putCliOptions(org.apache.commons.cli.Option[] cliOptions) {
        for (final org.apache.commons.cli.Option prop: Arrays.asList(cliOptions)) {
            if (!StringUtils.isEmpty(prop.getLongOpt())) {
                this.putCliOption(new ConfigurationOption(prop.getLongOpt(), prop.getValue()));
            }
            if (!StringUtils.isEmpty(prop.getOpt())) {
                this.putCliOption(new ConfigurationOption(prop.getOpt(), prop.getValue()));
            }
        }
    }



    public void putProperty(final ConfigurationOption option) {
        properties.put(option.getName(), option);
    }

    public boolean hasProperty(final String name) {
        return properties.containsKey(name);
    }

    public ConfigurationOption getProperty(final String name) {
        return properties.get(name);
    }

    public ConfigurationOption getProperty(final String name, final Object defaultValue) {
        if (properties.containsKey(name)) {
            return properties.get(name);
        }
        return new ConfigurationOption(name, defaultValue);
    }

    public Set getPropertieNames() {
        return properties.keySet();
    }

    public void putProperties(final Properties props) {
        for (final Object prop: props.keySet()) {
            this.putProperty(new ConfigurationOption(prop.toString(), props.get(prop)));
        }
    }

    public Properties propertiesAsProperties() {
        Properties props = new Properties();
        for (ConfigurationOption option: properties.values()) {
            props.put(option.getName(), option.getValue() == null ? "" : option.getValue());
        }
        return props;
    }

    public Properties optionsAsProperties() {
        Properties props = new Properties();
        for (ConfigurationOption option: options.values()) {
            props.put(option.getName(), option.getValue() == null ? "" : option.getValue());
        }
        return props;
    }

    public List<String> argsAsList() {
        List<String> retArgs = new ArrayList<>();
        for (ConfigurationOption option: args.values()) {
            retArgs.add(option.getStringValue());
        }
        return retArgs;
    }

    public void publishProperties() {
        for (ConfigurationOption option: properties.values()) {
            System.setProperty(option.getName(), option.getStringValue());
        }
    }
}
