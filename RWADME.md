# Playing with kt-search

## Build Docker Image
>1. gradle shadowJar
  
>2. docker build -t nmicra/scripts:kt030823 .

## Observe the help flags
>   docker run --rm nmicra/scripts/subscriberoofer:030823
>>Usage: sync options_list
Options:
--raterUrl, -ru -> Rater Url (always required) { String }
--raterPort, -rp -> Rater Port (always required) { Int }
--raterIsHttps, -rhttps [false] -> Rater Is Https Connection
--simIndex, -si -> Sim Index (always required) { String }
--elasticHost, -eh -> Elastic Host (always required) { String }
--elasticPort, -eport -> Elastic Port (always required) { Int }
--elasticIsHttps, -ehttps [false] -> Elastic Is Https Connection
--elasticUser, -eu [] -> Elastic User { String }
--elasticPass, -epass [] -> Elastic Password { String }
--help, -h -> Usage info

## Run Migration
>docker run --rm --entrypoint java docker.io/nmicra/scripts:kt030823 -Djava.security.egd=file:/dev/./urandom -Xmx512m -jar /work/app.jar -ru <REST_RT_IP> -rp 80 -si solid_sims -eh <ELASTIC_INSTANCE_IP> -eport 9200