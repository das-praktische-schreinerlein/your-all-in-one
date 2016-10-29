# docker-yaio
A build environment for a dockered version of YAIO

## docker-compose current version
or build a version with docker-compose
    
    ./setup-build.sh
    docker-compose up

and run a version with docker-compose
    
    docker-compose up
    
checkit [http://192.168.99.100:8666/yaio-explorerapp/yaio-explorerapp.html](http://192.168.99.100:8666/yaio-explorerapp/yaio-explorerapp.html)     

## build and run tagged special versions
build a docker-container for a packaged version of yaio (build from src)

    ./build-dockerimage.sh /cygdrive/d/Projekte/yaio-playground/

run it (specify the version of the pom - 0.1.0-beta-feature-YAIO2859-improvements-201610-SNAPSHOT)
    
    docker run -p 8666:8666 --name yaioserver-0.1.0-beta-feature-YAIO2859-improvements-201610-SNAPSHOT -it yaio/yaioserver:0.1.0-beta-feature-YAIO2859-improvements-201610-SNAPSHOT