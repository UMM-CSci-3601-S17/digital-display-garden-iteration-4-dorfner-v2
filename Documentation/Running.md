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

There are two different places in the code where you must specify the
domain name for your installation. 

- `API_URL` in `client/webpack.prod.js`
- `API_URL` in  `server/src/main/java/umm3601/Server.java`

Note that the first is only relevant when doing a production build, 
and the second is only relevant for generating QR codes.

Before attempting to browse the visitor website, be sure to visit 
`http://yourdomain.com:YOURPORT/admin` and populate the database via 
that interface.