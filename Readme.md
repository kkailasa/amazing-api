# amazing-api
This project implements the API needed for managing the basic organization data and the relationship between them. This project exposes 4 REST operations implemented using Spring Boot and is persisted in a PostgresSQL database.

## Getting Started

To clone and run this application, you'll need [Git](https://git-scm.com) , [OpenJDK 11](https://openjdk.java.net/projects/jdk/11/) and [PostgreSQL](https://www.postgresql.org/) 

```bash
# Clone this repository
$ git clone https://github.com/kkailasa/amazing-api

# Go into the repository
$ cd amazing-api
```
### Local Development
Modify the application.properties under src/main/resources to have the correct references to the database hostname, username and password.

```bash
# Build  the package
$ mvnw clean package

# Run the app
$ mvnw spring-boot:run
```
### Docker setup
This project comes with the dockerfiles and docker-compose files for running the services on a docker environment ( DB and Web ). To run the docker images, docker engine and docker-compose are needed.

```bash
# Build  the images
$ docker-compose build

# Run the containers ( db and web )
$ docker-compose up -d
```

## How to Test

Once the application is up and running the API's are exposed to /api/orgdata endpoint. The swagger-ui for the project can be opened at 
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

<p align="left">
  <img src="./img/operations-overview.png" alt="Overview" width="650">
</p>

### Create new OrgData ( POST /api/orgdata )

This operation is used for creating a new OrganizationData in the System. 
The example input for the operation is 

```bash
{
  "ancestorIdentifier": "string",
  "identifier": "0",
  "name": "root"
}
```

The "ancestorIdentifier" should not be used if there are no ancestorIdentifier ( ie for the root node )

### Update OrgData ( PUT /api/orgdata )

This operation is used for updating  a OrganizationData contents in the System.
This can be used for updating the name, identifier or ancestorIdentifier ( changing parent ) 
The example input for the operation is 

```bash
{
  "ancestorIdentifier": "string",
  "identifier": "0",
  "name": "root"
}
```

### Retrieve all  OrgData ( GET /api/orgdata )

This operation is used to retrieve all the OrganizationalData in the System.
This does not build the relationship between the OrganizationalData 
 
### Retrieve a specific OrgData ( GET /api/orgdata/{identifier}?includeDescendants=(true|false) )

This operation is used to retrieve all the details specific to an identifier in the System. It can also retrieve all the descendants of it based on the includeDescendants flag

## Implementation

The requirement to persist a tree like structure into a persistent store has been implemented to be persisted into a RDBMS database. In order to be flexible and to retrieve the data faster Closure table approach has been taken. The database consists of 2 tables. 

<p align="left">
  <img src="./img/ER.png" alt="ER" width="650">
</p>

The organizationaldatahierarchy is the closure table and is populated/updated by using triggers on the organizationdata table.


