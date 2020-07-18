package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Tag("api_tests")
class ApiTests {
    String users;
    String email;
    String firstName;
    Response response;
    RestAssuredConfig config;
    @Test
    void firstTest() {
        step("Simple get rest assured request", () -> {
            String users = get("https://reqres.in/api/users?page=2").asString();

            System.out.println(users);
        });
    }

    @Test
    void secondTest() {
        step("Assign users to get URI", ()->{
            users = get("https://reqres.in/api/users?page=2").asString();
        });
        step("assert that length of returned value is not equal to zero", ()-> {
//        assertTrue(users.length() == 0, "length: " + users.length() + "\n" +
            assertFalse(users.length() == 0, "length: " + users.length() + "\n" +
                    "data: " + users);
        });
        }

    @Test
    void thirdTest() {
        step("Assign the output of get as String", ()-> {
            String users = get("https://reqres.in/api/users?page=2").asString();
        });
        step("Assert that length of users is not equal to zero using is by hamcrest", ()-> {
            assertThat(users.length(), is(not(nullValue())));
        });
    }

    //"total": 12,
    @Test
    void fourthTest() {
        get("https://reqres.in/api/users?page=2")
                .then()
                .body("total", is(12));
    }

    @Test
    void fifthTest() { // как обычно все пишут
        Integer total = given()
                .filter(new AllureRestAssured())
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("total");

        assertThat(total, is(12));
    }

    @Test
    void sixthTest() { // такой же как fifthTest
        given()
                .filter(new AllureRestAssured())
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("total", is(12));
    }

    @Test
    void seventhTest() {
         email = get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("data[2].email");

        assertThat(email, is("tobias.funke@reqres.in"));
    }

    @Test
    void eighthTest() {
        email = get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("ad.company");

        assertThat(email, is("StatusCode Weekly"));
    }

    @Test
    void ninthTest() {
        firstName = get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("data.first_name");

        assertThat(firstName, is("Janet"));
    }

    @Test
    void tenthTest() {
        firstName = get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404)
                .extract()
                .response()
                .path("data.first_name");

        assertThat(firstName, is(nullValue()));
    }

    @Test
    void eleventhTest() {
        get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404)
                .body("data.first_name", is(nullValue()));
    }

    @Test
    void twelfthTest() {
        // "email": "eve.holt@reqres.in",
        //    "password": "cityslicka"
        response =
                given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String token = response
                .path("token");

        assertThat(token, is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void thirteenthTest() {
        // "email": "eve.holt@reqres.in",
        //    "password": "cityslicka"
        response =
                given()
                        .filter(new AllureRestAssured())
                        .contentType("application/json")
                        .body("{ \"email\": \"eve.holt@reqres.in\"}")
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .statusCode(400)
                        .extract()
                        .response();

        String error = response
                .path("error");

        assertThat(error, is("Missing password"));
    }

    @Test
    void fourteenthTest() {
        config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 5000));

        response =
                given()
                        .config(config)
                        .contentType("application/json")
                        .when()
                        .get("https://reqres.in/api/users?delay=3")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

    }

}