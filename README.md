# Prez-HumanTalks-Kong-2015
All the resources for demoing Kong

# Pre-requisites

## Modify /etc/hosts

```
sudo nano /etc/hosts
```

Add:

```
127.0.0.1       localhost chuckapi.io kong.chuckapi.io
```

## Install Kong via Vagrant

```
git clone https://github.com/Mashape/kong
cd kong
git checkout 0.5.2
cd ..
git clone https://github.com/Mashape/kong-vagrant
cd kong-vagrant/
```

Edit the VagrantFile and add:

`config.vm.network :private_network, ip: "192.168.50.4"`

(see http://stackoverflow.com/questions/16244601/vagrant-reverse-port-forwarding)

```
KONG_PATH=$PWD/../kong vagrant up
vagrant ssh
vagrant@precise64:~$ sudo nano /etc/hosts
```

Add:

`192.168.50.1    chuckapi.io`

Go back to vagrant terminal

```
vagrant@precise64:~$ cd /kong
vagrant@precise64:~$ sudo make dev
vagrant@precise64:~$ kong start -c kong_DEVELOPMENT.yml
```

Open another terminal

```
curl http://localhost:8001/

{"version":"0.5.2","lua_version":"LuaJIT 2.1.0-alpha","tagline":"Welcome to Kong","hostname":"precise64","plugins":{"enabled_in_cluster":{},"available_on_server":["ssl","jwt","acl","cors","oauth2","tcp-log","udp-log","file-log","http-log","key-auth","hmac-auth","basic-auth","ip-restriction","mashape-analytics","request-transformer","response-transformer","request-size-limiting","rate-limiting","response-ratelimiting"]}}
```

## Install Wetty

### On MacOSX

* Edit wetty/app.js and replace `'/bin/login'` with `'/usr/bin/login'`
* Edit /etc/ssh/sshd_config (`sudo nano /etc/ssh/sshd_config`) and modify it with `PasswordAuthentication yes`
* Go to `Preferences -> Share -> Remote Login` and add your user

This fixes the `Error: Error: ioctl(2) failed.`

### Installation

```
git clone https://github.com/krishnasrinivas/wetty.git
cd wetty
nvm use v0.12.7
npm install
node app.js -p 3000
```

Open a browser at `http://localhost:3000` to check you can connect to your terminal

## Install chuck-code

### chuck-api
```
cd chuck-code/chuck-api
mvn clean install
java -jar target/chuck-api-1.0-SNAPSHOT-fat.jar
curl http://localhost:8080/api/quote (you should get a quote)
```

### chuck-ui

```
cd chuck-code/chuck-ui
nvm use v4.1.2
npm install
```

Open a browser with the `index.html`

# Commands cheat-sheet

```
curl -i -X POST --url 'http://localhost:8001/apis/' --data 'name=chuckapi' --data 'upstream_url=http://chuckapi.io:8080/' --data 'request_path=/api/quote/'
curl -i -X GET --url 'http://kong.chuckapi.io:8000/api/quote'
curl -X POST 'http://localhost:8001/apis/chuckapi/plugins' --data "name=cors" --data "config.methods=GET"
curl -X POST 'http://localhost:8001/apis/chuckapi/plugins' --data "name=rate-limiting" --data "config.minute=5"
curl -i -X GET --url 'http://kong.chuckapi.io:8000/api/quote'
curl -X POST 'http://localhost:8001/apis/chuckapi/plugins' --data "name=key-auth"
curl -i -X GET --url 'http://kong.chuckapi.io:8000/api/quote'
curl -X POST 'http://localhost:8001/consumers/' --data "username=bob" --data "custom_id=bob@mydigital.com"
curl -X POST 'http://localhost:8001/consumers/bob/key-auth' --data 'key=abcd123'
curl -i -X GET --url 'http://kong.chuckapi.io:8000/api/quote?apikey=abcd123'
curl -i -X GET --url 'http://kong.chuckapi.io:8000/api/quote' -H 'apikey: abcd123'
curl -X POST 'http://localhost:8001/apis/chuckapi/plugins' --data "name=file-log" --data "config.path=/kong/file.log"
```

RAZ

```
curl -i -X DELETE --url 'http://localhost:8001/consumers/bob'
curl -i -X DELETE --url 'http://localhost:8001/apis/chuckapi'
```
