# PES University - Java Assignment

This is a Spring Boot application created for the Java assignment.

## ## Author
* **Name:** Sachin T
* **Reg No:** PES2UG22EC114

## ## Project Overview

This application automatically runs a task on startup. It:
1.  Sends a POST request to `/generateWebhook` to receive a task.
2.  Solves the assigned SQL problem (based on the `regNo`).
3.  Submits the final SQL query to the returned webhook URL using a JWT token for authorization.

## ## How to Run

[cite_start]This application is designed to run its main logic immediately on startup, as required by the assignment[cite: 609].

1.  Build the project using Maven: `./mvnw clean package`
2.  Run the generated JAR file: `java -jar target/pes-assignment-0.0.1-SNAPSHOT.jar`

[cite_start](No controllers or endpoints are needed to trigger the flow[cite: 609].)
