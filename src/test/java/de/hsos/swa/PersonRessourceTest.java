package de.hsos.swa;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.hsos.swa.Boundary.ACL.PersonDTO;
import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import jakarta.ws.rs.core.MediaType;



@QuarkusTest
public class PersonRessourceTest {

    @Test
    public void testGetAllPersons() {
        given()
            .when()
            .get("/persons")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("persons", notNullValue())
            .body("page", is(0))
            .body("size", is(10))
            .body("totalItems", notNullValue())
            .body("totalPages", notNullValue())
            .body("_links", notNullValue())
            .body("_links.self", notNullValue());
    }
    
    @Test
    public void testGetAllPersonsWithPagination() {
        given()
            .queryParam("page", 1)
            .queryParam("size", 5)
            .when()
            .get("/persons")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", is(1))
            .body("size", is(5))
            .body("_links.prev", notNullValue());
    }
    
    @Test
    public void testGetAllPersonsWithFilter() {
        given()
            .queryParam("gender", "MALE")
            .when()
            .get("/persons")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("persons.gender", hasItems("MALE"))
            .body("persons.gender.find { it != 'MALE' }", equalTo(null));
    }
    
    @Test
    public void testCreatePerson() {
        Person person = new Person();
        person.setName("Max");
        person.setLastname("Mustermann");
        person.setGender(Gender.MALE);
        
        given()
            .contentType(ContentType.JSON)
            .body(person)
            .when()
            .post("/persons")
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("person.id", notNullValue())
            .body("person.name", equalTo("Max"))
            .body("person.lastname", equalTo("Mustermann"))
            .body("person.gender", equalTo("MALE"))
            .body("_links.self", notNullValue())
            .body("_links.all_persons", notNullValue());
    }
    
    @Test
    public void testCreatePersonMissingFields() {
        Map<String, Object> incompleteData = new HashMap<>();
        incompleteData.put("name", "Max");
        // lastname and gender missing
        
        given()
            .contentType(ContentType.JSON)
            .body(incompleteData)
            .when()
            .post("/persons")
            .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }

}
