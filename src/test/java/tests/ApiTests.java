package tests;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiTests {

    @Test
    void firstTest() {
        String users = get("https://reqres.in/api/users?page=2").asString();

        System.out.println(users);
    }

    @Test
    void secondTest() {
        String users = get("https://reqres.in/api/users?page=2").asString();

        assertTrue(users.length() == 0, "length: " + users.length() + "\n" +
                "data: " + users);
    }

    @Test
    void thirdTest() {
        String users = get("https://reqres.in/api/users?page=2").asString();

        assertThat(users.length(), is(not(nullValue())));
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
    void sixTest() { // такой же как fifthTest
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("total", is(12));
    }

    @Test
    void sevenTest() {
        String email = get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("data[2].email");

        assertThat(email, is("tobias.funke@reqres.in"));
    }


    @Test
    void eightTest() {
        String email = get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("ad.company");

        assertThat(email, is("StatusCode Weekly"));
    }

    @Test
    void nineTest() {
        String firstName = get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("data.first_name");

        assertThat(firstName, is("Janet"));
    }

    @Test
    void tenTest() {
        String firstName = get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404)
                .extract()
                .response()
                .path("data.first_name");

        assertThat(firstName, is(nullValue()));
    }

    @Test
    void elevenTest() {
        get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404)
                .body("data.first_name", is(nullValue()));
    }

    @Test
    void twelveTest() {
        // "email": "eve.holt@reqres.in",
        //    "password": "cityslicka"
        Response response =
                given()
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
    void thirteenTest() {
        // "email": "eve.holt@reqres.in",
        //    "password": "cityslicka"
        Response response =
                given()
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
    void fourteenTest() {
        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 5000));

        Response response =
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