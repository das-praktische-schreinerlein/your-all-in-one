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

'use strict';

/**
 * <h4>FeatureDomain:</h4>
 *     WebGUI
 * <h4>FeatureDescription:</h4>
 *     servicefunctions for exported documentations
 *      
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

/*****************************************
 *****************************************
 * ervicefunctions for exported documentations
 *****************************************
 *****************************************/

/*
 * ###########################
 * # PrintLayout
 * ###########################
 */
function yaioAppBase.get('YaioLayoutService').togglePrintLayout() {
    if ($("#checkboxPrintAll").prop('checked')) {
        // print all
        $("#link_css_dataonly").attr("disabled", "disabled");
        $("#link_css_dataonly").prop("disabled", true);
    } else  {
        // print data only
        $("#link_css_dataonly").removeAttr("disabled");
        $("#link_css_dataonly").prop("disabled", false);
    }
}

/*
 * ###########################
 * # SpeechSynth
 * ###########################
 */
 function openSpeechSynth() {
     var target = document.getElementById('div_full');
     if (target == null) target = self;
     target.focus();
     var speechsynthWindow = window.open('/yaio-explorerapp/speechsynth.html', "speechsynth", "width=690,height=350,resizable=yes,dependent=yes,scrollbars=yes");
     speechsynthWindow.focus();
     if (speechsynthWindow.opener == null) { speechsynthWindow.opener = self; }
     speechsynthWindow.opener.targetElement = target;
 }

/*
 * ###########################
 * # Kalkulation
 * ###########################
 */
const CONST_FUNCNAME_SUM = "SUM";

function calcColumns(nodeTDId, functionName, praefix, suffix) {
   // Variablen belegen
   var errMsg = null;
   var elemNodeTD = null;
   var idxCol = null;
   var elemNodeTABLE = null;
    
   // Parameter checken und Eelemente einlesen
   
   // TD-Element einlesen
   if (! errMsg && ! functionName) { 
       errMsg = "Parameter functionName required"; 
   } else {
       if (functionName != CONST_FUNCNAME_SUM) {
          errMsg = "Parameter functionName must be [" + CONST_FUNCNAME_SUM + "]"; 
       }
   }
   if (! errMsg && ! nodeTDId) { 
       errMsg = "Parameter nodeTDId required"; 
   } else {
       elemNodeTD = $("#" + nodeTDId);
   }
   
   // TABLE-Element einlesen
   if (! errMsg && ! elemNodeTD) { 
       errMsg = "HTMLElement nodeTDId not found"; 
   } else {
       idxCol = elemNodeTD.attr('cellIndex');
       elemNodeTABLE = $(elemNodeTD).closest('table');
   }

   // IDX einlesen
   if (! errMsg && ! elemNodeTABLE) { 
       errMsg = "HTMLElement elemNodeTABLE not found"; 
   } else {
       idxCol = elemNodeTD[0].cellIndex;
   }
   if (! errMsg && ! idxCol) { 
       errMsg = "idxCol not found"; 
   }
   
   // abschließender Fehlercheck
   if (errMsg) {
      window.alert("Fehler aufgetreten: " + errMsg);
      return null;
   }
   
   // Funktionalitaet: alle x-Spalten iterieren und Zahlen extrahieren
   var filterTD = "td:nth-child(" + (idxCol + 1) + ")";
   var numbers = new Array();
   $(elemNodeTABLE).children("tbody").children("tr").children(filterTD).each(function (i) {
       var col = $(this);

       // mich selbst herauslassen 
       if (col.attr('id') == nodeTDId) {
          // Jquery continue;
          //alert("SKIP NODEID: Zeile:" + (i+1) + " ID:" + col.id + " Content:" + col.html() + " Number" + number);
          return;
       }

       // normalisieren
       var strNumber = col.html();
       strNumber = strNumber.replace(/./g, "");
       strNumber = strNumber.replace(/,/, ".");
       
       // auf Zahl testen
       var number = parseFloat(strNumber, "10");
       if (isNaN(number)) {
          // Jquery continue;
          //alert("SKIP NAN: Zeile:" + (i+1) + " ID:" + col.id + " Content:" + col.html() + " Number" + number);
          return;
       }
       
       // an Zahlen anhaengen
       //alert("ADD NUMBER: Zeile:" + (i+1) + " ID:" + col.id + " Content:" + col.html() + " Number" + number);
       numbers.push(number);
   });       
   
   // Funktionalitaet: gewünsche Aktion auf alle Zahlen ausführen
   var funcResult = 0;
   for (var index = 0; index < numbers.length; ++index) {
       // alert("funcResult +" +  numbers[index]);
       if (functionName == CONST_FUNCNAME_SUM) {
          funcResult += numbers[index]; 
       }
   }
   
   // Inhalt des Elements setzen
   //alert("funcResult:" + funcResult);
   var text = (praefix ? praefix : "") + funcResult + (suffix ? suffix : "");
   elemNodeTD.html(funcResult);
}

/*
 * ######################
 * # Volltextsuche
 * #######################
 */
