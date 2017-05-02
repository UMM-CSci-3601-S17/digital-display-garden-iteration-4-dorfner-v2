# Google OAuth2.0 Credentials

This program uses Google's OAuth2.0 API to verify the identity
of people accessing adminstrative functions on the website. This
document walks you through setting up your account with Google to
allow us to use that API.

## Create a project

Log in to the [Google API Console](https://console.developers.google.com/).
(In all our testing, we used UMN email accounts to interact with the API Console.
Our accounts may have thus had more access on this site, but I _think_ that any
Gmail user can use these APIs as well).

The first thing to do after logging in is create a project. In upper left, you will
see a dropdown that says "Select a project". Clicking that will bring up a screen
with all your projects. In the upper left of that window, you can associate the project
with any organizations. Then on the right you can click the "+" button.

This takes you to yet another screen. You will be prompted for your "Project name".
This is an internal name, so use whatever. For the Location, leave
it at whatever it defaults to. Click "Create". You'll probably land on something that
_looks_ like an error page. Go ahead and select your newly created project from the
drop menu in the upper left.

## Create your Credentials

In the left-hand column, select "Credentials". You will then be prompted to with a
dropdown menu of what kind of credentials to create. We want "OAuth client ID" credentials
because we need to be able to ask users for their account information.

You'll then be prompted to configure the consent screen. Click the button to do so.

Nothing on the consent screen is particularily important from a technical perspective,
the product name is what administrators of the Digital Display Garden will see during
authentication, so make it a reasonable name (e.g. "Digital Display Garden").

You'll then be asked to choose an application type. Choose "Web Application".

They will ask for a name, and I don't think that it matters what you choose.

There are two fields that you have to fill out. The first is authorized JavaScript
Origins. Simply make this the public URL of your website. As far as I can tell, our
setup will work with *any* URL there, but might as well put ours.

The second field *is* important. First add `http://localhost:2538/callback`. You'll then
see that you can add additional entries. Add `https://yourdomainname/callback` as the second one.

At this point, we'll have to talk a bit about how our OAuth setup works. The idea is that
when we want to access something belonging to a Google user's account (e.g. what email
address do they have?), then we'll send them to URL under Google's control. When we do that,
we also ask Google to send them back to a certain URL when they are done. These two URLs
that we've added above is the list of URLs that you are telling Google you want to be
able to redirect back to. So even if someone steals your client ID and client secret, they
won't be able to redirect users to their site unless they also steal your Google account
user name and password and log in and change list.

After saving your changes, you will presented with your client ID and client secret
which you can copy for
later use.
