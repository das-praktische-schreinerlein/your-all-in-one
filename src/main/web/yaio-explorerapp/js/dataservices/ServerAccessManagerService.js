/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for data-services
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
Yaio.ServerAccessManagerService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.AccessManagerService(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        // urls
        me.setAvailiableNodeAction('show', me.config.restShowUrl);
        me.setAvailiableNodeAction('createsymlink', me.config.restSymLinkUrl);
        me.setAvailiableNodeAction('edit', me.config.restUpdateUrl);
        me.setAvailiableNodeAction('create', me.config.restCreateUrl);
        me.setAvailiableNodeAction('mode', me.config.restMoveUrl);
        me.setAvailiableNodeAction('remove', me.config.restRemoveUrl);
        me.setAvailiableNodeAction('search', me.config.restSearchUrl);
        me.setAvailiableNodeAction('syshelp', me.config.restExportsBaseUrl + 'documentation/SysHelp1');
        me.setAvailiableNodeAction('sysinfo', me.config.restExportsBaseUrl + 'documentation/SysInfo1');
        me.setAvailiableNodeAction('frontpagebaseurl', me.config.restExportsBaseUrl + 'htmlfrontpagefragment/');
        me.setAvailiableNodeAction('logout', "#/logout");
//        me.setAvailiableNodeAction('logout', me.config.restLogoutUrl);
        
        // flags
        me.setAvailiableNodeAction('showsysdata', true);
        me.setAvailiableNodeAction('print', true);

        // import-forms
        me.setAvailiableImportForm('ImportWiki', '/imports/wiki/');

        // export-forms
        me.setAvailiableExportForm('ExportWiki', me.config.restExportsBaseUrl + 'wikiuseoptions/');
        me.setAvailiableExportForm('ExportMindmap', me.config.restExportsBaseUrl + 'mindmapuseoptions/');
        me.setAvailiableExportForm('ExportICal', me.config.restExportsBaseUrl + 'icaluseoptions/');
        me.setAvailiableExportForm('ExportHtml', me.config.restExportsBaseUrl + 'htmluseoptions/');
        me.setAvailiableExportForm('ExportExcel', me.config.restExportsBaseUrl + 'exceluseoptions/');
        me.setAvailiableExportForm('ExportCsv', me.config.restExportsBaseUrl + 'csvuseoptions/');

        // export-links
        me.setAvailiableExportLink('ExportYaioAppDirect', me.config.restExportsBaseUrl + 'yaioapp/');
        me.setAvailiableExportLink('ExportWikiDirect', me.config.restExportsBaseUrl + 'wiki/');
        me.setAvailiableExportLink('ExportMindmapDirect', me.config.restExportsBaseUrl + 'mindmap/');
        me.setAvailiableExportLink('ExportHtmlDirect', me.config.restExportsBaseUrl + 'html/');
        me.setAvailiableExportLink('ExportHtmlDocumentationDirect', me.config.restExportsBaseUrl + 'documentation/');
        me.setAvailiableExportLink('ExportICalDirect', me.config.restExportsBaseUrl + 'ical/');
        me.setAvailiableExportLink('ExportICalEventsDirect', me.config.restExportsBaseUrl + 'icalevents/');
        me.setAvailiableExportLink('ExportICalTasksDirect', me.config.restExportsBaseUrl + 'icaltasks/');
        me.setAvailiableExportLink('ExportICalTasksTodoDirect', me.config.restExportsBaseUrl + 'icaltaskstodo/');
        me.setAvailiableExportLink('ExportICalTasksLateDirect', me.config.restExportsBaseUrl + 'icaltaskslate/');
        me.setAvailiableExportLink('ExportExcelDirect', me.config.restExportsBaseUrl + 'excel/');
        me.setAvailiableExportLink('ExportCsvDirect', me.config.restExportsBaseUrl + 'csv/');
        me.setAvailiableExportLink('ExportJsonDirect', me.config.restExportsBaseUrl + 'json/');
        me.setAvailiableExportLink('ExportPplDirect', me.config.restExportsBaseUrl + 'ppl/');
    };

    me._init();
    
    return me;
};
