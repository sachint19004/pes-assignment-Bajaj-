package com.example.pes_assignment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Profile;

@Profile("!test") // Add this to prevent errors during the 'mvnw test' phase
@Component
public class AppRunner implements CommandLineRunner {

    // --- DTOs (Data Transfer Objects) defined here for clarity ---
    // These match the JSON structures for your API calls.
    record GenerateRequest(String name, String regNo, String email) {}
    record GenerateResponse(String webhook, String accessToken) {}
    record SubmitRequest(String finalQuery) {}

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Application has started! Running startup task...");

        // --- Step 1: Create RestTemplate and Request Body ---
        RestTemplate restTemplate = new RestTemplate();
        String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        // Your specific details
        GenerateRequest requestBody = new GenerateRequest(
            "Sachin T",
            "PES2UG22EC114",
            "sachint19122004@gmail.com"
        );

        // --- Step 2: Send First POST Request to get Webhook/Token ---
        System.out.println("Sending registration request...");
        ResponseEntity<GenerateResponse> responseEntity = restTemplate.postForEntity(
            generateUrl,
            requestBody,
            GenerateResponse.class
        );

        // --- Step 3: Extract Data from Response ---
        String webhookUrl = responseEntity.getBody().webhook();
        String accessToken = responseEntity.getBody().accessToken();

        System.out.println("Successfully received webhook URL: " + webhookUrl);
        System.out.println("Successfully received access token: " + accessToken.substring(0, 10) + "...");

        // --- Step 4: Define Your Final SQL Query ---
        // This is the solution for Question 2 (Even regNo ends in 114)
        String myFinalQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, (SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e1.DEPARTMENT AND e2.DOB > e1.DOB) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID ORDER BY e1.EMP_ID DESC;";
        
        System.out.println("SQL query is ready to be sent.");

        // --- Step 5: Prepare the Second POST Request (Submission) ---
        
        // Create the submission body
        SubmitRequest submitBody = new SubmitRequest(myFinalQuery);

        // Create headers and add the JWT Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        // Combine body and headers into an HttpEntity
        HttpEntity<SubmitRequest> submitRequestEntity = new HttpEntity<>(submitBody, headers);

        // --- Step 6: Send Second POST Request to Submit Solution ---
        System.out.println("Sending final SQL query to webhook...");

        ResponseEntity<String> submitResponse = restTemplate.exchange(
            webhookUrl,          // The URL you got from the first call
            HttpMethod.POST,
            submitRequestEntity, // Contains your SQL query and token
            String.class         // Expecting a simple String response
        );

        // --- Step 7: Finish ---
        System.out.println("Submission complete!");
        System.out.println("Server response: " + submitResponse.getBody());
    }
}