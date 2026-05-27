package utils;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;

public class TestinyUploader {

    private static final String BASE_URL  = "https://app.testiny.io/api/v1/testcase";
    private static final int    PROJECT_ID = 1;
    private static final int    FOLDER_ID  = 2;  // "DemoQA Automation"

    record TestCase(String title, String precondition, String steps, String description, String type) {}

    public static void main(String[] args) throws Exception {
        String apiKey = loadApiKey();
        HttpClient client = HttpClient.newHttpClient();

        List<TestCase> cases = List.of(

            // ── UserApiTest ────────────────────────────────────────────────────────
            new TestCase(
                "Get User - Existing User Is Returned With Correct Id",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Send GET request to /users/2\n" +
                "2. Verify response status code is 200\n" +
                "3. Verify response body field data.id equals 2",
                "Endpoint: GET /users/2\n" +
                "Expected status: 200\n" +
                "Expected body: data.id == 2",
                "API"
            ),
            new TestCase(
                "Get User Not Found - Non-Existent User Returns 404",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Send GET request to /users/999\n" +
                "2. Verify response status code is 404",
                "Endpoint: GET /users/999\n" +
                "Expected status: 404 (user does not exist)",
                "API"
            ),
            new TestCase(
                "Create User - New User Is Created And Name Is Echoed Back",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Prepare request body with name=Eduard and job=QA Engineer\n" +
                "2. Send POST request to /users with JSON content type\n" +
                "3. Verify response status code is 201\n" +
                "4. Verify response body field name equals Eduard",
                "Endpoint: POST /users\n" +
                "Request body: { \"name\": \"Eduard\", \"job\": \"QA Engineer\" }\n" +
                "Expected status: 201\n" +
                "Expected body: name == Eduard",
                "API"
            ),
            new TestCase(
                "Delete User - User Is Deleted With No Content Response",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Send DELETE request to /users/2\n" +
                "2. Verify response status code is 204 (No Content)",
                "Endpoint: DELETE /users/2\n" +
                "Expected status: 204 No Content",
                "API"
            ),

            // ── TextBoxTest ────────────────────────────────────────────────────────
            new TestCase(
                "Fill Text Box Form - Verify Output Section Displays All Values Correctly",
                "Browser is open and DemoQA website is accessible (https://demoqa.com/text-box)",
                "1. Navigate to Text Box page\n" +
                "2. Fill in Full Name\n" +
                "3. Fill in Email\n" +
                "4. Fill in Current Address\n" +
                "5. Fill in Permanent Address\n" +
                "6. Click Submit\n" +
                "7. Verify output Name contains submitted value\n" +
                "8. Verify output Email contains submitted value\n" +
                "9. Verify output Current Address contains submitted value\n" +
                "10. Verify output Permanent Address contains submitted value",
                "Data-driven test (3 sets):\n" +
                "Set 1: Name=John Doe | Email=john.doe@example.com | Current=123 Main Street | Permanent=456 Oak Avenue\n" +
                "Set 2: Name=Anna Smith | Email=anna.smith@example.com | Current=789 Pine Road | Permanent=321 Elm Street\n" +
                "Set 3: Name=Ed | Email=ed@uman.com | Current=Uman | Permanent=Uman\n" +
                "URL: https://demoqa.com/text-box",
                "Functional"
            ),

            // ── PracticeFormTest ───────────────────────────────────────────────────
            new TestCase(
                "Fill Student Registration Form - Verify Submission Confirmation Modal Appears",
                "Browser is open and DemoQA website is accessible (https://demoqa.com/automation-practice-form)",
                "1. Navigate to Practice Form page\n" +
                "2. Fill First Name: John\n" +
                "3. Fill Last Name: Doe\n" +
                "4. Fill Email: john.doe@example.com\n" +
                "5. Select Male gender radio button\n" +
                "6. Fill Mobile: 1234567890\n" +
                "7. Select Sports hobby checkbox\n" +
                "8. Fill Current Address: 123 Main Street, Springfield\n" +
                "9. Click Submit\n" +
                "10. Verify confirmation modal is displayed\n" +
                "11. Verify modal title text is 'Thanks for submitting the form'",
                "First Name: John | Last Name: Doe | Email: john.doe@example.com\n" +
                "Gender: Male | Mobile: 1234567890 | Hobbies: Sports\n" +
                "Address: 123 Main Street, Springfield\n" +
                "Expected modal title: Thanks for submitting the form\n" +
                "URL: https://demoqa.com/automation-practice-form",
                "Functional"
            )
        );

        System.out.println("Uploading " + cases.size() + " test cases to Testiny (project_id=" + PROJECT_ID + ", folder_id=" + FOLDER_ID + ")");
        System.out.println("=".repeat(70));

        int num = 0;
        for (TestCase tc : cases) {
            num++;
            String body = buildJson(tc);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("X-API-KEY", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status == 200 || status == 201) {
                System.out.printf("[%d/%d] SUCCESS (%d): %s%n", num, cases.size(), status, tc.title());
            } else {
                System.out.printf("[%d/%d] FAILED  (%d): %s%n", num, cases.size(), status, tc.title());
                System.out.println("        Response: " + response.body());
            }

            Thread.sleep(500);
        }

        System.out.println("=".repeat(70));
        System.out.println("Done.");
    }

    private static String buildJson(TestCase tc) {
        return "{"
                + "\"project_id\":"         + PROJECT_ID          + ","
                + "\"testcase_folder_id\":" + FOLDER_ID           + ","
                + "\"title\":"              + js(tc.title())       + ","
                + "\"precondition_text\":"  + js(tc.precondition()) + ","
                + "\"steps_text\":"         + js(tc.steps())       + ","
                + "\"description\":"        + js(tc.description()) + ","
                + "\"custom_fields\":{"
                +   "\"Type\":"        + js(tc.type())     + ","
                +   "\"Automation\":\"Automated\","
                +   "\"Priority\":2"
                + "}"
                + "}";
    }

    private static String loadApiKey() throws Exception {
        Properties props = new Properties();
        try (InputStream in = TestinyUploader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new RuntimeException("config.properties not found on classpath");
            props.load(in);
        }
        String key = props.getProperty("testiny.api.key");
        if (key == null || key.isBlank()) throw new RuntimeException("testiny.api.key not set in config.properties");
        return key;
    }

    private static String js(String value) {
        if (value == null) return "null";
        return "\""
                + value
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t")
                + "\"";
    }
}
