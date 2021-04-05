# v1 User Service

One Accuracy v1 user service handles api calls for user registration. It is implemented as a spring-boot project with swagger for API documentation.

#### Technologies and Frameworks

* Spring Boot 2
* JPA
* Hibernate 5 
* Lombok
* JUnit 5
* Mockito 2
* MySQL 8+ (InnoDB)
* Docker
* Spring Boot Admin
* Jacoco plugin

### Running Unit Tests
#### Configure your local MySQL Database for Unit Tests
1. create a database called userservicedbtest
2. change the required settings in test/resources application.yml
3. run: mvn test
4. Test report are in target/site/jacoco/index.html folder. Open it in a browser


### Running Local Devserver
#### Configure your local MySQL Database for Devserver
1. create a database called userservicedb
2. change the required settings in test/resources application-dev.yml
3. run: mvn clean install -o
4. Database Migrations: mvn flyway:migrate -Dflyway.url=jdbc:mysql://localhost:3306/userservicedb?useSSL=false -Dflyway.user= -Dflyway.password=   
5. run with correct values: java  -Dspring.profiles.active=dev -DMYSQL_USERNAME= -DMYSQL_PASSWORD= -DEMAIL_USERNAME= -DEMAIL_PASSWORD= -jar target/oneworld-user-service-1.0-SNAPSHOT.jar


### Running Docker Server
#### Configure your local MySQL Database for Devserver
1. create a database called userservicedbdocker
2. change the required settings in test/resources application-dev.yml
3. run: mvn clean install -o
4. Database Migrations: mvn flyway:migrate -Dflyway.url=jdbc:mysql://localhost:3306/userservicedbdocker?useSSL=false -Dflyway.user= -Dflyway.password=
5. Build image: ./docker.sh to build
6. Stop the service if its already running: docker stop user-service
7. Remove the service: docker rm user-service
8. Run application: docker run -d --name user-service -e SPRING_PROFILES_ACTIVE=docker \
   -e MYSQL_USERNAME= \
   -e MYSQL_PASSWORD= \
   -e EMAIL_USERNAME= \
   -e EMAIL_PASSWORD= \
   -p 9200:9001 user-service:1.0-SNAPSHOT
9. Logs: docker logs -f user-service


### Swagger documentation
* http://localhost:9001/swagger-ui.html

### Logging & Monitoring
* http://localhost:9001
