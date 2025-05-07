package de.hsos.swa.Boundary.ACL;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.hsos.swa.Entity.Gender;
import de.hsos.swa.Entity.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PersonDTO {

    @Schema(description = "Name einer Person")
    @NotBlank(message = "Name einer Person darf nicht leer sein")
    private String name;

    @Schema(description = "Nachname einer Person")
    @NotBlank(message = "lastname darf nicht leer sein")
    private String lastname;

    @Schema(description = "Eindeutige ID einer Person")
    @NotNull(message = "Gender darf nicht Null sein")
    private Gender gender;

    public PersonDTO() {
    }

    public PersonDTO(String name, String lastname, Gender gender) {
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
            .append(lastname);

        return sb.toString();
    }   


}
