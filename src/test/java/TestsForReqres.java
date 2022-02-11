import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;

public class TestsForReqres {

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void testForGetInfoAboutUser(){
        given()
                .filter(customLogFilter().withCustomTemplates())
                .when()
                .get("api/user/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.name", is("fuchsia rose"));
    }

    @Test
    void testForGetResourceNotFound(){
        given()
                .filter(customLogFilter().withCustomTemplates())
                .when()
                .get("/api/unknown/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    void testForPostUnsuccessfulRegister(){

        String data = "{\n" +
                "    \"email\": \"pupa@lupa\"\n" +
                "}";

        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body(data)
                .when()
                .post("api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void testForPatchUpdateUser(){

    String name = "Pupa";
    String job = "qa automation engineer";

        String data = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"job\": \"" + job + "\"\n" +
                "}";

       Response response = given()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("api/users/2")
                .then()
                .statusCode(200)
               .extract().response();

        assertThat(response.path("name").toString()).isEqualTo(name);
        assertThat(response.path("job").toString()).isEqualTo(job);
    }

    @Test
    void testForDeleteUser(){
        given()
                .filter(customLogFilter().withCustomTemplates())
                .delete("api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void testForGetInfoAboutUserWithSchema(){
        given()
                .filter(customLogFilter().withCustomTemplates())
                .when()
                .log().all()
                .get("api/user/2")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/schemaForApiTestGetInfoAboutUser.json"))
                .body("data.id", is(2));
    }
}
