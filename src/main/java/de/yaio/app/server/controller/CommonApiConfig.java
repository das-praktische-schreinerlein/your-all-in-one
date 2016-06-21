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
package de.yaio.app.server.controller;

import de.yaio.app.server.restcontroller.NodeRestController;
import de.yaio.app.server.restcontroller.StatisticsController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/** 
 * common config options
 * 
 * @FeatureDomain                Configuration
 * @package                      de.yaio.server
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

    @Value(value = "${yaio.dms-client.yaio-metaextract-service.available}")
    public Boolean metaextractAvailable;

    @Value(value = "${yaio.dms-client.yaio-webshot-service.available}")
    public Boolean webshotAvailable;

    @Value(value = "${yaio.dms-client.yaio-dms-service.available}")
    public Boolean dmsAvailable;

    public String configApiVersion = ApiConfigController.API_VERSION;
    public String adminApiVersion = AdminController.API_VERSION;
    public String converterApiVersion = ConverterController.API_VERSION;
    public String exportApiVersion = ExportController.API_VERSION;
    public String importApiVersion = ExportController.API_VERSION;
    public String restApiVersion = NodeRestController.API_VERSION;
    public String statisticsApiVersion = StatisticsController.API_VERSION;
}
