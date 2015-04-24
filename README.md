#docker2plant

Script used to create [plantUml] component diagrams from `docker-compose.yml` files using `links` and `volumes_from` properties.

##Preconditions
- [groovy](http://www.groovy-lang.org/)
- [maven](https://maven.apache.org/) configured to be able to download dependencies. 

##Installation

Just add it to your `/usr/local/bin` folder if you want it to be available from any path. 
Another option is just downloading the script and run it with the proper path of the `docker-compose.yml` file as parameter.

##Example of usage

with a `docker-compose.yml` like this one:

```
app:
  ...
  links:
    - redis:cache
    - mysql
    - mongodb
  volumes_from:
    - ftp
    - nfs
    - log
redis:
  ...
mysql:
  ...
mongodb:
  ...
ftp:
  ...
log:
  ...
nfs:
  ...
```

running `docker2plant.groovy -i log -i mysql` from the folder of the `docker-compose.yml` you will generate following [plantUml] diagram:

```
@startuml

skinparam monochrome true

[app] --> "cache" [redis]
[app] --> [mongodb]
[app] ..> [ftp]
[app] ..> [nfs]

legend left
- Dotted arrows represent dependencies through volumes
endlegend

@enduml
```

You can use [planttext](http://www.planttext.com/) to visualize, editor or get a png of the diagram.

Additionally you can use `-o url` option to just generate the URL.


[plantUml]:http://plantuml.com
