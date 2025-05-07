package de.hsos.swa;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsos.swa.Boundary.ACL.PersonDTO;
import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import io.quarkus.test.junit.QuarkusTest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


@QuarkusTest
public class PersonIDRessourceTest {

        private Long personId;
    
    @BeforeEach
    public void setup() {
        Person person = new Person();
        person.setName("Test");
        person.setLastname("Person");
        person.setGender(Gender.FEMALE);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(person)
            .when()
            .post("/persons")
            .then()
            .statusCode(201)
            .extract().response();


            Integer idAsInteger = response.path("person.id");
            personId = idAsInteger.longValue();
    }
    
    @Test
    public void testGetPerson() {
        given()
            .pathParam("id", personId)
            .when()
            .get("/persons/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("person.id", equalTo(personId.intValue())) 
            .body("person.name", equalTo("Test"))
            .body("person.lastname", equalTo("Person"))
            .body("person.gender", equalTo("FEMALE"))
            .body("_links.self", notNullValue())
            .body("_links.all_persons", notNullValue());
    }
    
    // falsche ID
    @Test
    public void testGetPersonNotFound() {
        given()
            .pathParam("id", 999999L) 
            .when()
            .get("/persons/{id}")
            .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }
    
    @Test
    public void testUpdatePerson() {
        Person updatePerson = new Person();
        updatePerson.setId(personId); 
        updatePerson.setName("Updated");
        updatePerson.setLastname("Name");
        updatePerson.setGender(Gender.MALE);
        
        given()
            .pathParam("id", personId)  
            .contentType(ContentType.JSON)
            .body(updatePerson)
            .when()
            .put("/persons/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("person.id", notNullValue())
            .body("person.name", equalTo("Updated"))
            .body("person.lastname", equalTo("Name"))
            .body("person.gender", equalTo("MALE"))
            .body("_links.self", notNullValue())
            .body("_links.all_persons", notNullValue());
    }
    
    // flasche ID
    @Test
    public void testUpdatePersonNotFound() {
        Person update = new Person();
        update.setName("Updated");
        update.setLastname("Name");
        update.setGender(Gender.MALE);
        
        given()
            .pathParam("id", 999999L)
            .contentType(ContentType.JSON)
            .body(update)
            .when()
            .put("/persons/{id}")
            .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }
    
    @Test
    public void testUpdatePersonMissingFields() {
        Map<String, Object> incompleteData = new HashMap<>();
        incompleteData.put("name", "Updated");
        // lastname and gender missing
        
        given()
            .pathParam("id", personId)
            .contentType(ContentType.JSON)
            .body(incompleteData)
            .when()
            .put("/persons/{id}")
            .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }
    
    @Test
    public void testDeletePerson() {
        given()
            .pathParam("id", personId)
            .when()
            .delete("/persons/{id}")
            .then()
            .statusCode(204);
        
        // Verifizieren, dass die Person gelöscht wurde
        given()
            .pathParam("id", personId)
            .when()
            .get("/persons/{id}")
            .then()
            .statusCode(404);
    }
    
    @Test
    public void testDeletePersonNotFound() {
        given()
            .pathParam("id", 999999L) // ID, die es nicht gibt
            .when()
            .delete("/persons/{id}")
            .then()
            .statusCode(404);
    }

}
