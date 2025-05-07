package de.hsos.swa.Boundary.Ressources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import de.hsos.swa.Boundary.ACL.TeamDTO;
import de.hsos.swa.Controller.TeamService;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.Team;
import de.hsos.swa.Entity.TeamCategory;
import de.hsos.swa.Entity.TeamType;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;


@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamsRessource {
    
    @Inject
    private TeamService teamService;

     @GET
     @Operation(summary = "Gibt eine paginierte Liste aller Teams zurück")
     public RestResponse<Map<String, Object>> getAllTeams(
             @Parameter(description = "Seitennummer (beginnt bei 0)")
             @QueryParam("page") @DefaultValue("0") int page,
             
             @Parameter(description = "Anzahl der Teams pro Seite")
             @QueryParam("size") @DefaultValue("10") int size,
             
             @Parameter(description = "Filtern nach Team-Typ")
             @QueryParam("type") TeamType type,
             
             @Parameter(description = "Filtern nach Team-Kategorie")
             @QueryParam("category") TeamCategory category,
             @Context UriInfo uriInfo) {
         
         List<Team> allTeams = teamService.getAllTeams();

         if (type != null) {
             allTeams = allTeams.stream()
                     .filter(team -> team.getType() == type)
                     .collect(Collectors.toList());
         }
         
         if (category != null) {
             allTeams = allTeams.stream()
                     .filter(team -> team.getCategory() == category)
                     .collect(Collectors.toList());
         }
         
         long totalTeams = allTeams.size();
         int totalPages = (int) Math.ceil((double) totalTeams / size);
         
         int fromIndex = page * size;
         List<Team> pagedTeams;
         
         if (fromIndex >= allTeams.size()) {
             pagedTeams = List.of(); 
         } else {
             int toIndex = Math.min(fromIndex + size, allTeams.size());
             pagedTeams = allTeams.subList(fromIndex, toIndex);
         }
         
         // Team rein
         Map<String, Object> result = new HashMap<>();
         result.put("teams", pagedTeams);
         result.put("page", page);
         result.put("size", size);
         result.put("totalItems", totalTeams);
         result.put("totalPages", totalPages);

        // Jetzt Links
        Map<String, String> links = new HashMap<>();
        links.put("self", uriInfo.getRequestUri().toString());
        
        // seite 1
        UriBuilder firstPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", 0);
        links.put("first", firstPageUri.build().toString());
        
        // Seite ende
        int lastPage = Math.max(0, totalPages - 1);
        UriBuilder lastPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", lastPage);
        links.put("last", lastPageUri.build().toString());
        
        // nexxxxxt
        if (page < lastPage) {
            UriBuilder nextPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page + 1);
            links.put("next", nextPageUri.build().toString());
        }
        
        // prev
        if (page > 0) {
            UriBuilder prevPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page - 1);
            links.put("prev", prevPageUri.build().toString());
        }

        result.put("_links", links);
         
         return RestResponse.ok(result);
     }


     // Post

    @POST
    @Operation(summary = "Erstellt ein neues Team")
    @APIResponse(responseCode = "201", description = "Team erfolgreich erstellt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabedaten")
    public RestResponse<Map<String, Object>> createTeam(
            TeamDTO teamDTO,
            @Context UriInfo uriInfo) {
        
        if (teamDTO == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Teamdaten fehlen");
            return RestResponse.status(Response.Status.BAD_REQUEST, errorResponse);
        }
        
        if (teamDTO.getName() == null || teamDTO.getName().trim().isEmpty() || 
            teamDTO.getType() == null || 
            teamDTO.getCategory() == null || 
            teamDTO.getManager() == null) {
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erforderliche Felder fehlen (Name, Typ, Kategorie oder Manager)");
            return RestResponse.status(Response.Status.BAD_REQUEST, errorResponse);
        }

        Team createdTeam = teamService.createTeam(teamDTO);
        
        // Link
        Map<String, Object> response = new HashMap<>();
        response.put("team", createdTeam);
        
        Map<String, String> links = new HashMap<>();
        String teamUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(createdTeam.getId()))
                .build().toString();
        
        links.put("self", teamUri);
        links.put("all_teams", uriInfo.getAbsolutePath().toString());
        
        response.put("_links", links);
        
        return RestResponse.status(Response.Status.CREATED, response);
    }



        

}
