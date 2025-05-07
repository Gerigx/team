package de.hsos.swa.Boundary.Ressources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import de.hsos.swa.Entity.Team;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

@Path("teams/{ID}")
public class TeamIDRessource {


    // get

    // post

    /*

        @PUT
    @Operation(summary = "Aktualisiert ein bestehendes Team")
    @APIResponse(responseCode = "200", description = "Team erfolgreich aktualisiert")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    @APIResponse(responseCode = "400", description = "Ungültige Daten")
    public RestResponse<Map<String, Object>> updateTeam(
            @Parameter(description = "ID des zu aktualisierenden Teams") 
            @PathParam("id") long id,
            Team updatedTeam,
            @Context UriInfo uriInfo) {
        
        // Prüfen, ob das Team existiert
        Team existingTeam = teamService.getTeamById(id);
        if (existingTeam == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Team mit ID " + id + " nicht gefunden");
            return RestResponse.notFound(errorResponse);
        }
        
        // ID des Pfads in das zu aktualisierende Team setzen
        updatedTeam.setId(id);
        
        // Team aktualisieren
        Team result = teamService.updateTeam(updatedTeam);
        
        // HATEOAS Links erstellen
        Map<String, Object> response = new HashMap<>();
        response.put("team", result);
        
        Map<String, String> links = new HashMap<>();
        links.put("self", uriInfo.getAbsolutePath().toString());
        links.put("all_teams", uriInfo.getBaseUriBuilder().path(TeamsRessource.class).build().toString());
        
        response.put("_links", links);
        
        return RestResponse.ok(response);
    }

     */

    // put

    // delete

}
