package de.hsos.swa;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsos.swa.Boundary.ACL.TeamDTO;
import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.Team;
import de.hsos.swa.Entity.TeamCategory;
import de.hsos.swa.Entity.TeamType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
public class TeamIDRessourceTest {
    private Long teamId;
    
    @BeforeEach
    public void setup() {
        Team team = new Team();
        team.setName("Test Team");
        team.setType(TeamType.TEAM);
        team.setCategory(TeamCategory.ADULTS);
        
        Person manager = new Person();
        manager.setId(1000L);
        manager.setName("Manager");
        manager.setLastname("Test");
        manager.setGender(Gender.NONBINARY);
        
        team.setManager(manager);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(team)
            .when()
            .post("/teams")
            .then()
            .statusCode(201)
            .extract().response();
        
        Integer idAsInteger = response.path("team.id");
        teamId = idAsInteger.longValue();
        
        System.out.println("Erstelltes Team mit ID: " + teamId);
    }
    
    @Test
    public void testGetTeam() {
        given()
            .pathParam("id", teamId)
            .when()
            .get("/teams/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("team.id", notNullValue())
            .body("team.name", notNullValue())
            .body("_links.self", notNullValue());
    }
    
    @Test
    public void testGetTeamNotFound() {
        given()
            .pathParam("id", 999999L)
            .when()
            .get("/teams/{id}")
            .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }
    
    @Test
    public void testUpdateTeam() {
        TeamDTO updateDTO = new TeamDTO();
        updateDTO.setName("Updated Team");
        updateDTO.setType(TeamType.TEAM);
        updateDTO.setCategory(TeamCategory.ADULTS);
        
        Person newManager = new Person();
        newManager.setId(2L);
        newManager.setName("New");
        newManager.setLastname("Manager");
        
        updateDTO.setManager(newManager);
        
        given()
            .pathParam("id", teamId)
            .contentType(ContentType.JSON)
            .body(updateDTO)
            .when()
            .put("/teams/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("team.id", notNullValue())
            .body("team.name", equalTo("Updated Team"))
            .body("team.type", equalTo("TEAM"))
            .body("team.category", equalTo("ADULTS"))
            .body("_links.self", notNullValue())
            .body("_links.all_teams", notNullValue());
    }
    
    @Test
    public void testUpdateTeamNotFound() {
        TeamDTO updateDTO = new TeamDTO();
        updateDTO.setName("Updated Team");
        updateDTO.setType(TeamType.TEAM);
        updateDTO.setCategory(TeamCategory.ADULTS);
        
        Person manager = new Person();
        manager.setId(2L);
        updateDTO.setManager(manager);
        
        given()
            .pathParam("id", 999999L)
            .contentType(ContentType.JSON)
            .body(updateDTO)
            .when()
            .put("/teams/{id}")
            .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }
    
    @Test
    public void testDeleteTeam() {
        given()
            .pathParam("id", teamId)
            .when()
            .delete("/teams/{id}")
            .then()
            .statusCode(204);
        
        // ist dem del
        given()
            .pathParam("id", teamId)
            .when()
            .get("/teams/{id}")
            .then()
            .statusCode(404);
    }
    
    @Test
    public void testDeleteTeamNotFound() {
        given()
            .pathParam("id", 999999L)
            .when()
            .delete("/teams/{id}")
            .then()
            .statusCode(404);
    }

}
