# Readme

Sports center management application for COMP2913.

This project is composed of 3 Sub-Modules (API, Native App, JavaScript Frontend)

This file is intended to give a general overview of all of these files.

## API

RESTful API that controls the core logic surrounding the Sports Center management.

The API can be ran and debugged easily with the Included .idea files and Docker Environment.

## Pre-requisites

You must have Docker and Docker-Compose correctly installed.

### Diagrams

UML Class Diagram can be found [here]()

### Unit Tests

There are a set of JUnit tests created for the API. Please write the Unit Test before you write code.

You should not push unless all tests are green. If you open a pull request where there are broken tests
this will appear immediately as the CI will display that the build is failing and therefore will not be
merged.

Tests can be ran as such on the CLI with

```c
```

or In IntelliJ via

```
```

### End to End Tests

These tests are performed in Postman REST Client

### Database

The database is either a PostgresSQL database in production or a H2 database runs in memory for
local testing.

### Migrations

Migrations for the database are stored in `/api/resources/db`

Any new schema changes should follow the standard Flyway Naming Convention and included in your commit.


## Front End

### Acceptance/Feature Tests

### Unit Tests

Unite and Integration tests are done using the JavaScript test stack Mocha, Chai and Sinon

## Native App


### Unit Test



# Staging Environment

There is a sample environment running at ...

This is automatically deployed to when there is a successful merge to staging

# Production Environment

You can find the production deployment diagram [here](https://www.draw.io/#G1YYV0_hLkMsQqtDgw5gjmA94cBizx8cLn)
