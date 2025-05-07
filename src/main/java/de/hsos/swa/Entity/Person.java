package de.hsos.swa.Entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Repräsentiert eine Person")
public class Person {

    @Schema(description = "Eindeutige ID einer Person")
    @NotBlank(message = "ID einer Person darf nicht Leer sein")
    private long id;

    @Schema(description = "Name einer Person")
    @NotBlank(message = "Name einer Person darf nicht leer sein")
    private String name;

    @Schema(description = "Nachname einer Person")
    @NotBlank(message = "lastname darf nicht leer sein")
    private String lastname;

    @Schema(description = "Eindeutige ID einer Person")
    @NotNull(message = "Gender darf nicht Null sein")
    private Gender gender;

    public Person() {
    }

    public Person(long id, String name, String lastname, Gender gender) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        Person other = (Person) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.gender.equals(Gender.MALE)) {
            sb.append("Herr ");
        }
        else if (this.gender.equals(Gender.FEMALE)) {
            sb.append("Frau ");
        } // bei neutral keine anrede

        sb.append(name).append(" ")
            .append(lastname)
            .append(" hat die ID ").append(id);

        return sb.toString();
    }   

}
