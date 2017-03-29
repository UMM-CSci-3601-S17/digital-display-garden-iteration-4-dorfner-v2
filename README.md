## Digital Display Garden
[![Build Status](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-2-grimaldi.svg?branch=master)](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-2-grimaldi)
Software Design S2017, Iteration 2, Team Grimaldi 

## Setup

Cloning the project inside IntelliJ:

- When prompted to create a new IntelliJ project, select **yes**.
- Select **import project from existing model** and select **Gradle.**
  - Make sure **Use default Gradle wrapper** is selected.
- Click **Finish.**

:bangbang: :fire: If IntelliJ ever prompts you to compile typescript files into
javascript **say no!**. Doing this will confuse webpack and break the client
side of your project during build. No permanent damage will be done, but it's
pretty annoying to deal with.

When you load the project on a new machine, tell Gradle to Refresh linked Gradle projects.

## Libraries used
###Client-Side
* **Angular 2**
* **Jasmine**and**Karma** 

###Server-Side
* **Java**
* **Spark**is used for the server operations
* **JUnit**is used for testing
* **Apache**is used for importing and exporting data in .xlsx format
* **zxing**is used for generating (supports reading it we want) QR codes
* **joda**TODO:why?

###Source of branches at end of Iteration 2 

Most of these branches can be deleted in the next Iterations' forks of this repo.

* **fix-tests**very behind, fixed tests at beginning of iteration
* **comments**Implements capability for comments
* **rating-flowers**Implements capability for flower likes and dislikes
* **improving-deployment**changes API_URL
* **chg-dialog**Changes plant information to be displayed from DialogComponent to PlantComponent
* **rm-unnecessary**Removes unnecessary files, imports, config from files
* **count**Implement the ability to count and display count for feedback on a plant
* **bed-pages**Creates pages for beds (BedComponent)
* **feedback-export**Implements ability to export comments to spreadsheet (xlsx)
* **QR-Generation**Implements ability to generate QRCodes and request a .zip from the server.
* **qr+text-export**A merge of QR-Generation and feedback-export with bug fixes  
* **fixing-deployment**
* **prettify**Makes things look nicer
* **testing-client**Tests
* **testing-PlantController**Tests
* **update-plantController-for-uploadIds**
* **improving-navigation**
* **master**

**Branches Inherited from Claude Arabo**

* **MakeMarkdownDocumentation** contains documentation that pertains to xlsx parsing, consider moving to master 
* **PlantController**
* **MergeFromMotherShip**
* **testXlsxToDatabase**
* **playingWithMongo**
* **xlsxIntoDatabase**


## Resources

- [Bootstrap Components][bootstrap]
- [Mongo's Java Drivers (Mongo JDBC)][mongo-jdbc]
- [What _is_ Angular 2... why TypeScript?][angular-2]
- [What _is_ webpack...?][whats-webpack]
- [Testing Angular 2 with Karma/Jasmine][angular2-karma-jasmine]

[angular-2]: https://www.infoq.com/articles/Angular2-TypeScript-High-Level-Overview
[angular2-karma-jasmine]: http://twofuckingdevelopers.com/2016/01/testing-angular-2-with-karma-and-jasmine/
[labtasks]: LABTASKS.md
[travis]: https://travis-ci.org/
[whats-webpack]: https://webpack.github.io/docs/what-is-webpack.html
[bootstrap]: https://getbootstrap.com/components/ 
[mongo-jdbc]: https://docs.mongodb.com/ecosystem/drivers/java/ 
