import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CompanyApiTest {

    private static final String BASE_URL = "https://fakerapi.it/api/v1/companies";

    @Test(description = "Check if the API returns the correct number of companies for different _quantity values")
    public void testCompanyQuantityResponse() {
        int[] testQuantities = {20, 5, 1};

        for (int quantity : testQuantities) {
            verifyCompanyQuantity(quantity);
        }
    }

    private void verifyCompanyQuantity(int expectedCount) {
        ValidatableResponse response = RestAssured.given()
                .baseUri(BASE_URL)
                .queryParam("_quantity", expectedCount)
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200);

        int actualCount = response.extract().jsonPath().getList("data").size();

        // Quick sanity check on returned data count
        assertEquals(actualCount, expectedCount,
                String.format("Expected %d items, but got %d", expectedCount, actualCount));
    }
}
