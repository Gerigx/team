package de.hsos.swa.Boundary.Ressources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import de.hsos.swa.Boundary.ACL.PersonDTO;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.PlayerPassService;
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

@Path("persons/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonIDRessource {
        
    @Inject
    private PlayerPassService passService;
    
    @GET
    @Operation(summary = "Ruft eine Person anhand ihrer ID ab")
    @APIResponse(responseCode = "200", description = "Person erfolgreich abgerufen")
    @APIResponse(responseCode = "404", description = "Person nicht gefunden")
    public RestResponse<Map<String, Object>> getPerson(
            @PathParam("id") long id,
            @Context UriInfo uriInfo) {
        
        Person person = passService.getPerson(id);
        
        if (person == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Person mit ID " + id + " nicht gefunden");
            return RestResponse.status(Response.Status.NOT_FOUND, errorResponse);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("person", person);
        
        // Links 
        Map<String, String> links = new HashMap<>();
        links.put("self", uriInfo.getRequestUri().toString());
        links.put("all_persons", uriInfo.getBaseUriBuilder().path("persons").build().toString());
        
        response.put("_links", links);
        
        return RestResponse.ok(response);
    }
    
    @PUT
    @Operation(summary = "Aktualisiert eine Person anhand ihrer ID")
    @APIResponse(responseCode = "200", description = "Person erfolgreich aktualisiert")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabedaten")
    @APIResponse(responseCode = "404", description = "Person nicht gefunden")
    public RestResponse<Map<String, Object>> updatePerson(
            @PathParam("id") long id,
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
        
        Person existingPerson = passService.getPerson(id);
        if (existingPerson == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Person mit ID " + id + " nicht gefunden");
            return RestResponse.status(Response.Status.NOT_FOUND, errorResponse);
        }
        
        Person updatedPerson = passService.updatePerson(person);
        
        Map<String, Object> response = new HashMap<>();
        response.put("person", updatedPerson);
        
        // Links 
        Map<String, String> links = new HashMap<>();
        links.put("self", uriInfo.getRequestUri().toString());
        links.put("all_persons", uriInfo.getBaseUriBuilder().path("persons").build().toString());
        
        response.put("_links", links);
        
        return RestResponse.ok(response);
    }
    
    @DELETE
    @Operation(summary = "Löscht eine Person anhand ihrer ID")
    @APIResponse(responseCode = "204", description = "Person erfolgreich gelöscht")
    @APIResponse(responseCode = "404", description = "Person nicht gefunden")
    public RestResponse<Void> deletePerson(
            @PathParam("id") long id) {
        
        // Prüfe, ob die Person existiert
        Person existingPerson = passService.getPerson(id);
        if (existingPerson == null) {
            return RestResponse.status(Response.Status.NOT_FOUND);
        }
        
        boolean deleted = passService.deletePerson(id);
        
        if (deleted) {
            return RestResponse.noContent();
        } else {
            return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
