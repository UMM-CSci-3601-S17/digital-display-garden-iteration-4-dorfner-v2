# Server Configuration

We use a configuration file in the Properties file format
[(see this demo)](https://www.mkyong.com/java/java-properties-file-examples/)
to configure our server.

The server treats the first argument you pass it as a path to the configuration
file. If no arguments are passed it simply tries to read from a file called
`config.properties` in the current working directory. So for development,
you should create a file by that name in `server/`. Note that this file is
in `.gitignore` and thus won't be committed.


#### clientID

This is the Google OAuth2.0 Client ID. It is essentially
a "username" for their API, but should be kept secret I believe.
(TODO: document how to get one of these.)

#### clientSecret

This is the Google OAuth2.0 Client Secret. This is essentially
a "password" for their API, and MUST be kept secret.
(TODO: document how to get one of these.)

#### publicURL

This is the URL that the server thinks visitors will be accessing
it from. It is important for this to be correct for security reasons,
and for a couple other things that assume we can create URLs for
vistors. During development on your local machine, this would be
something like `http://localhost:9000`, but in production, it would
look something like `https://a.real.website.com`.

#### callbackURL

This is the URL to which Google sends users after we have authenticated
them. During development, it should look like `http://localhost:2538/callback`.
During production, it should be the same domain port as `publicURL`, but
with `/callback` on the end.
## Example file

Here is an example of what such a file would look like:
```
# Make sure there is no trailing white space at the end of any lines
# Google OAuth2.0 Client ID
clientID=verylongstringofrandomnumbersandletters.apps.googleusercontent.com
# Google OAuth2.0 Client Secret
clientSecret=notquitesuchalongrandomstring
# The public URL of the website
publicURL=http://localhost:9000
# The callback URL of the website
callbackURL=http://localhost:2538/callback
```
