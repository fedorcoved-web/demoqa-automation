package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

public class ApiBaseTest {

    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        // Free key from https://reqres.in/signup; config.properties locally, REQRES_API_KEY on CI
        String apiKey = ConfigReader.get("reqres.api.key", "REQRES_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "reqres API key not found: set reqres.api.key in config.properties "
                    + "or the REQRES_API_KEY environment variable");
        }
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in/api")
                .addHeader("x-api-key", apiKey)
                .build();
    }
}
