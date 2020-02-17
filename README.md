# README

Sports center management application for COMP2913.

This project is composed of 3 Sub-Modules (API, Native App, JavaScript Frontend)

This file is intended to give a general overview of all of these files.

## API

RESTful API that controls the core logic surrounding the Sports Center management.

The API can be run and debugged with the Included `.idea` files and Docker Environment.

### Prerequisites and Requirements

The tools needed to build this project are:

#### OpenJDK-11

Please ensure it is the OpenJDK11, other versions will cause issues for you!

On Debian based Linux, use the following commands to install

```shell script
wget https://download.java.net/java/GA/jdk11/28/GPL/openjdk-11+28_linux-x64_bin.tar.gz -O /tmp/openjdk-11+28_linux-x64_bin.tar.gz
sudo tar xfvz /tmp/openjdk-11+28_linux-x64_bin.tar.gz --directory /usr/lib/jvm
rm -f /tmp/openjdk-11+28_linux-x64_bin.tar.gz
```

On Windows you can use the MSI Installer found [here](https://developers.redhat.com/products/openjdk/download)
or if you're lucky this should be a [direct link](https://developers.redhat.com/download-manager/file/java-11-openjdk-11.0.6.10-2.windows.redhat.x86_64.msi)


#### IntelliJ Idea IDE

Sign up for a student account [here](https://www.jetbrains.com/student/)

Then you can either install from [their website](https://www.jetbrains.com/idea/)

or if like me you
like all of their IDE's, download the [JetBrains Toolbox](https://www.jetbrains.com/toolbox-app/) and install
IntelliJ Idea from there, the toolbox will keep IntelliJ and all the other apps up to date

### Docker and Docker Compose

This will be used to ensure that we keep a consistent production environment
and used in the CI/CD testing pipeline.

[Docker for Windows](https://docs.docker.com/docker-for-windows/install/)

[Docker for Debian Linux](https://docs.docker.com/install/linux/docker-ce/ubuntu/)

[Docker Compose](https://docs.docker.com/compose/install/)

#### Postman

Follow the invite in your email, create an account and then [download](https://www.postman.com/downloads/) the Desktop app

### Environment Variables

In order to start the project, you must copy a few files and fill in some basic information in the form of `.env` files

The first file is called `.env.development.example`, run `mv .env.development.example .env`

Then open `/.env` in IntelliJ, some configuration to note is the `API_HOST_PORT`

This variable will change the port the API server listens on, if you change this then
please consider this when reading this guide as it will work on the assumption
this build is running on the default port of port 8000.

Next, run `mv api/api.env.example api/api.env`

This file is useful for adding extra configuration the the API Docker container
after building it, we can swap the Spring Profile and Override config, but
generally you won't need to change this.

Next, run `mv db/db.env.example dv/db.env`

Again, this file will require a change, you should change the database
password to something memorable as you will need this to view the database schema

### Starting the Project up

To start the Database and a local API server connected to the database you can simply run `docker-compose up -d api postgres` and then should be able to point the JavaScript front end or Postman to the local port (by default this is 8000)

localhost:8000 by default, however this may be configured in `/.env`

**Please Note:** that the server will be running the code was checked out at the time of running `docker-compose up`, if the code doesn't run then the docker container will not run either

### Connecting to the Database in IntelliJ

To run SQL Scripts and view data you can use the inbuilt Database view.

Use the sidebar on the right to add a new database configuration.

**Please note:** you will need to have the Docker database container up and running before you try to access the local database

You should be able to connect with the following info:
- Port, default is 5432, check `db/db.env` if not sure
- User, default is comp2913, check `db/db.env` if not sure
- Password, set by you, check `db/db.env` if not sure
- Database, default is comp2913, check `db/db.env` if not sure

### Project Management

#### Project Overview

To see work distribution and status check the [GitLab API Kanban board](https://gitlab.com/sgarwood/comp2913-sports-centre-management-project/-/boards/1552867)

#### Requirements

UML Class Diagram can be found [here](https://drive.google.com/drive/folders/1qaHaNSR7rIn5w9SIfkEccdTDHShAKLuu)

#### Specification and Workload assignment

GitLab Issues are [here](https://gitlab.com/sgarwood/comp2913-sports-centre-management-project/issues)

#### Sprints

GitLab Milestones are [here](https://gitlab.com/sgarwood/comp2913-sports-centre-management-project/-/milestones)

### Unit Tests

There are a set of JUnit tests created for the API. Please write the Unit Test before you write code.

You should not push unless all tests are green. If you open a pull request where there are broken tests
this will appear immediately as the CI will display that the build is failing and therefore will not be
merged.

Tests can be ran as such on the CLI with `maven test`

or In IntelliJ via the Run/Debug Configurations under `API TEST SUITE`

### End to End Tests

These tests are performed in Postman REST Client

### Database

The database is either a PostgresSQL database in production or a H2 database runs in memory for
local testing.

### Migrations

Migrations for the database are stored in `/api/resources/db`

Any new schema changes should follow the [standard Flyway Naming Convention](https://flywaydb.org/documentation/migrations#naming) and included in your commit.

Further Information surrounding Flyway can be found [here](https://flywaydb.org/documentation/migrations#overview)


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
