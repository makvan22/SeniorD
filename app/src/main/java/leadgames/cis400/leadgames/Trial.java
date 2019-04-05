package leadgames.cis400.leadgames;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

public class Trial {
    private String id;
    private String targetAnimal;
    private String targetGoal;
    private String targetPlatform;
    private String distractorAnimal;
    private String distractorGoal;
    private String distractorPlatform;
    private String soundFile;

    public Trial (DataSnapshot dataSnapshot) {
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
        this.id = dataSnapshot.getKey();
        this.targetAnimal=object.get("targetAnimal").toString();
        this.targetGoal=object.get("targetGoal").toString();
        this.targetPlatform=object.get("targetPlatform").toString();
        this.distractorAnimal=object.get("distractorAnimal").toString();
        this.distractorGoal=object.get("distractorGoal").toString();
        this.distractorPlatform=object.get("distractorPlatform").toString();
        this.soundFile = object.get("soundFile").toString();
    }


    public Trial (String id, String targetAnimal, String targetGoal, String targetPlatform,
                  String distractorAnimal, String distractorGoal, String distractorPlatform, String soundFile) {
        this.id = id;
        this.targetAnimal = targetAnimal;
        this.targetGoal = targetGoal;
        this.targetPlatform = targetPlatform;
        this.distractorAnimal = distractorAnimal;
        this.distractorGoal = distractorGoal;
        this.distractorPlatform = distractorPlatform;
        this.soundFile = soundFile;

    }

    public String getId (){
        return this.id;
    }

    public String getTargetAnimal() {return targetAnimal; }

    public String getTargetGoal() {
        return targetGoal;
    }

    public String getTargetPlatform() {
        return targetPlatform;
    }

    public String getDistractorAnimal() {
        return distractorAnimal;
    }

    public String getDistractorGoal() {
        return distractorGoal;
    }

    public String getDistractorPlatform() {
        return distractorPlatform;
    }

    public String getSoundFile() { return soundFile.toLowerCase().replace(".", ""); }

}
