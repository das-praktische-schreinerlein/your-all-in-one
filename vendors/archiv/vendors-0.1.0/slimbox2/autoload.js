    // AUTOLOAD CODE BLOCK (MAY BE CHANGED OR REMOVED)
    if (!/android|iphone|ipod|series60|symbian|windows ce|blackberry/i.test(navigator.userAgent)) {
        jQuery(function($) {

            // Bilder einlesen
            var imgList = $("div.container-content-desc img:not([class]), #div_full img:not([class])");

            // OnClick loeschen
            imgList.each(function(){$(this).removeAttr('onclick')});

            // an Lightbox anfuegen
            imgList.slimbox(
                {/* Put custom options here */},
                function(el) {
                    // Bild zum Link einlesen
                    var img = el;

                    // Bild-Url
                    var url = img.src;
                    if (! url) {
                        // falls kein Bild
                        return null;
                    }

                    // Beschreibung erzeugen
                    var desc = img.getAttribute("diadesc");
                    if (! desc) {
                        desc = img.alt;
                    }

                    return [url, desc];
                },
                function(el) {
                    // Bild zum Link einlesen
                    var img = el;

                    // Bild-Url
                    var url = img.src;
                    if (! url) {
                        // falls kein Bild
                        return false;
                    }
                    return true;
                    //return (this == el) || ((this.rel.length > 0) && (this.rel == el.rel));
                }
            );
        });
    }
 