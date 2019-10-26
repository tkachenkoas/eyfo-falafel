## Install && run manual

### About

Eyfo-falafel ('Where is falafel?') is a simple hobby project of a client-server web application for aggregation 
and rating places that serve my favourite 'fast-food' - falafel. At some point mobile app clients may be added, but for now it's only backend with a web
client for admin portal with minimum security.

If you want to explore it in live mode, https://95.181.198.73:8443/ is the dev 
server (contact me for login and password)

### Technology stack
* Backend: spring boot 2.1.* with java 8, bits of groovy
* Frontend: angular 8 & typescript
* Infrastructure: redis server (for session storage), PostgreSQL database
* build tools: maven (mine is 3.6.0, latest will probably be fine), node.js and npm 
(mine is 12.11.1, latest will probably be fine)

### Required components to run the app locally
* **Redis**  
Redis server is required to run the app. I use 5.0.3 for local and dev environments. If you are a Windows user, 
I strongly suggest that you use a Docker image with redis instead of installing redis-windows (which is 2.4.6, 
and requires different configuration from what is used in project)

* **Database**  
PostgreSQL server 9.6+ is required (I use 11.5, but 9.6 will do fine). You are also required to install PostGIS 
extension for geometry/geography or maybe use some ready Docker images with PostGIS already included

* **External API keys**  
For better experience, Google Maps API key is recommended (get one for free from google). However, if not provided, 
local stubs can be used on server side.

### Local properties files

Properties files for backend, frontend and database are templated. So you'll need to provide files with 
real credentials for maven to use them

* **Frontend local-properties file**  
Target directory:  
_./eyfo-frontend/src/environments/_  
Just copy environment.ts and rename it to environment-local.ts, also replace @GOOGLE_MAPS_API@ placeholder 
with your value

* **Backend properties source.**   
These properties will be read by maven-properties-plugin and will be used in filtering app's resources  
Target file:  
_./conf/local/application.properties_  
Sample content:
```
 # If left blank, local stub will be used 
 GOOGLE_API_KEY=
 # security props -> for password hashing
 security.password.strength=
 security.password.salt=
 # filestorage path -> for uploaded files
 files.drive.folder=/home/user/dev-files/
 # redis -> default params may be ignored for spring boot
 spring.redis.host=localhost
 spring.redis.port=6379
 spring.redis.password=
 # database credentials
 spring.datasource.url=jdbc:postgresql://localhost:5432/db-name
 spring.datasource.username=
 spring.datasource.password=

 # This data will be used to create a user in db with hashed password
 admin.username=
 admin.password=
```

* **Backend unit and IT test prop source**  
This file will be used as-is and copied to test-resources for UT's and IT's  
Target file:   
_./conf/test/application.properties_  
Sample content:
```
files.drive.folder=/home/user/dev-files/test

GOOGLE_API_KEY=

# I suggest using another db because unit-test will clean all tables
spring.datasource.url=jdbc:postgresql://localhost:5432/db-name
spring.datasource.username=
spring.datasource.password=

spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.spatial.dialect.postgis.PostgisDialect

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# enhanced logging in tests-> uncomment if needed

# logging.level.org.hibernate.SQL=debug
# spring.jpa.show-sql=true
# spring.jpa.properties.hibernate.format_sql=true
# logging.level.org.hibernate.type.descriptor.sql=trace

# SQL statements and parameters
# log4j.logger.org.hibernate.SQL=debug
# log4j.logger.org.hibernate.type.descriptor.sql=trace

# security
security.password.strength=
security.password.salt=

#redis
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=

# local app runs on 8080, testing app will run on 8084
tomcat.test.port=8084
```

### Building the app locally

If you're using Intellij Idea, .idea/runConfigurations will be automatically imported from git repo. 
You'll be able to run them by config name. For non-Intellij users maven commands are also provided (hope they won't outdate) 

I suggest following steps:

1. 'Liquibase update':  
_mvn clean process-resources -Plocal,updateDb -pl :eyfo-database_  
_mvn clean process-resources -PtestProps,updateDb -pl :eyfo-database -> for testing db_

2. 'Run all tests' to ensure that everything is configured fine  
_mvn clean verify -PtestProps,runTests -pl :eyfo-backend_

3. Build frontend
_cd eyfo-frontend/_  
_npm run prepare-packages_  
_npm run build-local_  

4. 'Package local app jar' -> to get an executable spring-boot jar in /target folder. Frontend is included    
_mvn clean verify -Plocal,packFrontend -DskipTests -pl :eyfo-backend_

5. Running backend and frontend separately  
_mvn clean verify -Plocal -DskipTests spring-boot:run -pl :eyfo-backend_  
_cd eyfo-frontend/_   
_npm run start_

6. Pushing some data into db not to have empty boring scrollers  
    6.1 Creating admin user to log in
        Since password must be stored hashed, direct db password manipulation will be no help.
        A groovy script than hashes the password with provided salt and strength is used to 
        create admin user. To run it, use following command:  
        _mvn clean verify -Plocal,createAdminUser -DskipTests -pl :eyfo-database_  
    6.2 Creating some 'mock' places (sorry, they will all be located in Moscow 
        and spelled in russian). This is also done via activating a groovy script:  
        _mvn clean verify -Plocal,seedTestPlaces -pl :eyfo-database_