function doAllNodeToggler(flgShow, minEbene) {
    try {
        // Bloecke Oeffnen/Schließen
        var toggleList = document.getElementsByClassName("blockToggler");
        if (toggleList.length > 0) {
            // Elemente vorhanden
            for (var j = 0; j < toggleList.length; j++) {
                // Elemente iterieren
                var element = toggleList[j];
                var toggleId = element.getAttribute('toggleId');
                var togglerBaseId = element.getAttribute('togglerBaseId');

                // Pruefen ob Node-Toggler
                if (toggleId && toggleId.search("childrencontainer") > 0) {
                    // Ebene einlesen
                    var toggleBlock = document.getElementById(toggleId);
                    var curEbene = 0;
                    if (toggleBlock) {
                        curEbene = toggleBlock.getAttribute('data-pjebene');
                    }

                    var effect = function(){
                        // Leertoggler
                        var togEf = new ToggleEffect(toggleId); 
                        togEf.slideAniLen = 1; 
                        togEf.doEffect();
                    };
                    if (flgShow) {
                        // Block zeigen
                        if (minEbene && curEbene && curEbene < minEbene) {
                            jMATService.getLayoutService().togglerBlockShow(
                                togglerBaseId, toggleId, effect);
                        } else {
                            jMATService.getLayoutService().togglerBlockHide(
                                togglerBaseId, toggleId, effect);
                        }
                    }
                    else {
                        // Block verbergen
                        if (minEbene && curEbene && curEbene > minEbene) {
                            jMATService.getLayoutService().togglerBlockHide(
                                togglerBaseId, toggleId, effect);
                        } else {
                            jMATService.getLayoutService().togglerBlockShow(
                                togglerBaseId, toggleId, effect);
                        }
                    }
                }
            }
        }
    } catch (e) {
        // anscheinend  nicht definiert
        window.alert(e);
    }
}

function doParentNodeToggler(myId, flgShow) {
    try {
        // Parent-Container einlesen (Child)
        var parents = $("#" + myId).parents();
        if (parents) {
            parents.map( 
                function () {
                    // Toggler visible setzen, wenn gefunden
                    var parentId = $(this).attr("id");
                    if (parentId) {
                        var nodeIdMatcher = parentId.match(/node_(.*)_childrencontainer/);
                        if (nodeIdMatcher && nodeIdMatcher.length > 0) {
                            // Toggler aktivieren
                            var togglerId = parentId;
                            if (! $(this).is(":visible")) {
                                jMATService.getLayoutService().togglerBlockShow(
                                    togglerId, togglerId, function () { 
                                        var togEf = new ToggleEffect(togglerId); 
                                        togEf.slideAniLen = 1; 
                                        togEf.doEffect();
                                    }
                                 );
                            }

                            // Element anzeigen
                            var nodeId = nodeIdMatcher[1];
                            var master = $("#node_" + nodeId + "_master");
                            if (master && ! $(master).is(":visible") ) {
                                $(master).show();                                
                            }
                        }
                    }
                }
            );
        }
    } catch (e) {
        // anscheinend  nicht definiert
        window.alert(e);
    }
}


/* Um einen Volltext-Treffer zu haben, müssen alle Worte im durchsuchten Text vorkommen. */
function VolltextTreffer(inhalt, suchworte) {
    // Wenn keine Suchzeichenkette als gefnden kennzeichnen
    if ( suchworte.length == 0 )
        return true;
 
    // alle Suchworte iterieren
    for (var i = 0; i < suchworte.length; i++) {
        if ( inhalt.indexOf(suchworte[i]) == -1) {
            // Wort nicht gefunden
            return false;
        }
    }
 
    // alle Worte gefunden
    return true;
}

function doSearch(suchworte) {
    // Suche auf alle Node-Elemente ausführen
    $(".node-block").each(
        function(index, value) {
            var flgFound = false;
            
            // Datenelemente konfigurieren
            var searchElements = new Array();
            searchElements.push($("#" + $(value).attr("id") + " > div:eq(1)").attr("id")); // Desc
            searchElements.push($("#" + $(value).attr("id") + " > div:first > div:first > div:first > div:eq(1)").attr("id")); // Name
            searchElements.push($("#" + $(value).attr("id") + " > div:first > div:eq(1)").attr("id")); // Status 

            // alle Datenelemente iterieren
            $.each(searchElements,
                function(subIndex, subId) {
                    // Inhalt auslesen
                    var inhalt = $("#"+subId).text().toLowerCase();
                    
                    // Volltextsuche
                    if (VolltextTreffer(inhalt, suchworte)) {
                        // wenn geufndne: verlassen;
                        flgFound = true;
                        return;
                    }
                }
             );
            
            // Elemente je nach Status der SubElemente ein/ausblenden
            if (flgFound) {
                if ( !$(value).is(":visible") ) {
                    // Element aktivieren
                    $(value).show();
                }  

                // Eltern oeffnen
                doParentNodeToggler($(value).attr("id"), true);
            } else {
                // Element deaktivieren
                $(value).hide();
            }
        }
    );
}

function startSearch() {
    // Suchworte auslesen
    var suchworte = $(".volltextsuchfeld").val().toLowerCase().split(" ");

    // alle Toggler schließen
    doAllNodeToggler(false, 0);
    
    // Suche ausfuehren
    doSearch(suchworte);
}

function resetSearch() {
    // Suchworte leeren
    $(".volltextsuchfeld").val('');
    
    // suche ausfuehren
    startSearch();
}

function initSearch() {
    // Volltextsuche
    $(".volltextsuchfeld").keyup(
        function(event) {
            // nur ausfuehren wenn enter
            if(event.keyCode != 13) {
               return;
            }
            
            // Suche ausfuehren
            startSearch();
        }
    );
}

/*
 * ######################
 * # Symlink
 * #######################
 */
function openNode(nodeId) {
    var masterId = "node_" + nodeId + "_master";
    
    // Node aktivieren
    $("#" + masterId).show();
    
    // openParent-Nodes
    doParentNodeToggler(masterId, true);
    
    // animated ScrollTo after intervall, because of time used for ToggleEffects
    var delayedFocus = window.setInterval(
        function() {
            // nach 0,1 Sekunden ausfuehren und Intervall loeschen
            $('html, body').animate({
                scrollTop: $("#" + masterId).offset().top
                }, 
                1000);
            window.clearInterval(delayedFocus);
        }, 
        100
    );
}

