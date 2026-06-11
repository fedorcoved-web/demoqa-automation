package tests;

import base.ApiBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("API Tests")
@Feature("User API")
public class UserApiTest extends ApiBaseTest {

    private static final Logger log = LogManager.getLogger(UserApiTest.class);

    @Story("Retrieve existing user by ID")
    @Description("GET /users/2 - existing user is returned with correct id")
    @Test(description = "GET /users/2 - existing user is returned with correct id",
            groups = {"smoke", "regression"})
    public void getUserTest() {
        log.info("Starting test: getUserTest");
        given()
            .when()
                .get("/users/2")
            .then()
                .statusCode(200)
                .body("data.id", equalTo(2));
        log.info("Test completed: getUserTest");
    }

    @Story("Request non-existent user and get 404")
    @Description("GET /users/999 - non-existent user returns 404")
    @Test(description = "GET /users/999 - non-existent user returns 404",
            groups = {"smoke", "regression"})
    public void getUserNotFoundTest() {
        log.info("Starting test: getUserNotFoundTest");
        given()
            .when()
                .get("/users/999")
            .then()
                .statusCode(404);
        log.info("Test completed: getUserNotFoundTest");
    }

    @Story("Create new user and verify name is echoed back")
    @Description("POST /users - new user is created and name is echoed back")
    @Test(description = "POST /users - new user is created and name is echoed back",
            groups = {"smoke", "regression"})
    public void createUserTest() {
        log.info("Starting test: createUserTest");
        Map<String, String> body = new HashMap<>();
        body.put("name", "Eduard");
        body.put("job", "QA Engineer");

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .body("name", equalTo("Eduard"));
        log.info("Test completed: createUserTest");
    }

    @Story("Delete user and verify 204 no content response")
    @Description("DELETE /users/2 - user is deleted with no content response")
    @Test(description = "DELETE /users/2 - user is deleted with no content response",
            groups = {"smoke", "regression"})
    public void deleteUserTest() {
        log.info("Starting test: deleteUserTest");
        given()
            .when()
                .delete("/users/2")
            .then()
                .statusCode(204);
        log.info("Test completed: deleteUserTest");
    }

    @Story("Update user job title")
    @Description("PATCH /users/2 - user job is updated and echoed back")
    @Test(description = "PATCH /users/2 - user job is updated and echoed back",
            groups = {"smoke", "regression"})
    public void patchUserTest() {
        log.info("Starting test: patchUserTest");
        Map<String, String> body = new HashMap<>();
        body.put("job", "Junior  QA Engineer");

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
                .patch("/users/2")
            .then()
                .statusCode(200)
                .body("job", equalTo("Junior  QA Engineer"));
        log.info("Test completed: patchUserTest");
    }
}
