# Using HTTPS with our server

This document describes how one can use NGINX as a reverse proxy in
front our server to provide HTTPS and and redirect plain HTTP requests
to HTTPS. These steps assume that you are running Ubuntu 16.04.2 LTS,
but should be fairly easy to adapt to similar operating systems. This 
document also assumes that you will use [certbot](https://certbot.eff.org/)
 to get certificates from Let's Encrypt. You should configure certbot 
 to automatically renew these certificates before they expire, but 
 we don't document that here.

You will need to be logged in as root on your server to perform
these tasks. 

Throughout this guide, we write "yourdomainname" where you should
put your domain name, e.g. "example.com" or "morris.umn.edu", etc.

## Installing Configuring NGINX

Install NGINX:
```
# apt-get install nginx
```

cd into `/etc/nginx/sites-enabled`.

Remove the default configuration:
```
# rm default
```

Create a new file with any name in that location with the following content:
```
server {
        listen 80 default_server;
        listen [::]:80 default_server;
        
        # create location for certbot to use for 
        # certificate generation
        location /.well-known {
                root /var/www/html;
        }

        # pass all other traffic to our server
        location / {
            proxy_pass http://127.0.0.1:2538;             
        }
}
```

Now you can restart NGINX with:
```
# systemctl restart nginx.service
```

At this point, you should be able to visit `http://yourdomainname` and see
the site. (Note how you don't need to specify a port number anymore.)


## Installing and Running certbot

First install certbot with the following commands:
```
# add-apt-repository ppa:certbot/certbot
# apt-get update
# apt-get install certbot 
```

Then run it interactively:
```
# certbot certonly
```

The script will ask you a bunch of questions, but there
should only be a couple that are non-obvious.

The first is when it asks you to choose between "webroot" and "standalone".
Choose "webroot". The second is when it asks you to enter your domain name(s).
Simply use the domain name of your server (which we've been referring to as
"yourdomainname"). The final trouble-point is when it asks you to
select what webroot to use. You must first enter "1", and then it
will prompt you to input the webroot. Use `/var/www/html`.

## Re-configuring NGINX

Now, replace the contents of your config file with the following and then
restart NGINX again.

```
server {
        # re-direct all HTTP traffic to HTTPS
        listen 80 default_server;
        listen [::]:80 default_server;
        return 301 https://$host$request_uri;
}

server {

        listen 443 ssl default_server;
        listen [::]:443 ssl default_server;

        ssl on;
        ssl_certificate /etc/letsencrypt/live/yourdomainname/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/yourdomainname/privkey.pem;
        
        # raise the limit for request for clients so that we can upload
        # large images
        client_max_body_size 300M;
        
        # create location for certbot to use for 
        # certificate generation
        location /.well-known {
                root /var/www/html;
        }

        # pass all other traffic to our server
        location / {
            proxy_pass http://127.0.0.1:2538;             
        }
}
```

## Configuring the firewall

Since clients could always just access your server via `yourdomainname:2538`, you will
need to block the port to force clients to go through the secure proxy.

First, allow `ssh` through the firewall
```
# ufw allow ssh
```
Then enable the firewall
```
# ufw enable
```
Finally, allow ports 80 and 443 for the server
```
# ufw allow 80
# ufw allow 443
```
## Final Remarks

Your site is now served over HTTPS just fine. You will have to change any HTTP requests
in your JavaScript to HTTPS requests since most browsers will prevent insecure
requests on secure pages. 

