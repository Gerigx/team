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
import de.hsos.swa.Entity.TeamCatalog;
import de.hsos.swa.Entity.TeamCategory;
import de.hsos.swa.Entity.TeamType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TeamServiceImpl implements TeamService {

    @Inject
    private TeamCatalog teamCatalog;

    
    public TeamServiceImpl() {
    }
    
    
    @Override
    public List<Team> getAllTeams() {
        return teamCatalog.getTeams();
    }
    
    @Override
    public Team getTeamById(long id) {
        return teamCatalog.getTeam(id);
    }
    
    @Override
    public Team createTeam(TeamDTO teamDTO) {
        Team team = new Team(0, teamDTO.getType() // FRAGE: ist das die schöne lösung oder würde es einen besseren ansatz geben indem das DTO weiter nach unten gegeben wird
                            ,teamDTO.getName()
                            ,teamDTO.getCategory()
                            ,teamDTO.getManager());

        return teamCatalog.createTeam(team); // Diese Zeile fehlt!
    }


    @Override
    public Team updateTeam(Team updatedTeam) {
        return teamCatalog.updateTeam(updatedTeam);

    }


    @Override
    public boolean deleteTeam(long id) {
        return teamCatalog.deleteTeam(id);

    }

}
