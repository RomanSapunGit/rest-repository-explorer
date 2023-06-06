# rest repository explorer

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [1.1 Obtaining Access Token](#11-obtaining-access-token)
  - [Running the Project](#running-the-project)
    - [Method 1: Using Git](#method-1-using-git)
    - [Method 2: Manual Setup](#method-2-manual-setup)
- [Testing](#testing)

## Introduction
The REST Task project retrieves all user's repositories (except for forks) along with their branches and last commit (sha).

## Features
- Exception handling: Allows for proper handling of exceptions without changing the service's code structure.
- Unit testing: Ensures expected behavior by testing against various scenarios, such as preventing the use of XML media type, checking for forks, and handling cases when the expected user cannot be found.
- Parallel processing: Utilizes parallel processing techniques to retrieve directories and branches faster than usual.

## Prerequisites
Before getting started, make sure you have the following:

- Set the environment variable ACCESS_TOKEN with your GitHub access token. This is required to increase the request limit and retrieve more user directories.

- Maven dependencies (The latest versions):
  - `hammock-resttemplate_2.13` for service and controller
  - `spring-boot-starter-web` for service and controller
  - `junit-jupiter-engine` for tests
  - `junit-jupiter-api` for tests
  - `spring-boot-test` for tests
  - `spring-test` for tests
  - `lombok` for "boilerplate" code

## Getting Started
Follow the steps below to set up and run the project.

### 1.1 Obtaining Access Token
To obtain the access token from GitHub, follow these steps:

1. Go to your GitHub profile settings.
2. Click on "Developer settings" located under the other options.
3. Select "Personal access tokens" -> "Generate new token".
4. Grant the following scopes: repo, user, workflow.

### Running the Project
You can run the project in two methods: using Git or manually.

#### Method 1: Using Git
1. Clone the repository to your preferred code editor using the `git clone` command.
2. Set the environment variable ACCESS_TOKEN with your GitHub access token.
3. Run the project.

#### Method 2: Manual Setup
1. Click on the "Code" button in the repository and select "Download ZIP".
2. Extract the ZIP file to your computer.
3. Open the extracted folder in your preferred text editor.
4. Set the environment variable ACCESS_TOKEN with your GitHub access token.
5. Run the project.

## Testing
The application includes comprehensive unit tests to ensure the correctness of its functionality.
The tests are implemented using the Spring Boot testing framework and the MockMvc library for simulating HTTP requests and verifying responses.

### To run the tests, follow these steps:

1. Ensure that the required environment variables are set, including ACCESS_TOKEN for increased request limits.
2. Make sure you have the necessary Maven dependencies specified in your project's pom.xml file.

### The testing content includes the following test cases:

- `testGetRepositories_NotForks`: This test verifies that the `getRepositories` method of the `GithubRepoService` retrieves all user repositories except for forks. It iterates over the repositories and asserts that none of them are marked as forks.

- `testGetRepositories_UserNotExist`: This test checks the behavior of the `getRepositories` method when a non-existing username is provided. It expects an `HttpClientErrorException` with a status code of `404` (NOT_FOUND) to be thrown.

- `testGetRepositories_SupportsXMLHeader_false`: This test validates the response of the `GithubRepoController` when a request is made with the Accept header set to `application/xml`. It sends a `GET` request to the `/api/repositories/{username}` endpoint with the specified header and expects a response with a status code of `406` (NOT_ACCEPTABLE) and a JSON error message indicating that no acceptable representation is available.

To execute the tests, you can run the following command:

`mvn test`

