# Spring Batch Web Crawler example
Contains an api that starts a ETL job which scrapes a website and then stores the result in 
multiple tables. Api key is not required to run.
## Deployment
Run by executing `mvn spring-boot:run` in command line
## Api Calls
- `http://localhost:8080/load?url={url}&apiKey={key}` - Trigger point to start Spring Batch Job
- `http://localhost:8080/h2-console` - H2 Console for querying the in-memory tables
## Built With

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/) - Dependency Management
* GSON
* Google Maps
* H2 
* JSOUP
* Spring Data JPA
* Spring MVC
* Spring Batch
## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details
