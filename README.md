## UMM CSci 3601 Lab 4: Mongo, Spark, etc.

In this lab, you'll be working to write a new todo API, using SparkJava,
which pulls todo data from a Mongo database (rather than from 'flat' files).
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

Now that the structure of the project is different, the way we run the project
is different too.

- The familiar **run** Gradle task will still run your SparkJava server.
(which is available at ``localhost:4567``)
- The **build** task will still _build_ the entire project (but not run it)

The major difference between this lab and lab #3 is that, here, your data
(users and todos) will be stored in a database rather than as "flat" JSON files
within the server source code.

In order to serve up the _client side_ of your project, you'll need to use the
**runClient** Gradle task. This will trigger the various tools in the
client side portion of the project to build and host your client side
application on their own little web-server, available by default at ``localhost:9000``.

## Testing and Continuous Integration

This things are mostly the same as they were in Lab #3.

Turn on your repo in [Travis CI][travis], replace the build status image in this README, and push your changes. That will trigger a build with Travis.

## Resources

- [What _is_ Angular 2... why TypeScript?][angular-2]
- [What _is_ webpack...?][whats-webpack]
- [Testing Angular 2 with Karma/Jasmine][angular2-karma-jasmine]

[angular-2]: https://www.infoq.com/articles/Angular2-TypeScript-High-Level-Overview
[angular2-karma-jasmine]: http://twofuckingdevelopers.com/2016/01/testing-angular-2-with-karma-and-jasmine/
[labtasks]: LABTASKS.md
[travis]: https://travis-ci.org/
[whats-webpack]: https://webpack.github.io/docs/what-is-webpack.html
