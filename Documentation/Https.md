# Using HTTPS with our server

We made the decision to not add any SSL support
directly to our server. Instead, we placed a simple
proxy in front of it to handle all of that. Following
all the instructions in this document should successfully
redirect all HTTP requests to HTTPS, and cause all HTTPS requests
to go to our server.

You will need to be logged in as root on your server to perform
these tasks. These steps assume that you are running Ubuntu 16.04.2 LTS.

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
        ssl_certificate /etc/letsencrypt/live/yourdomainname/cert.pem;
        ssl_certificate_key /etc/letsencrypt/live/yourdomainname/privkey.pem;
        
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

