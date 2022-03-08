# exam-generator-api

## Description

This project is an API built with Java and Spring Framework which returns data in Json format. This project is meant to allow Professors to create Courses that may contain questions, choices and answers for the questions, which Students would be able to access later, pretty similar to Google Forms, but in a light version. In it, I used a Docker container to create a MySQL image and JPA with Hibernate to create the repository of my application, I also had to use Hibernate Query Language(HQL) for creating a customized queries. This project is full documented with SpringFox so it is easier for people to understand and utilize its endpoints.

## How to Install and Run

For this project, you've got to have Docker installed in your computer for creating the MySQL image. You can follow the step-by-step on how to install Docker here: https://docs.docker.com/desktop/windows/install/

After installing the Docker, you gonna have to download all the files in the Exam-Generator-Api folder available on https://github.com/DavidPorto22/exam-generator-api. After that, run in the terminal within the application folder the command "docker-compose up". This command will engage the docker-compose.yml file that specifies which MySQL image have to be downloaded, and also creates the container and everything needed to create a MySQL instance.

Finally, you can run the project in your favorite IDE. When you run the project, the Hibernate will take care of creating all the tables needed.

## How to Use the Project

Since all the API's endpoints are documented with SpringFox

## Credits

#### Technologies

- Java
- Spring Boot
- Spring Framework
- JPA e Hibernate
- JWT
- Docker
