# exam-generator-api

## Description

This project is an API built with Java and Spring Framework which returns data in Json format. This project is meant to allow Professors to create Courses that may contain questions, choices and answers for the questions, which Students would be able to access later, pretty similar to Google Forms, but in a light version. In it, I used a Docker container to create a MySQL image and JPA with Hibernate to create the repository of my application, I also had to use Hibernate Query Language(HQL) for creating cusitomized queries. I also used JWT for the project's security so the api is stateless and generates a token for the client's requests. The project is full documented with SpringFox so it is easier for people to understand and utilize its endpoints.

## How to Install and Run

For this project, you've got to have Docker installed in your computer for creating the MySQL image. You can follow the step-by-step on how to install Docker here: https://docs.docker.com/desktop/windows/install/

After installing the Docker, you gonna have to download all the files in the Exam-Generator-Api folder available on https://github.com/DavidPorto22/exam-generator-api. After that, run in the terminal within the application folder the command "docker-compose up". This command will engage the docker-compose.yml file that specifies which MySQL image have to be downloaded, and also creates the container and everything needed to create a MySQL instance.

Finally, you can run the project in your favorite IDE. When you run the project, the Hibernate will take care of creating all the tables needed.

## How to Use the Project

Since the project uses JWT, for being able to use all the endpoints available in the project, you going to have to send a Post request to localhost:8085/login with json pattern "{"username":"username","password":"password"}" which will return a token for later requests. Do not forget to populate the ApplicationUser and Professor tables before sending the request.

Since all the API's endpoints are documented with SpringFox, you just need to access the url "localhost:8085/swagger-ui.html" in your browser to be able to access the project's documentation. With that, you will have access to all endpoints' information.

## Credits

Many thanks to all DevDojo's crew how made all the knowledge to create this project available. If you truly want to master Java, SpringBoot, JSF or get started with Microservices, definitely check DevDojo Academy out!! They have the best Java course available in the internet, and it's for free!! 
Special Thanks to William Suane, DevDojo's CEO.

DevDojo Academy: https://devdojo.academy/

## Technologies

- Java
- Spring Boot
- Spring Framework
- JPA e Hibernate
- JWT
- Docker
