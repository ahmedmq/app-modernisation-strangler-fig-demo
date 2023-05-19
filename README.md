# Application Modernisation using Strangler Fig Pattern

This repo contains a step-by-step tutorial on implementing a strangler fig pattern using CDC (Change Data Capture) to decompose a legacy monolith into microservices and deploying to Kubernetes.

See Git tags for step-by-step progress of modernisation of the application in scope. Clone this repository and run the below command from the root of the project folder:

```text
$ git tag -ln

v1.0.0			Spring PetClinic Monolith Baseline
v2.0.0                  Add Strangler Proxy
v3.0.0                  Create new replacement service
```

## Introduction
One of the most common techniques in application modernization is the Strangler Fig Pattern. Martin Fowler first captured this [pattern](https://martinfowler.com/bliki/StranglerFigApplication.html), inspired by a certain type of fig that seeds itself in the top branches of trees. The fig then descends towards the ground to take root, gradually enveloping the original tree. At first the existing tree becomes a support structure for the new fig but eventually the fig grows into a beautiful structure, fully self-supporting where it starts consuming the nutrients/resources from the original tree leaving it  to die and rot

![](docker/nginx/volume/strangler-fig.jpg)

In the context of app modernization we can draw a parallel here, where we can incrementally build microservices that replicate functionality of the existing monolith. The idea is that the old legacy monolith  and the new microservices can coexist and we can evolve the new services over time with the same functionality provided by the monolith and eventually replacing the old system

The following sections describe the Git commit tags (shown above) made in order to modernise the Spring PetClinic application using the Strangler Fig Pattern.

### v1.0.0 - Spring PetClinic Monolith Baseline
<hr/>

Spring PetClinic is an open-source sample application created by SpringSource for the Spring Framework. It is designed to display and manage information related to pets and veterinarians in a pet clinic. Instructions to run the baseline application locally can be found in the [spring-petclinic/README.md](spring-petclinic/README.md) file.

![](docs/baseline.png)

The monolithic application consists of four different modules all packaged and deployed as a single artefact onto a Tomcat Application server. All data related to the modules is stored in a single MySQL database.

### v2.0.0 - Add Strangler Proxy
<hr/>

Introduce a proxy into the infrastructure and configure the network to route all monolith traffic via it. The proxy will initially allow all traffic to pass through unmodified to the monolith. Its configuration will be gradually updated as new microservices are developed, routing specific requests to the newly created microservices.

![Add Proxy](docs/add_proxy.png)


- Change the Apache Tomcat web server port to `8080` from `80`. The NGINX web server will instead listen to incoming connections on port `80`.

- Define the basic [configuration](docker/nginx/config/nginx.conf) for an NGINX reverse proxy to route all incoming traffic on port `80` to the tomcat web server port `8080` where the spring-petclinic application is running.
   
- From the root folder of the project, run the following command to start the docker containers

    ```bash
     docker compose up -d
  ```
    The [docker-compose.yaml](docker-compose.yaml) in the root folder reuses the docker compose [file](spring-petclinic/docker-compose.yml) inside the spring-petclinic project. By running the command above it will start the spring-petclinic app, MySQL and NGINX container. 

Access the NGINX index page on [http://strangler-fig.demo/](http://strangler-fig.demo/).

The PetClinic application can be accessed on : [http://strangler-fig.demo/petclinic](http://strangler-fig.demo/petclinic)

### v3.0.0 - Create new replacement service 
<hr/>

Incrementally build the owner functionality in a new microservice. The new service is re-written using a modern toolkit.  Instructions to run the baseline application locally can be found in the [petclinic-owner-service/README.md](petclinic-owner-service/README.md) file.

It is worth noting that in this step , we can have the new service deployed into a production environment and wait until the functionality is full tested, with the understanding that no live traffic is handled by the new service

![New Service](docs/new_service.png)

In this step, only the READ functionality is implemented within the new microservice.

| Description      | HTTP Method | API               | Implemented |
|------------------|-------------|-------------------|-------------|
| Find Owners      | GET         | /owners/find      | Yes         |
| List All Owners  | GET         | /owners           | Yes         |
| Get Owner by Id  | GET         | /owners/{id}      | Yes         |
| Create New Owner | POST        | /owners/new       | No          |
| Edit Owner       | POST        | /owners/{id}/edit | No          |

