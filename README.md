# Digital Display Garden
[![Build Status](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-2-grimaldi.svg?branch=master)](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-2-grimaldi)
Software Design S2017, Iteration 2, Team _Grimaldi_ 

This repository is a fork from [Iteration 1 , Team _Claude Arabo_](https://github.com/UMM-CSci-3601-S17/digital-display-garden-iteration-1-claudearabo).

## Setup

Cloning the project inside IntelliJ:

- When prompted to create a new IntelliJ project, select **yes**.
- Select **import project from existing model** and select **Gradle.**
  - Make sure **Use default Gradle wrapper** is selected.
- Click **Finish.**

:fire: If IntelliJ ever prompts you to compile typescript files into
javascript **say no!**. Doing this will confuse webpack and break the client
side of your project during build. No permanent damage will be done, but it's
pretty annoying to deal with.

When you load the project on a new machine, tell Gradle to Refresh linked Gradle projects.

## Running your project
> Run the server  
> Run the client  

If you have data in the database from a previous version it would be
best to drop() the test database.
In order to populate the database
* Run the Server and Client
* go to localhost:9000/admin (or whatever ip/port the client is running on)
* Import the data set from the Excel spreadsheet (.xlsx)
* the liveUploadId will be set to the latest data set imported
* liveUploadId determines which set of data to refer to within the database.

## Attempted or Completed Stories
* Leave Comments About a Flower
* Allow a visitor to rate a flower
* Display counts of likes and dislikes on visitor website
* Display counts of comments on visitor website
* Extra: Add Bed Pages (required for QR Codes)
* Generate QR Codes
* Export Feedback to a File (Only plant comments are done)
* Track the number of times a flower page is visited (Client-side not done)
* Track the number of times a bed page is visited (Not attempted)

Finished by Claude Arabo
* Import Excel Spreadsheet to Database

## Documentation
* [Excel File Requirements](Documentation/ExcelFileRequirements.md)  
* [Excel Parser Documentation](Documentation/ExcelParser.md) 



## Libraries used
### Client-Side
* **Angular 2**
* **Jasmine** and **Karma** 

### Server-Side
* **Java** 
* **Spark** is used for the server operations
* **JUnit** is used for testing
* **Apache** is used for importing and exporting data in .xlsx format
* **zxing** is used for generating QR codes (supports reading them if we want) 
* **joda** is used for making an unique LiveUploadID

### Source of branches at end of Iteration 2 

Most of these branches can be deleted in the next Iterations' forks of this repo.

* **fix-tests** Very behind, fixed tests at beginning of iteration
* **comments** Implements capability for comments
* **rating-flowers** Implements capability for flower likes and dislikes
* **improving-deployment** Changes API_URL
* **chg-dialog** Changes plant information to be displayed from DialogComponent to PlantComponent
* **rm-unnecessary** Removes unnecessary files, imports, config from files
* **count** Implement the ability to count and display count for feedback on a plant
* **bed-pages** Creates pages for beds (BedComponent)
* **feedback-export** Implements ability to export comments to spreadsheet (xlsx)
* **QR-Generation** Implements ability to generate QRCodes and request a .zip from the server.
* **qr+text-export** A merge of QR-Generation and feedback-export with bug fixes  
* **fixing-deployment**
* **prettify** Makes things look nicer
* **testing-client** Tests
* **testing-PlantController** Tests
* **update-plantController-for-uploadIds**
* **improving-navigation**
* **master**

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
