# Spring OAuth2 Google

A Spring Boot application that integrates Google OAuth2 authentication. It includes user registration, traditional login, and Google login for a seamless authentication experience.

## Features

- **User Registration** and **Login** with email/password
- **Google OAuth2 Authentication**
- **Product Management**: Add and view products

## Technologies

- Spring Boot
- OAuth2
- Spring Security
- Thymeleaf
- MySQL

## Getting Started

1. **Clone the Repository**

    ```bash
    git clone https://github.com/yourusername/spring-oauth2-google.git
    cd spring-oauth2-google
    ```

2. **Configure Application**

    Update `application.properties` with your database and Google OAuth credentials.

3. **Build and Run**

    Using Maven:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

    Using Gradle:

    ```bash
    ./gradlew build
    ./gradlew bootRun
    ```

4. **Access**

    Visit `http://localhost:8080` in your browser.

## Contributing

1. Fork the repo
2. Create a branch
3. Make changes
4. Submit a pull request

## License

MIT License

For questions or issues, open an issue on this repo or contact the maintainer.

