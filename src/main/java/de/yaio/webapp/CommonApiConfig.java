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
package de.yaio.webapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.yaio.rest.controller.NodeRestController;
import de.yaio.webapp.controller.AdminController;
import de.yaio.webapp.controller.ApiConfigController;
import de.yaio.webapp.controller.ConverterController;
import de.yaio.webapp.controller.ExportController;


/** 
 * common config options
 * 
 * @FeatureDomain                Configuration
 * @package                      de.yaio.webapp
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Component
public class CommonApiConfig {
    @Value(value = "${yaio.plantUmlBaseUrl}")
    public String plantUmlBaseUrl;

    @Value(value = "${yaio.exportcontroller.plantUmlBaseUrl}")
    public String publicPlantUmlBaseUrl;

    @Value(value = "${yaio.exportcontroller.replace.baseref}")
    public String resBaseUrl;
    
    @Value(value = "${yaio.exportcontroller.replace.publicbaseref}")
    public String publicResBaseUrl;
    
    @Value(value = "${yaio.exportcontroller.excludenodepraefix}")
    public String yaioExportcontrollerExcludenodepraefix;
    
    @Value(value = "${yaio.knowninstances.name.1}")
    public String yaioInstanceName;
    
    @Value(value = "${yaio.knowninstances.url.1}")
    public String yaioInstanceUrl;

    @Value(value = "${yaio.knowninstances.desc.1}")
    public String yaioInstanceDesc;
    
    @Value(value = "${yaio.mastersysuid}")
    public String yaioMastersysuid;

    public String configApiVersion = ApiConfigController.API_VERSION;
    public String adminApiVersion = AdminController.API_VERSION;
    public String converterApiVersion = ConverterController.API_VERSION;
    public String exportApiVersion = ExportController.API_VERSION;
    public String importApiVersion = ExportController.API_VERSION;
    public String restApiVersion = NodeRestController.API_VERSION;
}
