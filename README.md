# Digital Display Garden
[![Build Status](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-3-dorfner.svg?branch=master)](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-3-dorfner)
Software Design S2017, Iteration 3, Team _dorfner_ 

This repository is a fork from [Iteration 2 , Team _grimaldi_](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-2-grimaldi).

## Running your project
The project assumes that you have Java 8, and Mongo installed on your machine. 
You may also need to install [yarn](https://yarnpkg.com/en/) to build for production.

The server uses the Mongo database named `test` is highly recommended to drop the 
database before attempting to use the server. To do this:

```
$ mongo
> use test
> db.dropDatabase()
> exit
```

There are two different places in the code where you must specify the
domain name for your installation. 

- `API_URL` in `client/webpack.prod.js`
- `API_URL` in  `server/src/main/java/umm3601/Server.java`

Note that the first is only relevant when doing a production build, 
and the second is only relevant for generating QR codes.

Before attempting to browse the visitor website, be sure to visit 
`http://yourdomain.com:YOURPORT/admin` and populate the database via 
that interface.

## Attempted or Completed Stories

### Stories/Features inherited from previous iterations
* Leave Comments About a Flower
* Allow a visitor to rate a flower
* Display counts of likes and dislikes on visitor website
* Display counts of comments on visitor website
* Generate QR Codes
* Export Feedback to a File (Only plant comments are done)
* Track the number of times a flower page is visited (Client-side not done)
* Import Excel Spreadsheet to Database

### Stories forIteration 3
- [x] Updating comment data to include following columns (3 jellybeans):
  - CommonName,
  - Cultivar,
  - Bed Location
- [x] Add an URL beneath QR-Code (_3 jellybeans_)
- [x] Export Plant Ratings (_2 jellybeans_)
   - Counts of Likes, Dislikes, Comments, and Page Views are all exported
- [x] Change like/dislike counter to a single number (_1 jellybean_)
- [x] Ability to Delete old Data (_1 jellybean_)
- [x] Add a footer to pages (_1 jellybean_)
- [x] (Epic) Create a homepage that consists of (_4 jellybeans_)
  - ~~QR-Code instructions~~, A general welcome message 
  - Ability to search by common name,
  - Bed Navigation

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
