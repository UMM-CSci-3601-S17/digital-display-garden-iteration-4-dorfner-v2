# Digital Display Garden
[![Build Status](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-4-dorfner-v2.svg?branch=master)](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-4-dorfner-v2)
Software Design S2017, Iteration 3, Team _dorfner_ 

This repository is a fork from [Iteration 3 , Team _dorfner_](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-3-dorfner).

## Running your project
The project assumes that you have Java 8, and Mongo installed on your machine. 
You may also need to install [yarn](https://yarnpkg.com/en/) to build for production.

The server uses the Mongo database named `test` so it is highly recommended to drop the 
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
* Update comment data to include:
    * Common name
    * Cultivar
    * Bed location
* Add an URL beneath QR-code
* Export plant rating
    * Counts of likes, dislikes, comments, and page views are all exported
* Change like/dislike counter to a single number
* Ability to delete old data
* Add a footer to pages
* (Epic) Create a homepage that consits of:
    * A general welcome message 
    * Ability to search by common name
    * Bed navigation

### Stories for Iteration 4
- [] Generate QR-code link for the homepage (_1 jellybean_)
- [] In the search results, group flowers by bed name then by cultivar (_2 jellybeans_)
- [] On the bed page, sort flowers by common name, then sort by cultivar (_2 jellybeans_)
- [] Export of visit timestamps of plants (_2 jellybean_)
- [] Add feature to cancel like/dislike (_3 jellybeans_)
- [] Ability for admin to selectively upload pictures for plants (_**8** jellybean_)
- [] Limit Admin page to authorized WCROC employees (_**10** jellybeans_)

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
* **Apache POI** is used for importing and exporting data in .xlsx format
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
