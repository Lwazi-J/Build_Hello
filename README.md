# Build_Hello Project - Spring Boot Application

This project is a simple Spring Boot application that demonstrates basic REST API functionality for managing greetings. Below you'll find comprehensive instructions for setting up, running, and testing the application.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Project Setup](#project-setup)
3. [Running the Application](#running-the-application)
4. [API Endpoints](#api-endpoints)
5. [Testing with cURL](#testing-with-curl)
6. [Testing with Postman](#testing-with-postman)
7. [Project Structure](#project-structure)
8. [Troubleshooting](#troubleshooting)

   <a href='https://coveralls.io/github/Lwazi-J/Build_Hello?branch=master'><img src='https://coveralls.io/repos/github/Lwazi-J/Build_Hello/badge.svg?branch=master' alt='Coverage Status' /></a>


## Prerequisites

Before you begin, ensure you have the following installed:
- Java JDK 11 or later
- Maven 3.6.3 or later
- cURL (for command line testing)
- Postman (optional, for GUI testing)

## Project Setup

### 1. Create Project Folder

Open your command line interface (CLI) and run:

```bash
mkdir project_folder
cd project_folder
```

### 2. Generate Spring Boot Project

Run the following command to generate a Maven-based Spring Boot project with web and actuator dependencies:

```bash
curl https://start.spring.io/starter.tgz -d dependencies=web,actuator -d type=maven-project | tar -xzf -
```

This will create a basic Spring Boot project structure with:
- Maven build configuration
- Web and Actuator dependencies
- Default application properties

### 3. Build the Project

```bash
mvn clean install
```

## Running the Application

To start the application, run:

```bash
mvn spring-boot:run
```

The application will start on port 8080. You can verify it's running by visiting:
[http://localhost:8080](http://localhost:8080)

## API Endpoints

The application provides the following endpoints:

| Method | Endpoint              | Description                     |
|--------|-----------------------|---------------------------------|
| GET    | /hello                | Basic hello endpoint            |
| GET    | /hello/{name}         | Personalized hello              |
| POST   | /hello                | Create a new greeting           |
| GET    | /hello/all            | Get all greetings               |
| PUT    | /hello/{id}           | Update a greeting               |
| DELETE | /hello/{id}           | Delete a greeting               |

## Testing with cURL

### 1. Initial Setup
Ensure your application is running on http://localhost:8080

### 2. Basic Testing

#### Test Basic Hello Endpoint
```bash
curl http://localhost:8080/hello
```

#### Test Hello with Name
```bash
curl http://localhost:8080/hello/John
```

### 3. Greetings CRUD Operations

#### Create Greetings
```bash
curl -X POST http://localhost:8080/hello \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "message": "Hello from cURL!"
  }'
```

```bash
curl -X POST http://localhost:8080/hello \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane",
    "message": "Another test message"
  }'
```

#### Retrieve Greetings
```bash
# Get all greetings
curl http://localhost:8080/hello/all

# Get specific greeting (replace '1' with actual ID)
curl http://localhost:8080/hello/1
```

#### Update a Greeting
```bash
curl -X PUT http://localhost:8080/hello/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "message": "Updated message"
  }'
```

#### Delete a Greeting
```bash
curl -X DELETE http://localhost:8080/hello/1
```

## Testing with Postman

1. Create a new Collection called "Greetings API"
2. Set up the following requests:

### GET Requests

#### Get Hello
- Method: GET
- URL: `http://localhost:8080/hello`

#### Get Hello with Name
- Method: GET
- URL: `http://localhost:8080/hello/{{name}}`
- Create environment variable "name"

#### Get All Greetings
- Method: GET
- URL: `http://localhost:8080/hello/all`

#### Get Greeting by ID
- Method: GET
- URL: `http://localhost:8080/hello/{{id}}`
- Create environment variable "id"

### POST Request

#### Create Greeting
- Method: POST
- URL: `http://localhost:8080/hello`
- Headers: 
  - `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "name": "John",
  "message": "Test message"
}
```

### PUT Request

#### Update Greeting
- Method: PUT
- URL: `http://localhost:8080/hello/{{id}}`
- Headers: 
  - `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "name": "John Updated",
  "message": "Updated message"
}
```

### DELETE Request

#### Delete Greeting
- Method: DELETE
- URL: `http://localhost:8080/hello/{{id}}`

## Project Structure

```
project_folder/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── DemoApplication.java
│   │   │               ├── controller/
│   │   │               │   └── HelloController.java
│   │   │               ├── model/
│   │   │               │   └── Greeting.java
│   │   │               └── service/
│   │   │                   └── GreetingService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── demo/
│                       └── DemoApplicationTests.java
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Troubleshooting

1. **Application won't start**:
   - Verify Java and Maven are properly installed
   - Check for port conflicts (another app using port 8080)
   - Run `mvn clean install` before starting

2. **Endpoints not working**:
   - Ensure the application is running
   - Verify endpoint paths match your requests
   - Check for typos in cURL commands or Postman requests

3. **Dependency issues**:
   - Run `mvn dependency:resolve` to fix dependency problems
   - Delete the `target` folder and rebuild if needed
