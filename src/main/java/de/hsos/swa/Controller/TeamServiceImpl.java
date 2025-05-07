package de.hsos.swa.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import de.hsos.swa.Boundary.ACL.TeamDTO;
import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.Team;
import de.hsos.swa.Entity.TeamCategory;
import de.hsos.swa.Entity.TeamType;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeamServiceImpl implements TeamService {

    private Map<Long, Team> teamDatabase = new HashMap<>();
    private AtomicLong idCounter = new AtomicLong(1);
    
    public TeamServiceImpl() {
        // Initialisierung mit Demo-Daten
        initializeDemoTeams();
    }
    
    private void initializeDemoTeams() {
        // Wir erstellen ein paar Manager-Personen für die Teams
        Person[] managers = new Person[] {
            new Person(1,"Jan","Steinkamp",Gender.MALE),
            new Person(1,"Kevin","Siofer",Gender.FEMALE),
            new Person(1,"Javin","Siokamp",Gender.NONBINARY)

        };
        
        // Werte für TeamType und TeamCategory
        // Annahme: Dies sind die möglichen Werte für die Enums
        TeamType[] types = TeamType.values();  // z.B. FOOTBALL, BASKETBALL, HANDBALL, etc.
        TeamCategory[] categories = TeamCategory.values();  // z.B. JUNIOR, SENIOR, PROFESSIONAL, etc.
        
        // 23 Demo-Teams erstellen
        for (int i = 1; i <= 23; i++) {
            long id = idCounter.getAndIncrement();
            
            // Teams mit verschiedenen Typen und Kategorien erstellen
            TeamType type = types[i % types.length];
            TeamCategory category = categories[i % categories.length];
            
            // Manager auswählen
            Person manager = managers[i % managers.length];
            
            // Team erstellen
            Team team = new Team(id, type, "Team " + id, category, manager);
            
            // Einige Spieler hinzufügen
            for (int j = 1; j <= 5 + (i % 10); j++) {
                long playerId = 100 + (id * 10) + j;
                Person player = new Person(playerId, "Spieler", "Spieler", Gender.MALE);
                team.addPlayerToTeam(player);
            }
            
            // Team zur Datenbank hinzufügen
            teamDatabase.put(id, team);
        }
    }
    
    @Override
    public List<Team> getAllTeams() {
        return new ArrayList<>(teamDatabase.values());
    }
    
    @Override
    public Team getTeamById(long id) {
        return teamDatabase.get(id);
    }
    
    @Override
    public Team createTeam(TeamDTO teamDTO) {
        long id = idCounter.getAndIncrement();

        Team team = new Team(id, teamDTO.getType()
                            ,teamDTO.getName()
                            ,teamDTO.getCategory()
                            ,teamDTO.getManager());

        //teamDatabase.put(id, team);
        return team;
    }

}
