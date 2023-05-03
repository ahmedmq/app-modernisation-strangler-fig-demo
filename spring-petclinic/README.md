# Spring PetClinic Sample Application
<hr/>

The Spring PetClinic is a sample application designed to show how the Spring stack can be used to build simple, but powerful database-oriented applications. This fork of the Spring PetClinic [project](https://github.com/spring-petclinic) is a monolithic application built using legacy Spring Framework 3 configuration with a 3-layer architecture (i.e. presentation --> service --> repository)

![Screenshot](docs/screenshot.png)

The project uses Maven to structure and bootstrap a Spring core 3.0.6 application deployed onto a Tomcat 8.5 container. MySQL 5.7.8 database provides persistent storage support 

## Running PetClinic locally using Docker
<hr/>

### Prerequisites
The following should be installed in your system:

* [Docker](https://docs.docker.com/engine/install/)
* [Docker Compose](https://docs.docker.com/compose/install/)

### Steps

- From a new terminal window inside the `spring-petclinic` folder, run the following command:

    ```bash
    docker compose up -d
    ```
    This will build the web application and run it as a docker container. The MySQL database is also started as a container

  
- Wait for all docker containers to be up and running. Check using the following command:

   ```bash
   docker compose ps
  ```

  The [dockerfile](./Dockerfile) includes a command to change the default Tomcat port to `80`


- Update `/etc/hosts` file in your local system to map a sample domain to `localhost`

  ```bash
  sudo vi /etc/hosts
  ```
  After you enter your password, add the below domain mapping

  ```text
  127.0.0.1 strangler-fig.demo
  ```

You can then access PetClinic here: [http://strangler-fig.demo/petclinic](http://strangler-fig.demo/petclinic)

## Running PetClinic locally without Docker
<hr/>
This section describes the manual steps for installing MySQL, Tomcat and then deploying the application to the Tomcat Server

### Prerequisites
- Java 1.8 
- [Homebrew](https://brew.sh/)

### Steps

- Install MySQL

    ```bash
    brew install mysql@5.7
    ```
- Set up path for mysql

    ```bash
    export PATH="/opt/homebrew/opt/mysql@5.7/bin:$PATH"
    ```
  
- Set up password authentication using

    ```bash
    mysql_secure_installation
    ```
  Answer the prompts with the following:

  - VALIDATE PASSWORD plugin: `Y`
  - Password Validation Policy: `0` (LOW)
  - New Password: `password`
  - Renter Password: `password`
  - Do you wish to continue with the password: `Y`
  - Remove anonymous user: `Y`
  - Disallow root login from remote: `Y`
  - Remove the test database: `Y`
  - Reload privilege tables now: `Y`


- Create the `petclinic` database 

    - Log in to MySQL server
    ```
     mysql -uroot -ppassword

    ```
  
    - Create the database
    ```
     CREATE DATABASE `petclinic` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```


- Set up Tomcat on your machine

  - Start by [downloading the binary](https://tomcat.apache.org/download-80.cgi)
  
  - Extract to a location and inside the bin directory start the Tomcat server using the below command
    ```bash
    ./catalina.sh run
    ```

    Browse to http://localhost:8080, and you should see the Tomcat installation page.
  

- From a new terminal window inside the `spring-petclinic` folder, package the application with the following command:

    ```bash
    ./mvnw clean package
    ```
    This will create a `petclinic.war` file inside the `target` folder


 - Deploy the WAR to `webapps` inside the tomcat installation folder

    ```bash
    cp ./target/petclinic.war <TOMCAT_INSTALLATION>/webapps/
    ```

- For the purpose of this demo, change the Tomcat port number to `80`. Edit the main `Connector` element in the `tomcat/conf/server.xml` file. Find the XML tag that looks something like this:

    ```text
    <!-- Define a non-SSL HTTP/1.1 Connector on port 8080 -->
    <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
    ```

  Once you locate the above, change the port attribute from `8080` to `80`


- Stop (`Ctrl+C`) and Start the Tomcat container by following the steps as before


- Update `/etc/hosts` file in your local system to map a sample domain to `localhost`
    
  ```bash
  sudo vi /etc/hosts
  ```
  After you enter your password, add the below domain mapping

  ```text
  127.0.0.1 strangler-fig.demo
  ```
    

Alternatively, You can use the IDE to add Tomcat as a server and deploy the artifact. The instructions can be found [here](https://www.jetbrains.com/idea/guide/tutorials/working-with-apache-tomcat/using-existing-application/) 

You can then access PetClinic here: [http://strangler-fig.demo/petclinic](http://strangler-fig.demo/petclinic)
