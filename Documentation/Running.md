# Running / Building 

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

There are two different places in the project where you must specify the
domain name for your installation. 

The first is in `client/webpack.prod.js` and is only relevant when
doing a production build.

The second place is in your `config.properties` file. See
[ServerConfiguration.md](./ServerConfiguration.md) for details.

Before attempting to browse the visitor website, be sure to visit 
`http://yourdomain.com:YOURPORT/admin` and populate the database via 
that interface.