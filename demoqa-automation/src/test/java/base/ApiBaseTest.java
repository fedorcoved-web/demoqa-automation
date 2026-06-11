package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeClass;

public class ApiBaseTest {

    // Paste your free API key from https://reqres.in/signup
    private static final String API_KEY = "free_user_3E2PocJT2PU5IFui7glhvdNfsT6";

    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in/api")
                .addHeader("x-api-key", API_KEY)
                .build();
    }
}
