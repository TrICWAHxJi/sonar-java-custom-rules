# Custom rules plugin for SonarQube 10.3 and Java 11

## Install

* `cd <project directory>`
* `mvn clean install`
* `cp ./target/sonar-java-custom-rules-0.0.1.jar ./volumes/plugins/`
* `docker compose up`

## Debug

`mvnDebug clean install sonar:sonar -Dsonar.projectKey='<project_key>' -Dsonar.projectName='<project_name>' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=<token>`