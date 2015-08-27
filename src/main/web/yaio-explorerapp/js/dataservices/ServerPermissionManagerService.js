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
Yaio.ServerPermissionManagerService = function(appBase, config, defaultConfig) {
    'use strict';

    // my own instance
    var me = Yaio.PermissionManagerService(appBase, config, defaultConfig);

    /**
     * initialize the object
     */
    me._init = function() {
        me.setAvailiableNodeAction('show', me.appBase.config.restShowUrl);
        me.setAvailiableNodeAction('createsymlink', me.appBase.config.restSymLinkUrl);
        me.setAvailiableNodeAction('edit', me.appBase.config.restUpdateUrl);
        me.setAvailiableNodeAction('create', me.appBase.config.restCreateUrl);
        me.setAvailiableNodeAction('mode', me.appBase.config.restMoveUrl);
        me.setAvailiableNodeAction('remove', me.appBase.config.restRemoveUrl);
        me.setAvailiableNodeAction('search', me.appBase.config.restSearchUrl);
        me.setAvailiableNodeAction('showsysdata', true);
        me.setAvailiableNodeAction('print', true);
        me.setAvailiableNodeAction('syshelp', me.appBase.config.restExportsBaseUrl + 'documentation/SysHelp1');
        me.setAvailiableNodeAction('sysinfo', me.appBase.config.restExportsBaseUrl + 'documentation/SysInfo1');

        me.setAvailiableImportForm('ImportWiki', '/imports/wiki/');

        me.setAvailiableExportForm('ExportWiki', me.appBase.config.restExportsBaseUrl + 'wikiuseoptions/');
        me.setAvailiableExportForm('ExportMindmap', me.appBase.config.restExportsBaseUrl + 'mindmapuseoptions/');
        me.setAvailiableExportForm('ExportICal', me.appBase.config.restExportsBaseUrl + 'icaluseoptions/');
        me.setAvailiableExportForm('ExportHtml', me.appBase.config.restExportsBaseUrl + 'htmluseoptions/');
        me.setAvailiableExportForm('ExportExcel', me.appBase.config.restExportsBaseUrl + 'exceluseoptions/');
        me.setAvailiableExportForm('ExportCsv', me.appBase.config.restExportsBaseUrl + 'csvuseoptions/');

        me.setAvailiableExportLink('ExportWikiDirect', me.appBase.config.restExportsBaseUrl + 'wiki/');
        me.setAvailiableExportLink('ExportMindmapDirect', me.appBase.config.restExportsBaseUrl + 'mindmap/');
        me.setAvailiableExportLink('ExportHtmlDirect', me.appBase.config.restExportsBaseUrl + 'html/');
        me.setAvailiableExportLink('ExportHtmlDocumentationDirect', me.appBase.config.restExportsBaseUrl + 'documentation/');
        me.setAvailiableExportLink('ExportICalDirect', me.appBase.config.restExportsBaseUrl + 'ical/');
        me.setAvailiableExportLink('ExportICalEventsDirect', me.appBase.config.restExportsBaseUrl + 'icalevents/');
        me.setAvailiableExportLink('ExportICalTasksDirect', me.appBase.config.restExportsBaseUrl + 'icaltasks/');
        me.setAvailiableExportLink('ExportICalTasksTodoDirect', me.appBase.config.restExportsBaseUrl + 'icaltaskstodo/');
        me.setAvailiableExportLink('ExportICalTasksLateDirect', me.appBase.config.restExportsBaseUrl + 'icaltaskslate/');
        me.setAvailiableExportLink('ExportExcelDirect', me.appBase.config.restExportsBaseUrl + 'excel/');
        me.setAvailiableExportLink('ExportCsvDirect', me.appBase.config.restExportsBaseUrl + 'csv/');
        me.setAvailiableExportLink('ExportJsonDirect', me.appBase.config.restExportsBaseUrl + 'json/');
        me.setAvailiableExportLink('ExportPplDirect', me.appBase.config.restExportsBaseUrl + 'ppl/');
    };

    me._init();
    
    return me;
};
