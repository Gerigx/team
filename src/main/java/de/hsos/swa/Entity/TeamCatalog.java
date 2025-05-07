package de.hsos.swa.Entity;

import java.util.List;

public interface TeamCatalog {
    List<Team> getTeams();
    Team getTeam(Long id);
    Team createTeam(Team team);
    Team updateTeam(Team team);
    boolean deleteTeam(long id);

}
