
package com.addressbook;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressBookJsonServerTest {

	@BeforeAll
	static void setup() {
		RestAssured.baseURI = "http://localhost:3000";
	}

	@Test
	public void givenJSONServer_whenContactsFetched_shouldReturnRecords() {

		Response response = RestAssured.get("/contacts");

		assertNotNull(response);
		assertEquals(200, response.getStatusCode());

		System.out.println(response.getBody().asPrettyString());
	}

	@Test
	public void givenNewContact_whenAddedToJsonServer_shouldReturnCreated() {

		String newContact = """
				{
				  "firstName": "Amit",
				  "lastName": "Sharma",
				  "address": "",
				  "city": "Mumbai",
				  "state": "MH",
				  "zip": "400001",
				  "phoneNumber": "7777777777",
				  "email": "amit@gmail.com"
				}
				""";

		Response response = RestAssured.given().contentType("application/json").body(newContact).when()
				.post("/contacts");

		assertEquals(201, response.getStatusCode());

		System.out.println(response.getBody().asPrettyString());
	}

	@Test
	public void givenExistingContact_whenUpdated_shouldReturnSuccess() {

		// First create a contact
		String newContact = """
				{
				  "firstName": "Temp",
				  "lastName": "User",
				  "address": "",
				  "city": "Delhi",
				  "state": "DL",
				  "zip": "110001",
				  "phoneNumber": "8888888888",
				  "email": "temp@gmail.com"
				}
				""";

		Response createResponse = RestAssured.given().contentType("application/json").body(newContact)
				.post("/contacts");

		String id = createResponse.jsonPath().getString("id");

		// Update the created contact
		String updatedContact = """
				{
				  "firstName": "Updated",
				  "lastName": "User",
				  "address": "",
				  "city": "Mumbai",
				  "state": "MH",
				  "zip": "411001",
				  "phoneNumber": "9999999999",
				  "email": "updated@gmail.com"
				}
				""";

		Response updateResponse = RestAssured.given().contentType("application/json").body(updatedContact)
				.put("/contacts/" + id);

		assertEquals(200, updateResponse.getStatusCode());
	}

	@Test
    public void givenExistingContact_whenDeleted_shouldReturnSuccess() {

        // First create a contact
        String newContact = """
            {
              "firstName": "Delete",
              "lastName": "User",
              "address": "",
              "city": "Pune",
              "state": "MH",
              "zip": "411001",
              "phoneNumber": "9999999999",
              "email": "delete@gmail.com"
            }
            """;

        Response createResponse =
                RestAssured
                        .given()
                        .contentType("application/json")
                        .body(newContact)
                        .post("/contacts");

        String id = createResponse.jsonPath().getString("id");

        // Delete the created contact
        Response deleteResponse =
                RestAssured
                        .given()
                        .delete("/contacts/" + id);

        assertEquals(200, deleteResponse.getStatusCode());

        System.out.println("Contact deleted successfully with id: " + id);
    }
}