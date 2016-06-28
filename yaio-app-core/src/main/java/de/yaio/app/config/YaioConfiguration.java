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
package de.yaio.app.config;

import de.yaio.app.utils.config.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * baseclass for configuration
 *
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
public class YaioConfiguration extends Configuration {

    /** property: file location exportcontroller.replacer.config **/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER =
                    "yaio.exportcontroller.replacerdef.location";
    /** property:  exportcontroller.replacer documentation pattern**/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_SRC =
                    "replacer.documentation.pattern";
    /** property:  exportcontroller.replacer documentation target**/
    public static final String CONST_PROPNAME_EXPORTCONTROLLER_REPLACER_DOCUMENTATION_TARGET =
                    "replacer.documentation.target";


    /** property:  yaioinstances.name **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_NAME =
                    "yaio.knowninstances.name";
    /** property:  yaioinstances.desc **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_DESC =
                    "yaio.knowninstances.desc";
    /** property:  yaioinstances.url **/
    public static final String CONST_PROPNAME_YAIOINSTANCES_URL =
                    "yaio.knowninstances.url";

    /** property: masterid to export for static datasource */
    public static final String CONST_PROPNAME_YAIOEXPORT_STATIC_MASTERID =
                    "yaio.staticdatasource.mastersysuid";


    // must be instantiated after LOGGER because it is used in constructor
    protected static final YaioConfiguration yaioinstance = new YaioConfiguration();

    /** the configured yaioInstances to allow XFrameHeader and to construct in sourceselector of app */
    protected final Map<String, Map<String, String>> knownYaioInstances = new LinkedHashMap<>();

    /** replacements to do after processing a node in documentation-context **/
    protected final Map<String, String> postProcessorReplacements =
                    new LinkedHashMap<>();

    protected YaioConfiguration() {
        super();
    }

    public static YaioConfiguration getInstance() {
        return YaioConfiguration.yaioinstance;
    }

    public Map<String, Map<String, String>> getKnownYaioInstances() {
        return knownYaioInstances;
    }

    public Map<String, String> getPostProcessorReplacements() {
        return postProcessorReplacements;
    }
}
