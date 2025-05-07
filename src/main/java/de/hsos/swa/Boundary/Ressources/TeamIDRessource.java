package de.hsos.swa.Boundary.Ressources;



import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import de.hsos.swa.Boundary.ACL.PersonDTO;
import de.hsos.swa.Controller.TeamService;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.PlayerPassService;
import de.hsos.swa.Entity.Team;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.MediaType;


@Path("teams/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamIDRessource {

    @Inject
    private TeamService teamService;
    
    @GET
    @Operation(summary = "Gibt ein Team anhand seiner ID zurück")
    @APIResponse(responseCode = "200", description = "Team erfolgreich gefunden")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public RestResponse<Map<String, Object>> getTeam(
            @Parameter(description = "ID des Teams") 
            @PathParam("id") long id,
            @Context UriInfo uriInfo) {
        
        Team team = teamService.getTeamById(id);
        if (team == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Team mit ID " + id + " nicht gefunden");
            return RestResponse.status(Response.Status.NOT_FOUND, errorResponse);
        }
        
        // Links
        Map<String, Object> response = new HashMap<>();
        response.put("team", team);
        
        Map<String, String> links = new HashMap<>();
        links.put("self", uriInfo.getAbsolutePath().toString());
        links.put("all_teams", uriInfo.getBaseUriBuilder().path(TeamsRessource.class).build().toString());
        
        // update links
        links.put("update", uriInfo.getAbsolutePath().toString());
        
        // dellll
        links.put("delete", uriInfo.getAbsolutePath().toString());
        
        response.put("_links", links);
        
        return RestResponse.ok(response);
    }

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
        
        Team existingTeam = teamService.getTeamById(id);
        if (existingTeam == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Team mit ID " + id + " nicht gefunden");
            return RestResponse.status(Response.Status.NOT_FOUND, errorResponse);
        }
        
        updatedTeam.setId(id);
        
        Team result = teamService.updateTeam(updatedTeam);
        if (result == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Team konnte nicht aktualisiert werden");
            return RestResponse.status(Response.Status.BAD_REQUEST, errorResponse);
        }
        
        //links
        Map<String, Object> response = new HashMap<>();
        response.put("team", result);
        
        Map<String, String> links = new HashMap<>();
        links.put("self", uriInfo.getAbsolutePath().toString());
        links.put("all_teams", uriInfo.getBaseUriBuilder().path(TeamsRessource.class).build().toString());
        
        response.put("_links", links);
        
        return RestResponse.ok(response);
    }

    @DELETE
    @Operation(summary = "Löscht ein Team anhand seiner ID")
    @APIResponse(responseCode = "204", description = "Team erfolgreich gelöscht")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public RestResponse<Map<String, Object>> deleteTeam(
            @Parameter(description = "ID des zu löschenden Teams") 
            @PathParam("id") long id,
            @Context UriInfo uriInfo) {
        
        Team existingTeam = teamService.getTeamById(id);
        if (existingTeam == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Team mit ID " + id + " nicht gefunden");
            return RestResponse.status(Response.Status.NOT_FOUND, errorResponse);
        }

        boolean deleted = teamService.deleteTeam(id);
        if (!deleted) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Team konnte nicht gelöscht werden");
            return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        }
        
        return RestResponse.noContent();
    }
}
