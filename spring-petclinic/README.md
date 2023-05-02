# Spring PetClinic Sample Application

### Prerequisites
The following should be installed in your system:
* Java 8 
* [Maven 3.3](http://maven.apache.org/install.html)
* [Docker](https://docs.docker.com/engine/install/)
* [MySQL](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/)

Alternatively you can use [SDKMAN](https://sdkman.io/) to install some of the above in a convenient manner. The below steps use SDKMAN to install few of the prerequisites

## Running PetClinic locally

- In a new terminal window , install Java 8 using the below command

   ```bash
   sdk install java 8.0.372-amzn
  ```
  
- In the same window, install maven 3.3 using the below command

   ```bash
   sdk install maven 3.3.9
  ```

- Run MySQL as a docker container

    ```bash
    docker run -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7.8
    ```

- Run the application

    ```bash
    mvn package cargo:run -P MySQL
    ```

You can then access PetClinic here: [http://localhost:8080/](http://localhost:8080/)

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">



