package de.hsos.swa.Boundary.ACL;

import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.TeamCategory;
import de.hsos.swa.Entity.TeamType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class TeamDTO {    
    @NotNull(message = "Team-Typ darf nicht null sein")
    @Schema(description = "Typ des Teams")
    private TeamType type;
    
    @NotBlank(message = "Team-Name darf nicht leer sein")
    @Schema(description = "Name des Teams")
    private String name;
    
    @NotNull(message = "Team-Kategorie darf nicht null sein")
    @Schema(description = "Kategorie des Teams")
    private TeamCategory category;
    
    private Map<Long, Person> players;

    @NotBlank(message = "Team-Manager darf nicht leer sein")
    @Schema(description = "Manager des Teams")
    private Person manager;

    public TeamDTO() {
    }

    

    public TeamDTO(@NotNull(message = "Team-Typ darf nicht null sein") TeamType type,
            @NotBlank(message = "Team-Name darf nicht leer sein") String name,
            @NotNull(message = "Team-Kategorie darf nicht null sein") TeamCategory category, Map<Long, Person> players,
            @NotBlank(message = "Team-Manager darf nicht leer sein") Person manager) {
        this.type = type;
        this.name = name;
        this.category = category;
        this.players = players;
        this.manager = manager;
    }



    public TeamType getType() {
        return type;
    }

    public void setType(TeamType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamCategory getCategory() {
        return category;
    }

    public void setCategory(TeamCategory category) {
        this.category = category;
    }

    public Map<Long, Person> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Long, Person> players) {
        this.players = players;
    }

    public Person getManager() {
        return manager;
    }

    public void setManager(Person manager) {
        this.manager = manager;
    }

    

}
