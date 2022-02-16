import io.restassured.response.Response;
import lombokTest.ResourceForReqres;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestsForReqres {

    @Test
    void testForGetInfoAboutUser(){
        given()
                .spec(Specs.requestSpecificationForReqres)
                .when()
                .get("user/2")
                .then()
                .spec(Specs.responseSpecificationForReqres)
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.name", is("fuchsia rose"));
    }

    @Test
    void testForGetResourceNotFound(){
        given()
                .spec(Specs.requestSpecificationForReqres)
                .when()
                .get("unknown/23")
                .then()
                .spec(Specs.responseSpecificationForReqres)
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    void testForPostUnsuccessfulRegister(){

        String data = "{\n" +
                "    \"email\": \"pupa@lupa\"\n" +
                "}";

        given()
                .spec(Specs.requestSpecificationForReqres)
                .body(data)
                .when()
                .post("register")
                .then()
                .spec(Specs.responseSpecificationForReqres)
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
                .spec(Specs.requestSpecificationForReqres)
                .body(data)
                .when()
                .patch("users/2")
                .then()
                .statusCode(200)
                .spec(Specs.responseSpecificationForReqres)
                .extract().response();

        assertThat(response.path("name").toString()).isEqualTo(name);
        assertThat(response.path("job").toString()).isEqualTo(job);
    }

    @Test
    void testForDeleteUser(){
        given()
                .spec(Specs.requestSpecificationForReqres)
                .delete("users/2")
                .then()
                .spec(Specs.responseSpecificationForReqres)
                .statusCode(204);
    }

    @Test
    void testForGetInfoAboutUserWithSchema(){
        given()
                .spec(Specs.requestSpecificationForReqres)
                .when()
                .get("user/2")
                .then()
                .spec(Specs.responseSpecificationForReqres)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/schemaForApiTestGetInfoAboutUser.json"))
                .body("data.id", is(2));
    }

    @Test
    void testForGetListOfUsersWithGPath(){
        given()
                .spec(Specs.requestSpecificationForReqres)
                .get("users?page=1")
                .then()
                .body("data.findAll{it.id}.id", hasSize(6))
                .body("data.findAll{it.last_name.contains('W')}.last_name.flatten()", hasSize(2));
    }

    @Test
    void testForGetListOfUsersWithLombok(){

        int indexResource = 1;

        ResourceForReqres resourceForReqres = given()
                .spec(Specs.requestSpecificationForReqres)
                .get("users?page=1")
                .then()
                .spec(Specs.responseSpecificationForReqres)
                .extract().as(ResourceForReqres.class);

        assertThat(resourceForReqres.getData().get(indexResource).getId()).isEqualTo(2);

    }

//    @Test
//    void singleUserWithLombokModel() {
//        // @formatter:off
//        LombokUserData data = given()
//                .spec(request)
//                .when()
//                .get("/users/2")
//                .then()
//                .spec(responseSpec)
//                .log().body()
//                .extract().as(LombokUserData.class);
//        // @formatter:on
//        assertEquals(2, data.getUser().getId());
//    }
}
