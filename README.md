# CISQ1: Lingo Trainer

[![Java CI](https://github.com/Nuuhtjee/cisq1-lingo/actions/workflows/build.yml/badge.svg)](https://github.com/Nuuhtjee/cisq1-lingo/actions/workflows/build.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Nuuhtjee_cisq1-lingo&metric=coverage)](https://sonarcloud.io/dashboard?id=Nuuhtjee_cisq1-lingo)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Nuuhtjee_cisq1-lingo&metric=ncloc)](https://sonarcloud.io/dashboard?id=Nuuhtjee_cisq1-lingo)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Nuuhtjee_cisq1-lingo&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Nuuhtjee_cisq1-lingo)



#Extra uitdagingen:

**Heroku**
[Heroku met continuous deployment](https://cisq1-lingo.herokuapp.com/)

Ik heb bij het project gebruik gemaakt van docker en om het toch te laten werken op heroku heb ik een aparte deployment database gemaakt en de credentials in "application-heroku.properties" gestopt. De testen in de Continuous Integration pipeline worden wel uitgevoerd op de mock H2 database.

**Line en branch coverage**
Line en branch coverage is ook 100%

Op github kun je de sonarcloud badge zien en anders de coverage rapports

**Custom exceptions**

Ik heb om de controller schoner te houden gebruik gemaakt van een ControllerAdvisor die al mijn exceptions vangt en afhandeld
