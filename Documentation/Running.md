# Running / Building
This project assumes that you are running a fairly recent version of a common
Linux-based operating system such as Ubuntu or Fedora.

## External Dependencies
This project uses [Gradle](https://gradle.org/) for building and running. Because we include
the `gradlew` script in the project, you don’t need to install gradle separately. This script
 pulls in Gradle and then pulls in almost all the dependencies for the project, but
you will need the following external dependencies

- [MongoDB](https://www.mongodb.com/) : The project assumes that mongoDB is running locally and requires
  no authentication to access
- Java 8: This project requires Java 8 for all the server side code. All the development
  was done using OpenJDK, but our CI test suite was run on Oracle’s implementation.
- [Yarn](https://yarnpkg.com/en/): Gradle can automatically install its own version of yarn on some platforms,
  but requires manual installation on others.

## Building

- Use `git clone https://github.com/UMM-CSci-3601-S17/digital-display-garden-iteration-4-dorfner-v2 ddg` to
  get a copy of your repository.
- Use `cd ddg` to move into the directory you just cloned.
- In `client/webpack.prod.js` a variable `API_URL` is defined. The value should be the URL to your website
  followed by `/api/`. For example `https://ddg.mygarden.com/api/`. (Note the trailing slash!)
- This project uses Google OAuth services to authenticate users. Currently, the list of administrators is
   hardcoded in the constructor for 
   `server/src/main/java/umm3601/digitalDisplayGarden/Authentication/Auth.java`. 
   Add the address each of the people that need access and remove the addresses of those who don’t. Each
   address in the list _must_ be a Gmail address or a Gmail for Business address, otherwise they won’t be
   able to authentication through Google.
- Run `./gradlew build` to build the project. This will also run all the tests for the project some of
  which can take a lot of memory. If you wish to skip the tests, use `./gradlew assemble` instead.

## Running
- Run `cp server/build/distributions/server.tar ~` to copy the built version of your server to the home directory.
- Run `cd ~` to move into the home directory.
- Run `tar xvf server.tar` to extract your server tarball.
- Create a file called `config.properties`. See [ServerConfiguration.md](./ServerConfiguration.md) for what needs to go in the file. 
- Run `server/bin/server config.properties` to start the server. 

Our server does not have any HTTPS capabilities and will happily serve everything over HTTP. 
We _strongly_ recommend running this application behind a reverse proxy that provides HTTPS 
to clients. An example of how to create a reverse proxy using NGINX can be found [here](./Https.md).

After getting the site up and running, the first thing you will want to do is visit the admin
page and populate the database. See the end user documentation for the formatting requirements
on the file that you import.

## Development
The documentation above attempts to describe how to get things up and running in a production environment.
The project also has some Gradle tasks to make the development process easier.

### `./gradlew runClient`

Launches a simple node web server that serves the client-side app for the project. This server constantly 
monitors your client files and restarts itself to reflect those changes. It also maintains a connection to 
all connected clients and sends a signal to cause them to reload after it restarts. It listens for connections
on port `9000`. 

### `./gradlew run`

This launches the Spark server listening on port `2538` that provides all the server endpoints that
the client-side calls to interact with the database. During production, this server is responsible
for serving up the client-side app as well, but in development we have the server listening port `9000`
for that. If the server on port `2538` receives request for routes that are implemented on the
client-side (such as `/bed/6`) the server will throw an exception instead of passing it to the
client like it does during production. 

You will also need to provide a `config.properties` file to the server. The easiest way to do that is
to place it in `server/src`. The contents of this will be different than what you use during production.
See [ServerConfiguration.md](./ServerConfiguration.md) for more info.

### `./gradlew runClientTests`

Run all the client-side tests.

The server-side tests are all JUnit tests. We recommend
using the JUnit integration in your favorite IDE to run them.