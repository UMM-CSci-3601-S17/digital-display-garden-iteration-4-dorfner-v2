## UMM CSci 3601 Lab 4: Mongo, Spark, etc.
[![Build Status](https://travis-ci.org/UMM-CSci-3601/3601-lab4_mongo.svg?branch=master)](https://travis-ci.org/UMM-CSci-3601/3601-lab4_mongo)

In this lab, you'll be working to re-implement the ToDO API, this time pulling
data from a Mongo Database rather than a flat JSON file. You will also be implementing
a new *summary* API which returns results of processing ToDO data. 
You will also be implementing a simple client-side application to view this data.

## Setup

As in the previous lab, you'll be using IntelliJ. Once you've all joined your
group using GitHub classroom, you can clone your repository using IntelliJ:

- When prompted to create a new IntelliJ project, select **yes**.
- Select **import project from existing model** and select **Gradle.**
  - Make sure **Use default Gradle wrapper** is selected.
- Click **Finish.**

:bangbang: :fire: If IntelliJ ever prompts you to compile typescript files into
javascript **say no!**. Doing this will confuse webpack and break the client
side of your project during build. No permanent damage will be done, but it's
pretty annoying to deal with.

## Running your project

- The familiar **run** Gradle task will still run your SparkJava server.
(which is available at ``localhost:4567``)
- The **build** task will still _build_ the entire project (but not run it)
- The **runClient** task will build and run the client side of your project (available at ``localhost:9000``)

The major difference between this lab and lab #3 is that, here, your data
(users and todos) will be stored in a database rather than as "flat" JSON files
within the server source code.

For the most part, you will be using a local installation of Mongo as a
test database. You don't *really* need to worry about how this is set up,
but you *do* need to know a couple of tricks to help you use it:

- To load new seed data into your local test database, use the gradle task:
**seedMongoDB**.
- *Seed* data is stored in the aptly named JSON files.

## Testing and Continuous Integration

This things are mostly the same as they were in Lab #3.

Turn on your repo in [Travis CI][travis], replace the build status image in this README, and push your changes. That will trigger a build with Travis.

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
