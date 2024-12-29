# Library Management System

A comprehensive solution for managing library resources, including books and borrowers. This project is built using modern technologies and follows best practices for software development.

## Features

- Add, update, and delete book records
- Manage borrower information
- Search functionality for books and borrower

## Tech Stack

### Backend

- **Java Spring Boot**: For developing the RESTful APIs.
- **Hibernate/JPA**: For ORM (Object-Relational Mapping) and database interactions.
- **MySQL**: As the relational database for storing records.
- **ModelMapper**: For object-to-object mapping between DTOs and entities.
- **Lombok**: To reduce boilerplate code in Java.
- **JUnit**: For unit testing.
- **Swagger/OpenAPI**: For API documentation.
- **SLF4J with Logback**: For logging purposes.

### Containerization and Deployment

- **Docker**: For containerizing the application.
- **Docker Compose**: For orchestrating multi-container Docker applications.
- **GitHub Actions**: For Continuous Integration and Continuous Deployment (CI/CD) workflows.

## REST APIs

The application exposes RESTful endpoints for managing library resources. The APIs follow standard conventions and are documented using Swagger/OpenAPI.

- **Books API**: Manage book records.
  - `GET /api/books`: Retrieve all books.
  - `GET /api/books/{id}`: Retrieve a specific book by ID.
  - `POST /api/books`: Add a new book.

- **Borrower API**: Manage borrower information.
  - `GET /api/borrowers`: Retrieve all borrowers.
  - `POST /api/borrowers`: Adds a new Borrower
  - `POST /api/borrowers/{borrowerId}/borrow/{bookId}`: Add a new borrowers with book.
  - `POST /api/borrowers//{borrowerId}/return/{bookId}`: Return book
  - `GET /api/borrowers/{id}`: Retrieve borrower


## Validations

The application employs Spring Boot's validation framework to ensure data integrity and consistency.

- **Annotations**: Used on DTOs to enforce constraints (e.g., `@NotNull`, `@Size`, `@Email`).
- **Custom Validators**: Implemented for complex validation scenarios.
- **Exception Handling**: Global exception handlers provide meaningful error responses.

## Object Mapping with MapStruct

ModelMapper is used for efficient and type-safe mapping between DTOs and entities.

- **Mapper Interfaces**: Defined for each entity to handle conversions.
- **Usage**: Simplifies the service layer by handling repetitive mapping logic.

## Logging Aspect

The application uses SLF4J with Logback for logging, and Aspect-Oriented Programming (AOP) is applied to log method executions.

- **Logging Levels**: Configured appropriately (e.g., INFO, DEBUG, ERROR).
- **AOP**: Aspects are defined to log entry and exit points of critical methods.
- **Configuration**: Logback configuration is defined in `logback.xml`.

## Database Integration with MySQL

MySQL is used as the primary database for the application.

- **Configuration**: Database connection properties are specified in `application.properties`.
- **JPA Entities**: Defined for each table with appropriate relationships.
- **Database Migrations**: Managed using Flyway for version control.

## Development and Test Profiles

Spring Boot profiles are used to manage different configurations for development and testing environments.

- **application-dev.properties**: Contains configurations for the development environment.
- **application-test.properties**: Contains configurations for the test environment.
- **Activation**: Profiles can be activated via command-line arguments or environment variables.

## Docker Deployment

The application is containerized using Docker, facilitating easy deployment.

- **Dockerfile**: Defines the image build process.
- **Docker Compose**: `docker-compose.yml` orchestrates the application and its dependencies (e.g., MySQL).
- **Commands**:
  - Build the image: `docker build -t  aeon-library .`
  - Start the containers: `docker-compose up`

## CI/CD with GitHub Actions

Continuous Integration and Deployment are managed using GitHub Actions.

- **Workflow File**: Located at `.github/workflows/ci-cd.yml`.
- **Pipeline Steps**:
  - **Build**: Compiles the application.
  - **Test**: Runs unit and integration tests.
  - **Docker Build**: Builds the Docker image.
  - **Deployment**: Deploys the application to the specified environment.

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/asadmalik999/library.git
