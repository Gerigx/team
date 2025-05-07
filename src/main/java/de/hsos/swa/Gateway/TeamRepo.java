package de.hsos.swa.Gateway;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.Team;
import de.hsos.swa.Entity.TeamCatalog;
import de.hsos.swa.Entity.TeamCategory;
import de.hsos.swa.Entity.TeamType;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TeamRepo implements TeamCatalog {
    private final ConcurrentMap<Long, Team> teams = new ConcurrentHashMap<>();
    private static final AtomicLong TEAM_ID_COUNTER = new AtomicLong(1);



    public TeamRepo() {
        initDemoData();
    }
    
    private void initDemoData() {
        // Erstelle einige Test-Personen direkt (ohne Dependency auf PersonRepo)
        Person jan = createDemoPerson(1, "Jan", "Steinkamp", Gender.MALE);
        Person kevin = createDemoPerson(2, "Kevin", "Siofer", Gender.MALE);
        Person luffy = createDemoPerson(3, "Monkey D.", "Luffy", Gender.MALE);
        Person zoro = createDemoPerson(4, "Roronoa", "Zoro", Gender.MALE);
        Person nami = createDemoPerson(5, "Nami", "", Gender.FEMALE);
        Person robin = createDemoPerson(6, "Nico", "Robin", Gender.FEMALE);
        Person stark = createDemoPerson(7, "Tony", "Stark", Gender.MALE);
        
        // Team HS OS mit Jan und Kevin
        Team hsosTeam = new Team();
        hsosTeam.setType(TeamType.TEAM);
        hsosTeam.setName("HS OS");
        hsosTeam.setCategory(TeamCategory.ADULTS);
        hsosTeam.setManager(jan);
        hsosTeam.setPlayers(new HashMap<>());
        hsosTeam.addPlayerToTeam(jan);
        hsosTeam.addPlayerToTeam(kevin);
        createTeam(hsosTeam);
        
        // One Piece Team mit Luffy als Manager
        Team strawHatTeam = new Team();
        strawHatTeam.setType(TeamType.TEAM);
        strawHatTeam.setName("Straw Hat Pirates");
        strawHatTeam.setCategory(TeamCategory.ADULTS);
        strawHatTeam.setManager(luffy);
        strawHatTeam.setPlayers(new HashMap<>());
        strawHatTeam.addPlayerToTeam(luffy);
        strawHatTeam.addPlayerToTeam(zoro);
        strawHatTeam.addPlayerToTeam(nami);
        strawHatTeam.addPlayerToTeam(robin);
        createTeam(strawHatTeam);
        
        // Avengers Team mit Tony Stark als Manager
        Team avengersTeam = new Team();
        avengersTeam.setType(TeamType.TEAM);
        avengersTeam.setName("Avengers");
        avengersTeam.setCategory(TeamCategory.ADULTS);
        avengersTeam.setManager(stark);
        avengersTeam.setPlayers(new HashMap<>());
        avengersTeam.addPlayerToTeam(stark);
        createTeam(avengersTeam);
    }

        // Hilfsmethode zum Erstellen von Demo-Personen
        private Person createDemoPerson(long id, String name, String lastname, Gender gender) {
            Person person = new Person();
            person.setId(id);
            person.setName(name);
            person.setLastname(lastname);
            person.setGender(gender);
            return person;
        }

    @Override
    public List<Team> getTeams() {
        return this.teams.values().stream().toList();
    }

    @Override
    public Team getTeam(Long id) {
        return this.teams.get(id);
    }

    @Override
    public Team createTeam(Team team) {
        team.setId(TEAM_ID_COUNTER.getAndIncrement());
        
        // tiefe cop
        Team teamCopy = new Team(
            team.getId(), 
            team.getType(), 
            team.getName(), 
            team.getCategory(), 
            team.getManager()
        );
        
        // Spieler kopieren, falls vorhanden
        if (team.getPlayers() != null && !team.getPlayers().isEmpty()) {
            teamCopy.setPlayers(new HashMap<>(team.getPlayers()));
        }
        
        // Im Repository speichern
        this.teams.put(team.getId(), team);
        
        return teamCopy;
    }

    @Override
    public Team updateTeam(Team team) {
        if(teams.get(team.getId()) != null){
            teams.put(team.getId(), team);
            return team;
        }
        return null;
    }

    @Override
    public boolean deleteTeam(long id) {
        return this.teams.remove(id) != null;
    }
}
