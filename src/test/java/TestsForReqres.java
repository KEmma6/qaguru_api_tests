import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
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
                .when()
                .get("api/user/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"));
    }

    @Test
    void testForGetResourceNotFound(){
        given()
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

        assertThat((String) response.path("name")).isEqualTo(name);
        assertThat((String) response.path("job")).isEqualTo(job);
    }

    @Test
    void testForDeleteUser(){
        given()
                .delete("api/users/2")
                .then()
                .statusCode(204);
    }
}
