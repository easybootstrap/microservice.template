# Readme Template

This is a clojure microservice template: Components, Reitit, Pedestal, Malli, Postgresql and Tests.


## About this template
 - **template**: An example of how use the template, it's a simple job post api, which you can create, list all or list one job by id.

- [common/components](https://github.com/easybootstrap/common): Foundation to create new services in clojure,
you can find components for database, http, webserver and tools for db migrations.

Verb | URL                | Description
-----| ------------------ | ------------------------------------------------
GET  | /job/              | get all job
POST | /job/              | create a new job post
GET  | /job/:id           | get the job by id

## Directory Structure
```
./
├── .clj-kondo -- clj-kondo configuration and classes
├── .lsp -- clojure-lsp configuration
├── .github
│   └── workflows -- Github workflows folder.
├── docker -- docker and docker-compose files for the database
├── resources -- Application resources assets folder and configuration files.
│   └── migrations -- Current database schemas, synced on service startup.
│   └── config.edn -- Application config.
├── src -- Library source code and headers.
│   └── io/easybootstrap/template -- Source for the service example job template.
└── test -- Test source code.
    ├── integration -- Integration tests source (uses state-flow).
    │   └── io/easybootstrap/template/ -- Tests for service integration.
    └── unit -- Unity tests source (uses clojure.test).
        └── io/easybootstrap/template/ -- Tests for service unit.
```

## Repl
To open a nrepl
```bash
clj -M:nrepl
```
To open a nrepl with all test extra-deps on it
```bash
clj -M:test:nrepl
```
## Run dev
To open a dev nrepl
```bash
clj -M:dev:nrepl
```

Then go to `dev/user.clj` and eval `(start)`

## Run Tests
To run unit tests inside `./test/unit`
```bash
clj -M:test :unit
```
To run integration tests inside `./test/integration`
```bash
clj -M:test :integration
```
To run all tests inside `./test`
```bash
clj -M:test
```
To generate a coverage report 
```bash
clj -M:test --plugin kaocha.plugin/cloverage

|----------------------------------------------------+---------+---------|
|                                          Namespace | % Forms | % Lines |
|----------------------------------------------------+---------+---------|
|             io.easybootstrap.template.adapters.job |   65.71 |   61.54 |
|          io.easybootstrap.template.controllers.job |  100.00 |  100.00 |
| io.easybootstrap.template.mediator.db.postgres.job |  100.00 |  100.00 |
|         io.easybootstrap.template.mediator.http-in |  100.00 |  100.00 |
|                   io.easybootstrap.template.server |   14.93 |   31.82 |
|              io.easybootstrap.template.wire.common |  100.00 |  100.00 |
|              io.easybootstrap.template.wire.db.job |  100.00 |  100.00 |
|              io.easybootstrap.template.wire.in.job |  100.00 |  100.00 |
|             io.easybootstrap.template.wire.out.job |  100.00 |  100.00 |
|----------------------------------------------------+---------+---------|
|                                          ALL FILES |   84.42 |   86.58 |
```

## Lint fix and format

```bash
clj -M:clojure-lsp format
clj -M:clojure-lsp clean-ns
clj -M:clojure-lsp diagnostics
```

## Migrations
To create a new migration with a name
```bash
clj -M:migratus create migration-name
```
To execute all pending migrations
```bash
clj -M:migratus migrate
```
To rollback the latest migration
```bash
clj -M:migratus rollback
```
See [Migratus Usage](https://github.com/yogthos/migratus#usage) for documentation on each command.

## Docker
Start containers with postgres `user: postgres, password: postgres, hostname: db, port: 5432`  
and [pg-admin](http://localhost:5433) `email: pg@pg.cc, password: pg, port: 5433`
```bash
docker-compose -f docker/docker-compose.yml up -d
```
Stop containers
```bash
docker-compose -f docker/docker-compose.yml stop
```

## Running the server
First you need to have the database running, for this you can use the docker command in the step above.

### Repl
You can start a repl open and evaluate the file `src/microservice_boilerplate/server.clj` and execute following code:
```clojure
(start-system! (build-system-map))
```

### Uberjar
You can generate an uberjar and execute it via java in the terminal:
```bash
# genarate a target/service.jar
clj -T:build uberjar
# execute it via java
java -jar target/service.jar
```
## Related

Based on [this](https://github.com/parenthesin/microservice-boilerplate-malli)

## License
This is free and unencumbered software released into the public domain.  
For more information, please refer to <http://unlicense.org>


