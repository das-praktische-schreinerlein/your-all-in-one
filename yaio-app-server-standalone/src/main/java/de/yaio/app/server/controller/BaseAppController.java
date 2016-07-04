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

import de.yaio.app.config.YaioConfiguration;
import de.yaio.app.datatransfer.common.ConverterException;
import de.yaio.app.utils.config.ConfigurationOption;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

/** 
 * controller to publish the baseapp
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Controller
@RequestMapping("/yaio-explorerapp/yaio-explorerapp.html")
public class BaseAppController {
    
    @Autowired
    protected ConverterUtils converterUtils;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(BaseAppController.class);

    /** 
     * Request to read the yaioApp-html-format with all Sys1-children
     * @param response               the response-Obj to set contenttype and headers
     * @return                       String - yaioApp-html-format of the node
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public String exportNodeAsYaioApp(final HttpServletResponse response) throws ConverterException {
        String sysUID = ConfigurationOption.stringValueOf(YaioConfiguration.getInstance().getProperty(
                YaioConfiguration.CONST_PROPNAME_YAIOEXPORT_STATIC_MASTERID));
        return converterUtils.commonExportNodeAsYaioApp(sysUID, response,
                "/static/yaio-explorerapp/yaio-explorerapp.html", false);
    }

    @ExceptionHandler(ConverterException.class)
    public String handleCustomException(final HttpServletRequest request, final ConverterException e,
                                        final HttpServletResponse response) {
        LOGGER.info("ConverterException while running request:" + request.toString(), e);
        response.setStatus(SC_BAD_REQUEST);
        return "cant serve baseApp => " + e.getMessage();
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String handleAllException(final HttpServletRequest request, final Exception e,
                                     final HttpServletResponse response) {
        LOGGER.warn("error while running request:" + request.toString(), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return "cant serve baseApp";
    }
}
