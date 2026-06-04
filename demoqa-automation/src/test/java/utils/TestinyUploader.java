package utils;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;

public class TestinyUploader {

    private static final String BASE_URL   = "https://app.testiny.io/api/v1/testcase";
    private static final int    PROJECT_ID = 1;
    private static final int    FOLDER_ID  = 2;  // "DemoQA Automation"

    record TestCase(String title, String precondition, String steps, String description, String type, int priority) {}

    public static void main(String[] args) throws Exception {
        String     apiKey = loadApiKey();
        HttpClient client = HttpClient.newHttpClient();

        // ── Step 1: delete stale test cases ───────────────────────────────────
        int[] toDelete = {10, 11, 12, 13, 14, 15};
        System.out.println("Deleting " + toDelete.length + " stale test cases...");
        System.out.println("=".repeat(70));
        for (int id : toDelete) {
            deleteTestCase(client, apiKey, id);
            Thread.sleep(300);
        }

        // ── Step 2: upload all 21 test cases ──────────────────────────────────
        List<TestCase> cases = List.of(

            // ── TextBox ────────────────────────────────────────────────────────
            new TestCase(
                "Fill Text Box Form – Verify Output Section Displays All Values Correctly",
                "Browser is open and DemoQA Text Box page is accessible (https://demoqa.com/text-box)",
                "1. Navigate to Text Box page\n" +
                "2. Fill Full Name field\n" +
                "3. Fill Email field\n" +
                "4. Fill Current Address field\n" +
                "5. Fill Permanent Address field\n" +
                "6. Click Submit button",
                "Output section displays the submitted name, email, current address, " +
                "and permanent address.\n" +
                "Data-driven: 3 datasets (John Doe / Anna Smith / Ed).",
                "Functional", 3
            ),

            // ── CheckBox ───────────────────────────────────────────────────────
            new TestCase(
                "Select Home Checkbox – Verify Result Section Is Visible",
                "Browser is open and DemoQA Check Box page is accessible (https://demoqa.com/checkbox)",
                "1. Navigate to Check Box page\n" +
                "2. Select the Home checkbox (root node, visible without expanding)",
                "Result section is visible after selecting; first selected item in result is 'home'.",
                "Functional", 3
            ),

            // ── RadioButton ────────────────────────────────────────────────────
            new TestCase(
                "Select Impressive Radio Button – Verify Confirmation Message",
                "Browser is open and DemoQA Radio Button page is accessible (https://demoqa.com/radio-button)",
                "1. Navigate to Radio Button page\n" +
                "2. Click the 'Impressive' radio button",
                "Success message displays 'Impressive'.",
                "Functional", 3
            ),

            // ── PracticeForm ───────────────────────────────────────────────────
            new TestCase(
                "Submit Student Registration Form – Verify Confirmation Modal Appears",
                "Browser is open and DemoQA Practice Form page is accessible (https://demoqa.com/automation-practice-form)",
                "1. Navigate to Practice Form page\n" +
                "2. Fill First Name: John\n" +
                "3. Fill Last Name: Doe\n" +
                "4. Fill Email: john.doe@example.com\n" +
                "5. Select Male gender radio button\n" +
                "6. Fill Mobile: 1234567890\n" +
                "7. Select Sports hobby checkbox\n" +
                "8. Fill Current Address: 123 Main Street, Springfield\n" +
                "9. Click Submit button",
                "Submission confirmation modal is displayed.\n" +
                "Modal title is 'Thanks for submitting the form'.",
                "Functional", 3
            ),

            // ── Alerts: simple ─────────────────────────────────────────────────
            new TestCase(
                "Accept Simple Alert – Verify Alert Message Text",
                "Browser is open and DemoQA Alerts page is accessible (https://demoqa.com/alerts)",
                "1. Navigate to Alerts page\n" +
                "2. Click the simple alert button\n" +
                "3. Capture alert text\n" +
                "4. Accept the alert",
                "Alert message text is 'You clicked a button'.",
                "Functional", 3
            ),

            // ── Alerts: confirm dismiss ────────────────────────────────────────
            new TestCase(
                "Dismiss Confirm Alert – Verify Cancel Result",
                "Browser is open and DemoQA Alerts page is accessible (https://demoqa.com/alerts)",
                "1. Navigate to Alerts page\n" +
                "2. Click the confirm alert button\n" +
                "3. Dismiss the alert (click Cancel)",
                "Confirm result text contains 'Cancel'.",
                "Functional", 2
            ),

            // ── Alerts: prompt ─────────────────────────────────────────────────
            new TestCase(
                "Type Text Into Prompt Alert – Verify Echoed Result",
                "Browser is open and DemoQA Alerts page is accessible (https://demoqa.com/alerts)",
                "1. Navigate to Alerts page\n" +
                "2. Click the prompt alert button\n" +
                "3. Type 'TestUser' into the prompt alert\n" +
                "4. Accept the alert",
                "Prompt result text contains 'TestUser'.",
                "Functional", 3
            ),

            // ── BrowserWindows ─────────────────────────────────────────────────
            new TestCase(
                "Open New Tab – Verify Sample Page URL and Heading",
                "Browser is open and DemoQA Browser Windows page is accessible (https://demoqa.com/browser-windows)",
                "1. Navigate to Browser Windows page\n" +
                "2. Click New Tab button\n" +
                "3. Switch to the newly opened tab",
                "New tab URL contains 'sample'.\n" +
                "Page heading is 'This is a sample page'.",
                "Functional", 3
            ),

            // ── DatePicker ─────────────────────────────────────────────────────
            new TestCase(
                "Select Date Using Date Picker – Verify Input Field Value",
                "Browser is open and DemoQA Date Picker page is accessible (https://demoqa.com/date-picker)",
                "1. Navigate to Date Picker page\n" +
                "2. Select date '01/15/2025' using the date picker widget",
                "Date picker input field value equals '01/15/2025'.",
                "Functional", 3
            ),

            // ── Slider ─────────────────────────────────────────────────────────
            new TestCase(
                "Move Slider to Value 75 – Verify Display Updates",
                "Browser is open and DemoQA Slider page is accessible (https://demoqa.com/slider)",
                "1. Navigate to Slider page\n" +
                "2. Move the range slider to position 75",
                "Slider display value shows '75'.",
                "Functional", 3
            ),

            // ── DragAndDrop ────────────────────────────────────────────────────
            new TestCase(
                "Drag Element onto Drop Target – Verify Dropped State",
                "Browser is open and DemoQA Droppable page is accessible (https://demoqa.com/droppable)",
                "1. Navigate to Drag and Drop page\n" +
                "2. Drag the draggable element onto the droppable target area",
                "Droppable area text changes to 'Dropped!'.",
                "Functional", 3
            ),

            // ── BrokenImages ───────────────────────────────────────────────────
            new TestCase(
                "Verify at Least One Valid Image Loads on Broken Images Page",
                "Browser is open and DemoQA Broken Images page is accessible (https://demoqa.com/broken)",
                "1. Navigate to Broken Images page\n" +
                "2. Count total img elements on the page\n" +
                "3. Check that at least one image has naturalWidth > 0",
                "Page contains at least one img element.\n" +
                "At least one image loaded successfully (naturalWidth > 0).",
                "Functional", 2
            ),

            // ── UserApi: GET 200 ───────────────────────────────────────────────
            new TestCase(
                "GET Existing User by ID – Returns 200 with Correct Data",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Send GET request to /users/2\n" +
                "2. Verify response status code is 200\n" +
                "3. Verify response body field data.id equals 2",
                "Response status: 200.\nResponse body: data.id == 2.",
                "API", 3
            ),

            // ── UserApi: GET 404 ───────────────────────────────────────────────
            new TestCase(
                "GET Non-Existent User – Returns 404 Not Found",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Send GET request to /users/999\n" +
                "2. Verify response status code is 404",
                "Response status: 404 (user does not exist).",
                "API", 2
            ),

            // ── UserApi: POST ──────────────────────────────────────────────────
            new TestCase(
                "POST Create New User – Verify Name Echoed in Response",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Prepare request body: {\"name\": \"Eduard\", \"job\": \"QA Engineer\"}\n" +
                "2. Send POST request to /users with Content-Type application/json\n" +
                "3. Verify response status code is 201\n" +
                "4. Verify response body field name equals 'Eduard'",
                "Response status: 201.\nResponse body: name == 'Eduard'.",
                "API", 3
            ),

            // ── UserApi: DELETE ────────────────────────────────────────────────
            new TestCase(
                "DELETE User – Verify 204 No Content Response",
                "API is available and base URL is configured (https://reqres.in/api)",
                "1. Send DELETE request to /users/2\n" +
                "2. Verify response status code is 204",
                "Response status: 204 No Content.",
                "API", 3
            ),

            // ── SelectMenu ─────────────────────────────────────────────────────
            new TestCase(
                "Select Option from Old-Style Dropdown – Verify Active Selection",
                "Browser is open and DemoQA Select Menu page is accessible (https://demoqa.com/select-menu)",
                "1. Navigate to Select Menu page\n" +
                "2. Select 'Blue' from the old-style HTML select dropdown",
                "Selected dropdown option is 'Blue'.",
                "Functional", 3
            ),

            // ── WebTables: add ─────────────────────────────────────────────────
            new TestCase(
                "Add New Row – Verify Entry Appears in Table",
                "Browser is open and DemoQA Web Tables page is accessible (https://demoqa.com/webtables)",
                "1. Navigate to Web Tables page\n" +
                "2. Click Add button\n" +
                "3. Fill form: First Name=Alice, Last Name=Smith, Email=alice@test.com, Age=28, Salary=75000, Department=Engineering\n" +
                "4. Submit the form",
                "Newly added row with name 'Alice' is visible in the table.",
                "Functional", 3
            ),

            // ── WebTables: delete ──────────────────────────────────────────────
            new TestCase(
                "Delete Existing Row – Verify Entry Is Removed from Table",
                "Browser is open and DemoQA Web Tables page is accessible (https://demoqa.com/webtables)",
                "1. Navigate to Web Tables page\n" +
                "2. Search for 'Cierra'\n" +
                "3. Click Delete icon for the first matching row",
                "Entry 'Cierra' is no longer visible in the table after deletion.",
                "Functional", 3
            ),

            // ── WebTables: edit ────────────────────────────────────────────────
            new TestCase(
                "Edit Row Salary – Verify Updated Value Appears in Table",
                "Browser is open and DemoQA Web Tables page is accessible (https://demoqa.com/webtables)",
                "1. Navigate to Web Tables page\n" +
                "2. Search for 'Cierra'\n" +
                "3. Click Edit button for the matching row\n" +
                "4. Change salary to '11000'\n" +
                "5. Submit the form",
                "Updated salary value '11000' is visible in the web table.",
                "Functional", 3
            ),

            // ── UploadDownload ─────────────────────────────────────────────────
            new TestCase(
                "Upload File – Verify Filename Appears in UI",
                "Browser is open and DemoQA Upload page is accessible (https://demoqa.com/upload-download)",
                "1. Navigate to Upload & Download page\n" +
                "2. Upload file 'test-upload.txt' via the file upload input",
                "Uploaded filename 'test-upload.txt' appears in the UI after upload.",
                "Functional", 3
            )
        );

        System.out.println("\nUploading " + cases.size() + " test cases to Testiny " +
                "(project_id=" + PROJECT_ID + ", folder_id=" + FOLDER_ID + ")");
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

            Thread.sleep(300);
        }

        System.out.println("=".repeat(70));
        System.out.println("Done.");
    }

    private static void deleteTestCase(HttpClient client, String apiKey, int id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("X-API-KEY", apiKey)
                .DELETE()
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        int status = res.statusCode();
        if (status == 200 || status == 204) {
            System.out.printf("[DELETE] SUCCESS (%d): testcase id=%d%n", status, id);
        } else {
            System.out.printf("[DELETE] FAILED  (%d): testcase id=%d  ->  %s%n", status, id, res.body());
        }
    }

    private static String buildJson(TestCase tc) {
        return "{"
                + "\"project_id\":"         + PROJECT_ID           + ","
                + "\"testcase_folder_id\":" + FOLDER_ID            + ","
                + "\"title\":"              + js(tc.title())        + ","
                + "\"precondition_text\":"  + js(tc.precondition()) + ","
                + "\"steps_text\":"         + js(tc.steps())        + ","
                + "\"description\":"        + js(tc.description())  + ","
                + "\"custom_fields\":{"
                +   "\"Type\":"        + js(tc.type())    + ","
                +   "\"Automation\":\"Automated\","
                +   "\"Priority\":"    + tc.priority()
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
