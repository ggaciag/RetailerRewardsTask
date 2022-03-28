
#Running project
## Requirements
- JDK 17 (developed with Amazon Corretto 17)
- Project uses Lombok for code generation. If opened and run from IDE it might require additional plugins/settings. For intellij Idea that will be lombok plugin and enabling `Enable annotation processing`

## Setting up mongodb
By default, project is configured to run with embedded mongo. This is for the ease of checking how it works without a need for setting up external database. However, if required this can be changed by switching `spring.active.profile` to `dev` and uncommenting ` <scope>test</scope>` in `pom.xml` for dependency:
```
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
    <version>3.4.3</version>
    <!--<scope>test</scope>-->
</dependency> 
```

## Building
Project is based on Spring boot + maven. It can be build using included `mvnw`

- For building and running tests `mvnw clean test`
- For building, runing tests, and generating target `mvnw clean install`
- To run project `mvnw spring-boot:run`

## API documentation
After running, project have embedded swagger-ui available at: `http://localhost:8080/swagger-ui/` 

## Test Data
When project is run with profile `dev` or `devEmbedded`, on startup, database is populated with test data. Responsible for that is `InitialDataGenerator` component. 
Transactions that are loaded can be changed by editing file `resources/data/transactions.json`
