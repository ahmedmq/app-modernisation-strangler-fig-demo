# Application Modernisation using Strangler Fig Pattern

This repo contains a step-by-step tutorial on implementing a strangler fig pattern using CDC (Change Data Capture) to decompose a legacy monolith into microservices and deploying to Kubernetes.

See Git tags for step-by-step progress of modernisation of the application in scope. Clone this repository and run the below command from the root of the project folder:

```text
$ git tag -ln

v1.0.0			Spring PetClinic Monolith Baseline
```

## Introduction
One of the most common techniques in application modernization is the Strangler Fig Pattern. Martin Fowler first captured this [pattern](https://martinfowler.com/bliki/StranglerFigApplication.html), inspired by a certain type of fig that seeds itself in the top branches of trees. The fig then descends towards the ground to take root, gradually enveloping the original tree. At first the existing tree becomes a support structure for the new fig but eventually the fig grows into a beautiful structure, fully self-supporting where it starts consuming the nutrients/resources from the original tree leaving it  to die and rot

![](docs/strangler.jpg)

In the context of app modernization we can draw a parallel here, where we can incrementally build microservices that replicate functionality of the existing monolith. The idea is that the old legacy monolith  and the new microservices can coexist and we can evolve the new services over time with the same functionality provided by the monolith and eventually replacing the old system

The following sections describe the Git commit tags (shown above) made in order to modernise the Spring PetClinic application using the Strangler Fig Pattern.

### v1: Spring PetClinic Monolith Baseline

Spring PetClinic is an open-source sample application created by SpringSource for the Spring Framework. It is designed to display and manage information related to pets and veterinarians in a pet clinic. Instructions to run the baseline application locally can be found in the [spring-petclinic/README.md](spring-petclinic/README.md) file.

![](docs/baseline.png)

The monolithic application consists of four different modules all packaged and deployed as a single artefact onto a Tomcat Application server. All data related to the modules is stored in a single MySQL database.