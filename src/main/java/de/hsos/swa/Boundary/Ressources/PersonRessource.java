package de.hsos.swa.Boundary.Ressources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import de.hsos.swa.Boundary.ACL.PersonDTO;
import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.PlayerPassService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/persons")
public class PersonRessource {
        
    @Inject
    private PlayerPassService passService;

    @GET
    @Operation(summary = "Gibt eine paginierte Liste aller Personen zurück")
    public RestResponse<Map<String, Object>> getAllPersons(
            @Parameter(description = "Seitennummer (beginnt bei 0)")
            @QueryParam("page") @DefaultValue("0") int page,
            
            @Parameter(description = "Anzahl der Personen pro Seite")
            @QueryParam("size") @DefaultValue("10") int size,
            
            @Parameter(description = "Filtern nach Gender")
            @QueryParam("gender") Gender gender,
            
            @Context UriInfo uriInfo) {
        
        List<Person> allPersons = passService.getPersons();

        if (gender != null) {
            allPersons = allPersons.stream()
                    .filter(person -> person.getGender() == gender)
                    .collect(Collectors.toList());
        }
        
        // page

        long totalPersons = allPersons.size();
        int totalPages = (int) Math.ceil((double) totalPersons / size);
        
        int fromIndex = page * size;
        List<Person> pagedPersons;
        
        if (fromIndex >= allPersons.size()) {
            pagedPersons = List.of(); 
        } else {
            int toIndex = Math.min(fromIndex + size, allPersons.size());
            pagedPersons = allPersons.subList(fromIndex, toIndex);
        }
        
        // Personen ins Ergebnis
        Map<String, Object> result = new HashMap<>();
        result.put("persons", pagedPersons);
        result.put("page", page);
        result.put("size", size);
        result.put("totalItems", totalPersons);
        result.put("totalPages", totalPages);

        // Jetzt Links
        Map<String, String> links = new HashMap<>();
        links.put("self", uriInfo.getRequestUri().toString());
        
        // erste Seite
        UriBuilder firstPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", 0);
        links.put("first", firstPageUri.build().toString());
        
        // letzte Seite
        int lastPage = Math.max(0, totalPages - 1);
        UriBuilder lastPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", lastPage);
        links.put("last", lastPageUri.build().toString());
        
        // nächste Seite
        if (page < lastPage) {
            UriBuilder nextPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page + 1);
            links.put("next", nextPageUri.build().toString());
        }
        
        // vorherige Seite
        if (page > 0) {
            UriBuilder prevPageUri = uriInfo.getRequestUriBuilder().replaceQueryParam("page", page - 1);
            links.put("prev", prevPageUri.build().toString());
        }

        result.put("_links", links);
        
        return RestResponse.ok(result);
    }

    @POST
    @Operation(summary = "Erstellt eine neue Person")
    @APIResponse(responseCode = "201", description = "Person erfolgreich erstellt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabedaten")
    public RestResponse<Map<String, Object>> createPerson(
            Person person,
            @Context UriInfo uriInfo) {
        
        if (person == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Personendaten fehlen");
            return RestResponse.status(Response.Status.BAD_REQUEST, errorResponse);
        }
        
        if (person.getName() == null || person.getName().trim().isEmpty() || 
            person.getLastname() == null || person.getLastname().trim().isEmpty() || 
            person.getGender() == null) {
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erforderliche Felder fehlen (Name, Nachname oder Gender)");
            return RestResponse.status(Response.Status.BAD_REQUEST, errorResponse);
        }

        Person createdPerson = passService.createPerson(person);
        
        // Link
        Map<String, Object> response = new HashMap<>();
        response.put("person", createdPerson);
        
        Map<String, String> links = new HashMap<>();
        String personUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(createdPerson.getId()))
                .build().toString();
        
        links.put("self", personUri);
        links.put("all_persons", uriInfo.getAbsolutePath().toString());
        
        response.put("_links", links);
        
        return RestResponse.status(Response.Status.CREATED, response);
    }

}
