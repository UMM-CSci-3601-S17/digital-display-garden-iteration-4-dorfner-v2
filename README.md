# Digital Display Garden[![Build Status](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-4-dorfner-v2.svg?branch=master)](https://travis-ci.org/UMM-CSci-3601-S17/digital-display-garden-iteration-4-dorfner-v2)
Software Design S2017, Iteration 4, Team _dorfner-v2_  

This repository is a fork from [Iteration 3 , Team _dorfner_](https://github.com/UMM-CSci-3601-S17/digital-display-garden-iteration-3-dorfner).


## Introduction :tulip:
This project's main goal is to develop a web application that provides visitors 
the ability to leave feedback for a local horticulture garden, the West Central Research Outreach Center (WCROC).
The application includes an administration side that allows certain garden employees to manage the website, and a visitor side
that allows visitors to view all the beds in the garden, view the plants in those beds, and leave feedback about these plants.
It is designed in a way that would make it fairly easy to be used by other interested gardens. 

## Key features
### Administration
* Upload a spreadsheet of plants and populate a database
* Delete an existing spreadsheet from the database
* Export collected visitor feedback to a spreadsheet
    * Comments
    * Counts
        * Visits
        * Likes/Dislikes
        * Comments
    * Visit Timestamps
        * Hourly
        * Daily
* Download QR codes for each bed, and the homepage
* Upload photos to specific plant pages

### Visitor experience
* Homepage 
    * Searchbar 
    * Bed-list dropdown
    * Brief overview of the garden
    * Instructions on how to use the application
* Bed Pages
    * List of plants in the current bed
* Plant Pages
    * Name of plant
    * Uploaded picture
    * Ability to leave feedback on the plant
        * Like/Dislike
        * Leave a comment
    * Total number of interactions on this plant (Ratings + Comments)
* Footer
    * Name and address of the garden
    * Links to website and social media


## Running your project

See [Documentation/Running.md](./Documentation/Running.md).


## Documentation
See the [Administration Guide](./Documentation/AdministrationGuideforDigitalDisplayGarden.docx.docx)
for a brief overview of how to use the site after you have it up and running.

## Languages used
* **Typescript**
* **Javascript**
* **Java**

## Libraries used
### Client-Side
* **Angular 2**
* **Jasmine** and **Karma** 
* **Gradle**

### Server-Side
* **Spark** is used for the server operations
* **JUnit** is used for testing
* **Apache POI** is used for importing and exporting data in .xlsx format
* **zxing** is used for generating QR codes (supports reading them if we want) 
* **joda** is used for making an unique LiveUploadID
* **Nimbus** is used for OAuth
* **scribejava** is used for OAuth
* **MongoDB** is used as our Database
* **Gradle**

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


  
_This project was developed as a part of Software Design course at University of Minnesota, Morris._