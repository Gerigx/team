package de.hsos.swa;

import org.junit.jupiter.api.Test;

import de.hsos.swa.Boundary.ACL.TeamDTO;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.TeamCategory;
import de.hsos.swa.Entity.TeamType;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import io.restassured.http.ContentType;

@QuarkusTest
public class TeamsRessourceTest {

        @Test
    public void testGetAllTeams() {
        given()
            .when()
            .get("/teams")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("teams", notNullValue())
            .body("page", is(0))
            .body("size", is(10))
            .body("totalItems", notNullValue())
            .body("totalPages", notNullValue())
            .body("_links", notNullValue())
            .body("_links.self", notNullValue());
    }
    
    @Test
    public void testGetAllTeamsWithPagination() {
        given()
            .queryParam("page", 1)
            .queryParam("size", 5)
            .when()
            .get("/teams")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("page", is(1))
            .body("size", is(5))
            .body("_links.prev", notNullValue());
    }
    
    @Test
    public void testGetAllTeamsWithTypeFilter() {
        given()
            .queryParam("type", "TEAM")
            .when()
            .get("/teams")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }
    
    @Test
    public void testGetAllTeamsWithCategoryFilter() {
        given()
            .queryParam("category", "ADULTS")
            .when()
            .get("/teams")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }
    
    @Test
    public void testCreateTeam() {
        // TeamDTO erstellen
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Test Team");
        teamDTO.setType(TeamType.TEAM);
        teamDTO.setCategory(TeamCategory.ADULTS);
        
        // Manager erstellen
        Person manager = new Person();
        manager.setId(1L);
        manager.setName("Manager");
        manager.setLastname("Test");
        
        teamDTO.setManager(manager);
        
        given()
            .contentType(ContentType.JSON)
            .body(teamDTO)
            .when()
            .post("/teams")
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("team.id", notNullValue())
            .body("team.name", equalTo("Test Team"))
            .body("team.type", equalTo("TEAM"))
            .body("team.category", equalTo("ADULTS"))
            .body("team.manager.id", equalTo(1))
            .body("_links.self", notNullValue())
            .body("_links.all_teams", notNullValue());
    }
    
    @Test
    public void testCreateTeamMissingRequiredFields() {
        // Manager fehlt
        TeamDTO incompleteTeamDTO = new TeamDTO();
        incompleteTeamDTO.setName("Incomplete Team");
        incompleteTeamDTO.setType(TeamType.TEAM);
        incompleteTeamDTO.setCategory(TeamCategory.SENIORS);
        // Kein Manager
        
        given()
            .contentType(ContentType.JSON)
            .body(incompleteTeamDTO)
            .when()
            .post("/teams")
            .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }
    
    @Test
    public void testCreateTeamEmptyName() {
        // Leerer Name
        TeamDTO teamWithEmptyName = new TeamDTO();
        teamWithEmptyName.setName("");
        teamWithEmptyName.setType(TeamType.TEAM);
        teamWithEmptyName.setCategory(TeamCategory.SENIORS);
        
        Person manager = new Person();
        manager.setId(1L);
        teamWithEmptyName.setManager(manager);
        
        given()
            .contentType(ContentType.JSON)
            .body(teamWithEmptyName)
            .when()
            .post("/teams")
            .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("error", notNullValue());
    }

}
