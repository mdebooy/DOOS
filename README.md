DOOS
====

Dit project bevat de Administratie applicatie van mijn web applicaties. De Applicatie zorgt voor:
* De teksten in de taal van de gebruiker/browser.
* De rapporten in de taal van de gebruiker/browser en in het juiste formaat (PDF, ODT, ODS). Dit gebeurt met JasperReport.
* Opladen en onderhoud van de teksten.
* Opladen en onderhoud van de parameters.
* Opladen en onderhoud van de rapporten.
* Versturen van e-mails.

Het project bestaat uit 3 modules:

doos-components
---------------

Deze produceert een jar die in alle web applicaties moet worden toegevoegd.  Het zorgt ervoor dat de applicaties over hun teksten, parameters en rapporten kunnen beschikken.

doos-config
-----------

Deze produceert een jar met hierin alle bestanden die nodig zijn om met de applicatie te kunnen werken:
* `resources_XX.properties` bevat de teksten in taal `XX`.
* `parameters.properties` bevat de parameters voor de applicatie.
* `reports` directory bevat de rapporten voor de applicatie.
* `db` directory bevat de database scripts. Er is een sub-directory per database type. Scripts met de naam `XXXX-patch.sql` moeten enkel worden uitgevoerd als er wordt overgegaan naar een nieuwere versie. Sla geen versie over.

doos-web
--------

Dit is de eigenlijke applicatie. Zet het war-bestand in de `webapps` directory van Tomee. De eerste maal dat de applicatie gebruikt wordt zijn er geen teksten of parameters aanwezig. Laad ze in via de juiste menu opties. Van zodra ze geladen zijn worden ze in de applicatie gebruikt.

<hr />

This project contains the Administration application of my web applications. The application takes care of:
* The texts in the language of the user/browser;
* The reports in the language of the user/browser and in the right format (PDF, ODT, ODS). This is done with JasperReport;
* Loading and maintenance of the texts;
* Loading and maintenance of the parameters;
* Loading and maintenance of the reports.
* Sending of emails.

The project consists of thre modules:

doos-components
---------------

This produces a jar that must be used by all web applications. It takes care for the applications to have their texts, parameters and reports.

doos-config
-----------

This produces a jar that includes all files that are needed to work with the application:
* `resources_XX.properties` contains the texts in language `XX`;
* `parameters.properties` contains the paramleters for the application;
* `reports` directory contains the reports for the application;
* `db` directory contains the database scripts. There is a sub-directory per database type. Scripts the name `XXXX-patch.sql` should only be executed when it is for an upgrade from one version to another. Do not skip a version.

doos-web
--------

This is the application. Put the war-file in the `webapps` directory of Tomee. The first time that you use the application there will be no texts or parameters available. Load them through the right menu options. As soon as they are loaded they will be used by the application.
