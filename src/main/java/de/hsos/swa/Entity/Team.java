package de.hsos.swa.Entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Schema(description = "Repräsentiert ein Team mit Spielern")
public class Team {

    @Schema(description = "Eindeutige ID des Teams")
    private long id;
    
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
    


    public Team() {
    }

    public Team(long id, TeamType type, String name, TeamCategory category, Person manager) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.category = category;
        this.players = new HashMap<>();
        this.manager = manager;
    }


    public void addPlayerToTeam(Person player) {
        players.put(player.getId(), player);
    }

    public void removePlayerFromTeam(int playerId) {
        players.remove(playerId);
    }
    

    public Person getPlayerById(int playerId) {
        return players.get(playerId);
    }
    
    public Map<Long, Person> getPlayers() {
        return players;
    }

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
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

    public void setPlayers(Map<Long, Person> players) {
        this.players = players;
    }

    public Person getManager() {
        return manager;
    }

    public void setManager(Person manager) {
        this.manager = manager;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Team other = (Team) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team [id=").append(id)
          .append(", type=").append(type)
          .append(", name='").append(name).append("'")
          .append(", category=").append(category)
          .append(", players={\n");
        
        if (players != null && !players.isEmpty()) {
            for (Map.Entry<Long, Person> entry : players.entrySet()) {
                sb.append("    Player ID: ").append(entry.getKey())
                  .append(" -> ").append(entry.getValue()).append("\n");
            }
        } else {
            sb.append("    Keine Spieler vorhanden\n");
        }
        
        sb.append("}]");
        return sb.toString();
    }
}