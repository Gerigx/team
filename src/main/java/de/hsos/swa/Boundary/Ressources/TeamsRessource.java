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
import jakarta.ws.rs.Path;
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
    
    // team service
    @Inject
    private TeamService teamService;

    @Context
    private UriInfo uriInfo;

    // Verben



    /*
    @GET
    public Response getAllTeams(
        @QueryParam("filter[name]") String nameFilter,
        @QueryParam("filter[category]") String categoryFilter,
        @QueryParam("page[number]") @DefaultValue("1") int pageNumber,
        @QueryParam("page[size]") @DefaultValue("10") int pageSize,
        @QueryParam("include") String include) {
    
        try {
            // 1. Teams abrufen und filtern
            List<Team> teams = teamService.getAllTeams();
            
            // Name filtern
            if (nameFilter != null && !nameFilter.isEmpty()) {
                teams = teams.stream()
                        .filter(t -> t.getName().contains(nameFilter))
                        .collect(Collectors.toList());            
            }
            
            // Kategorie filtern
            if (categoryFilter != null && !categoryFilter.isEmpty()) {
                TeamCategory category = TeamCategory.valueOf(categoryFilter.toUpperCase());
                teams = teams.stream()
                        .filter(t -> t.getCategory() == category)
                        .collect(Collectors.toList());
            }
            
            // 2. Paginierung anwenden
            PaginationResult<Team> pageResult = applyPagination(teams, pageNumber, pageSize);
            
            // 3. JSON-API Response erstellen (ohne manuelle JSON Manipulation)
            JsonApiResponse<Team> response = new JsonApiResponse<>(
                pageResult.getItems(),
                createPaginationLinks(pageResult),
                createIncludedResources(pageResult.getItems(), include)
            );
            
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            // Quarkus Exception Mapping für ungültige Kategorie
            throw new BadRequestException("Ungültige Team-Kategorie: " + categoryFilter);
        }
    }

     */

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
             @QueryParam("category") TeamCategory category) {
         
         // Teams abrufen
         List<Team> allTeams = teamService.getAllTeams();
         
         // Filter anwenden, wenn vorhanden
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
         
         // Paginierung anwenden
         long totalTeams = allTeams.size();
         int totalPages = (int) Math.ceil((double) totalTeams / size);
         
         // Seitenbereich berechnen
         int fromIndex = page * size;
         List<Team> pagedTeams;
         
         if (fromIndex >= allTeams.size()) {
             pagedTeams = List.of(); // Leere Liste zurückgeben, wenn Seite außerhalb des Bereichs
         } else {
             int toIndex = Math.min(fromIndex + size, allTeams.size());
             pagedTeams = allTeams.subList(fromIndex, toIndex);
         }
         
         // Einfache Map als Antwort zusammenstellen
         Map<String, Object> result = new HashMap<>();
         result.put("teams", pagedTeams);
         result.put("page", page);
         result.put("size", size);
         result.put("totalItems", totalTeams);
         result.put("totalPages", totalPages);
         
         return RestResponse.ok(result);
     }

        

}
