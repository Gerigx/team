package de.hsos.swa.Gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import de.hsos.swa.Entity.PlayerPassService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonRepo implements PlayerPassService {
    private final ConcurrentMap<Long, Person> persons;
    private static final AtomicLong PERSON_ID_COUNTER = new AtomicLong(1);
    
    public PersonRepo() {
        persons = new ConcurrentHashMap<>();
        initDemoData();
    }
    
    // auswahl der demo Personen von AI
    private void initDemoData() {
        // Reale Personen
        createAndRegisterPerson("Jan", "Steinkamp", Gender.MALE);
        createAndRegisterPerson("Kevin", "Siofer", Gender.MALE);
        
        // One Piece Charaktere
        createAndRegisterPerson("Monkey D.", "Luffy", Gender.MALE);
        createAndRegisterPerson("Roronoa", "Zoro", Gender.MALE);
        createAndRegisterPerson("Nami", "", Gender.FEMALE);
        createAndRegisterPerson("Nico", "Robin", Gender.FEMALE);
        createAndRegisterPerson("Tony Tony", "Chopper", Gender.MALE);
        createAndRegisterPerson("Vinsmoke", "Sanji", Gender.MALE);
        
        // Andere Popkultur - Marvel
        createAndRegisterPerson("Tony", "Stark", Gender.MALE);
        createAndRegisterPerson("Natasha", "Romanoff", Gender.FEMALE);
        
        // Star Wars
        createAndRegisterPerson("Luke", "Skywalker", Gender.MALE);
        createAndRegisterPerson("Leia", "Organa", Gender.FEMALE);
        
        // Game of Thrones
        createAndRegisterPerson("Jon", "Snow", Gender.MALE);
        createAndRegisterPerson("Daenerys", "Targaryen", Gender.FEMALE);
        
        // Harry Potter
        createAndRegisterPerson("Harry", "Potter", Gender.MALE);
        createAndRegisterPerson("Hermione", "Granger", Gender.FEMALE);
        
        // Breaking Bad
        createAndRegisterPerson("Walter", "White", Gender.MALE);
        createAndRegisterPerson("Jesse", "Pinkman", Gender.MALE);
        
        // The Witcher
        createAndRegisterPerson("Geralt", "of Rivia", Gender.MALE);
        createAndRegisterPerson("Yennefer", "of Vengerberg", Gender.FEMALE);
        
        // Diverses
        createAndRegisterPerson("Alex", "Johnson", Gender.NONBINARY);
        createAndRegisterPerson("Sam", "Rodriguez", Gender.NONBINARY);
    }

    // nur für iint
    private void createAndRegisterPerson(String name, String lastname, Gender gender) {
        Person person = new Person();
        person.setName(name);
        person.setLastname(lastname);
        person.setGender(gender);
        registerNewPlayer(person);
    }

    @Override
    public boolean validatePlayerId(long playerId) {
        return persons.containsKey(playerId);
    }

    @Override
    public boolean registerNewPlayer(Person player) {
        player.setId(PERSON_ID_COUNTER.incrementAndGet());
        
        persons.put(player.getId(), player);
        
        return true;
    }

    @Override
    public List<Person> getPersons() {
        return new ArrayList<>(persons.values());
    }

    @Override
    public Person getPerson(long id) {
        return persons.get(id);
    }

    @Override
    public Person createPerson(Person person) {
        if (registerNewPlayer(person)) {
            return person;
        }
        return null;
    }

    @Override
    public Person updatePerson(Person person) {
        if (validatePlayerId(person.getId())) {
            persons.put(person.getId(), person);
            return person;
        }
        return null;
    }

    @Override
    public boolean deletePerson(long id) {
        return persons.remove(id) != null;
    }

    // AI hat in der Code Review vorgeschlagen das sowas auch noch cool sein könnte. Wir fanden das auch so, kamen aber nicht von alleine drauf
    @Override
    public boolean isPlayerEligibleForTeam(long playerId, long teamId) {

        // regeln, falls alter relevant ist oder so
        return validatePlayerId(playerId);
        
    }
}
