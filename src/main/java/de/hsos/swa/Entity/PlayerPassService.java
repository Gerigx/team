package de.hsos.swa.Entity;

import java.util.List;

public interface PlayerPassService  {

    boolean validatePlayerId(long playerId);
    
    boolean registerNewPlayer(Person player);
    
    List<Person> getPersons();
    
    Person getPerson(long id);
    
    Person createPerson(Person person);
    
    Person updatePerson(Person person);
    
    boolean deletePerson(long id);
    
    boolean isPlayerEligibleForTeam(long playerId, long teamId);

}
