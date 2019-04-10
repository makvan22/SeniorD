package leadgames.cis400.leadgames;

import java.io.Serializable;

public class Participant implements Serializable {


    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String id;

    public Participant(String first, String last, String gender, String dob, String id) {
        firstName = first;
        lastName = last;
        this.gender = gender;
        this.dob = dob;
        this.id = id;
    }

}