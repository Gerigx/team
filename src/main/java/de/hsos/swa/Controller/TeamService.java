package de.hsos.swa.Controller;

import java.util.List;

import de.hsos.swa.Entity.Team;

public interface TeamService {
    List<Team> getAllTeams();
    Team getTeamById(long id);
    Team createTeam(Team team);
}
