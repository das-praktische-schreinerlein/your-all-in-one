/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     overloads some functions from my JMAT-Framework
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
'use strict';


    /**
     * fuegt FormRowToggler an Elternelement an
     */
    JMATPageLayout.prototype.appendFormrowToggler = function(parentId, togglerBaseId, toggleClassName, label) {
        var html = jMATService.getPageLayoutService().createFormrowToggler(
                togglerBaseId, toggleClassName, 
                label + "<span class='icon-formrowtoggler icon-formrowtoggleron'>&nbsp;</span>", 
                label + "<span class='icon-formrowtoggler icon-formrowtoggleroff'>&nbsp;</span>", "", "");
        jMATService.getJMSServiceObj().appendHtml(html,parentId, "formrowToggler");
    };

    /**
     * show InputRow
     * @param eleInputRow
     * @returns {Boolean}
     */
    JMSLayout.prototype.showInputRow = function(eleInputRow) {
        if (! eleInputRow) {
           return false;
        }
        eleInputRow.style.display = "inline-block";
        return true;
    };

    /**
     * liefert Status eines Input-Elements
     * @param eleInput  INPUT-HTMLElement
     * return belegt trzue/false
     */
    JMSLayout.prototype.getStateInputElement = function(eleInput) {
        var state = false;

        if (eleInput.nodeName.toUpperCase() == "SELECT") {
            // Select-Box
            if (eleInput.value && (eleInput.value !== "search_all.php")) {
                state = true;
            } else {
                // Multiselect auswerten
                for (var i = 0; i < eleInput.length; i++) {
                    if (eleInput.options[i].selected && eleInput.options[i].value && (eleInput.options[i].value !== "search_all.php")) {
                        state = true;
                        i = eleInput.length + 1;
                    }
                }
            }
        } else if (eleInput.nodeName.toUpperCase() == "INPUT") {
           // Element als Radio/Checkbox suchen
           if (eleInput.type.toUpperCase() == "RADIO") {
              if (eleInput.checked) {
                  state = true;
              }
           } else if (eleInput.type.toUpperCase() == "CHECKBOX") {
              if (eleInput.checked) {
                  state = true;
              }
           } else if (eleInput && eleInput.value) {
              // normales Eingabefeld
              state = true;
           }
        } else if (eleInput.nodeName.toUpperCase() == "TEXTAREA") {
            // Element als textarea suchen
            if (eleInput && eleInput.value) {
               // normales Eingabefeld
               state = true;
            }
         }

        return state;
    };

    
    JMSLayout.prototype.isInputRowsSet = function(className) {
        // InputRows anhand des Classnames abfragen
        var lstInputRows = this.getInputRows(className);
        if (! lstInputRows || lstInputRows.length <= 0) {
           return null;
        }
        var state = false;

        // alle InputRows iterieren
        for (var i = 0; i < lstInputRows.length; ++i){
           // InputRow verarbeiten
           var eleInputRow = lstInputRows[i];
           state = state || this.getState4InputRow(eleInputRow);
           console.log("state=" + state + " for " + eleInputRow.id);
        }
        return state;
     };
    