# Roadmap

## short term

- **Code-Verbesserungen (YAIO2125)**
    - weitere Tests implementieren
    - Codeclean
    - Refactoring (Java, JS, CSS)

## mid term
- **Dokumentation (YAIO1143)**
    - Projektplanung (YAIO643)
    - Code-Dokumentation Prio2-Klassen (YAIO1145)
    - Modellierung (YAIO644)
- **Erweiterungen (YAIO1921)**
    - Neue Funktionen(YAIO2123)
        - Taskanlegen über Fixture-Import(YAIO2159)
        - Im/Export als JSON(YAIO2095)
        - Copy/Move über GUI+JSON(YAIO2096)
    - Ergonomie (YAIO1923)
        - Kontextmenü über Icon öffnen (YAIO2092)
        - neues Iconset als sprite einbinden (YAIO2093)
        - externe CSS-Styles definieren (YAIO1724)
        - DryRun-Modus für Import (YAIO1712)
        - OutputOptions im Cookie speichern (YAIO1674)
        - bei Anlegen/Löschen einer Task - Aufwand von Parent abziehen (YAIO1680)
        - Style-Switcher (anderes css mit Auswahl in Coockie) (YAIO1626)
        - Änderung von Metadaten ermöglichen (mit Check ob eindeutig in DB) (YAIO1470)
        - Form bei Hinweis ablehnen, Force-Option zum ueberschreiben (YAIO1542)
        - Hinweise im Formular einblenden (YAIO1543)
        - Verschärfung der Vailidierungsregeln (YAIO1924)
    - Funktionen (YAIO1575)
        - Tagen der Nodes mit TagEditor (YAIO1247)
    - Datenmodell (YAIO1686)
        - rekursive Aktualisierung CalcSum usw. (YAIO576)
        - Terminvererbung (YAIO1891)
- **Code-Verbesserungen (YAIO1926)**
    - Auswertung Checkstyle(YAIO2132)
    - Auswertung Findbugs(YAIO1979)
    - Java-Codeabdeckung für neue Java-Funktionen(YAIO2098)
    - Layout-Tests(YAIO2100)
    - Optionen in Config auslagern (YAIO1638)
    - eigene JS-Klassen (YAIO1927)
    - DB-Zugriffe von BaseNodeDBService machen und nicht von BaseNode (YAIO1553)
    - checksum+version per json uebertragen, beim speichern kontrollieren und ggf. Warnung (YAIO1541)
    - für Formular eigene Angular-Directive (Tag mit Layout, ganze Form-Elemente, ganze Forms) (YAIO1550)
    - autom Classloader fuer Formatter/Parser-Plugins (YAIO575)
    - Exceptions aufräumen (YAIO676)

## long term

- **Projekt - Configurator (YAIO1716)**
    - java ConfiguratorClass (YAIO1717)
    - Frontend - ConfigController (YAIO1718)
- **Projekt - User-Modul (YAIO1705)**
    - Datenmodell: Usertabelle (YAIO1706)
    - CRUD-Masken User (YAIO1707)
    - Node-User: Creator/Besitzer/LastChanger (YAIO1710)
    - Rechtemodell an Node Alle/Team/Privat (YAIO1711)
    - Mandantenmodell -> an User wird Mandant festgemacht, jede Node bekommt Mandant zugewiesen (YAIO1719)
- **Projekt - neue Funktionen (YAIO171)**
    - AbrechnungsTyp hinzufuegen (YAIO183)
    - Typ hinzufuegen (YAIO182)
    - neue Bereiche Alarm4Start, Alarm4End (YAIO240)
    - neue Bereiche Taskstati Sleep, Wait (YAIO243)
- **Projekt - Correspondence-Modul (YAIO1688)**
    - Einführung eines Correspondence-Modells mit Kontakten/Email/Tel... (YAIO1551)
    - neuer Typ Kontakt (YAIO246)
    - Dokumentenverwaltung (YAIO187)
        - Inbox mit allen möglichen Ressourcen (YAIO188)
        - Ressource verlinkt auf Ressource mit Detaildaten (analog Correspondence) (YAIO189)
        - Ressource wird versioniert analog Correspondence (YAIO190)
        - Ressourcen-Stammadten änderbar (YAIO191)
        - Ressourcen-Kontakte analog Correspondence (Sender, Empfänger ) usw. (YAIO192)
        - können mit Schlagworten versehen werden (YAIO193)
        - Verlinkung n:n Ressourcen mit Nodes (YAIO194)
        - Framelösung links Nodeliste, rechts Ressourcenliste und dann mit Drag&Drop) verlinken (YAIO195)
        - Emails, Telefonate, Chats werden als Thread versioniert und dargestellt (YAIO196)
        - Dokumente per Upload bzw. WebDav (YAIO197)
        - Email-Datei x.msg per Upload bzw. WebDav -> parse die Infos dort direkt raus (YAIO198)
        - Telefonate per WebForm/Email/Asterisk (YAIO199)
- **Projekt - als Android-App (YAIO1689)**
    - das ganze als lokale Android-App (YAIO1552)
- **Projekt - Zeiterfassung (YAIO200)**
    - An Node Kalender aufrufen, Tag auswählen und speichern wie viele Stunden ich an diesem Tag dort gearbeitet habe (YAIO201)
    - Im Kalender Tag aufrufen, aus Selectbox Node auswählen und Stunden angeben (YAIO202)
    - Datenmodell: Tag,Projekt,Mitarbeiter,Stunden (YAIO203)
