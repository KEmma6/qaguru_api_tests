import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static filters.CustomLogFilter.customLogFilter;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class Specs {

   public static RequestSpecification requestSpecificationForReqres = new RequestSpecBuilder()
            .addFilter(customLogFilter().withCustomTemplates())
            .setContentType(JSON)
            .setBaseUri("https://reqres.in/")
            .setBasePath("api/")
            .log(ALL)
            .build();


    public static ResponseSpecification responseSpecificationForReqres = new ResponseSpecBuilder()
            .log(ALL)
            .build();
}
