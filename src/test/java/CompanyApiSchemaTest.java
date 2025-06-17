import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class CompanyApiSchemaTest {

    private static final String BASE_URL = "https://fakerapi.it/api/v1/companies";
    private static final int TEST_QUANTITY = 10;

    @Test(description = "Validate company API response schema and ensure all IDs are not null")
    public void testCompanySchemaAndIds() {
        ValidatableResponse response = RestAssured.given()
                .baseUri(BASE_URL)
                .queryParam("_quantity", TEST_QUANTITY)
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/company-schema.json"));

        List<Map<String, Object>> companies = response.extract().jsonPath().getList("data");

        assertNotNull(companies, "Response data is null");
        assertEquals(companies.size(), TEST_QUANTITY,
                String.format("Expected %d companies but got %d", TEST_QUANTITY, companies.size()));

        for (int i = 0; i < companies.size(); i++) {
            Object id = companies.get(i).get("id");
            assertNotNull(id, "Company at index " + i + " has a null 'id'");
        }
    }
}
