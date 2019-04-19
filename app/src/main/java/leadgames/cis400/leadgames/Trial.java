package leadgames.cis400.leadgames;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

public class Trial {
    private String id;
    private int list;
    private String item;
    private int trial;
    private String sentence;
    private int referents;
    private String ambiguity;
    private String typeOfTrial;
    private PUT_LOCATION targetLocation, targetGoalLocation, distractorPlatformLocation, competitorLocation;

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

    public Trial (String id, int list, String item, int trial, String soundFile, String sentence,
                  int referents, String ambiguity, String typeOfTrial, String target,
                  String targetPlatform,String goal, String incorrectGoal,
                  String competitorPlatform, String competitor,
                  String targetLocation, String goalLocation,
                  String incorrectGoalLocation, String competitorLocation) {
        this.id = id;
        this.list = list;
        this.item = item;
        this.trial = trial;
        this.sentence = sentence;
        this.referents = referents;
        this.ambiguity = ambiguity;
        this.typeOfTrial = typeOfTrial;

        this.targetAnimal = target;
        this.targetGoal = goal;
        this.targetPlatform = targetPlatform;
        this.distractorAnimal = competitor;
        this.distractorGoal = incorrectGoal;
        this.distractorPlatform = competitorPlatform;
        this.soundFile = soundFile;

        this.targetLocation = locationToEnum(targetLocation);
        this.targetGoalLocation = locationToEnum(goalLocation);
        this.distractorPlatformLocation = locationToEnum(incorrectGoalLocation);
        this.competitorLocation = locationToEnum(competitorLocation);
    }


    public Trial (String id, String targetAnimal, String targetGoal, String targetPlatform,
                  String distractorAnimal, String distractorGoal, String distractorPlatform,
                  String soundFile) {
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

    public int getList() { return this.list; }

    public String getItem() { return this.item; }

    public int getTrial() { return this.trial; }

    public String getSentence() { return this.sentence; }

    public int getReferents() { return this.referents; }

    public String getAmbiguity() { return this.ambiguity; }

    public String getTypeOfTrial() { return this.typeOfTrial; }

    public PUT_LOCATION getTargetLocation() { return this.targetLocation; }

    public PUT_LOCATION getTargetGoalLocation() { return this.targetGoalLocation; }

    public PUT_LOCATION getDistractorPlatformLocation() { return this.distractorPlatformLocation; }

    public PUT_LOCATION getCompetitorLocation() { return this.competitorLocation; }

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

    public String getSoundFile() {
        return soundFile.toLowerCase().replace(".", "");
    }

    public PUT_LOCATION locationToEnum(String location) {
        PUT_LOCATION ret = PUT_LOCATION.UPPER_LEFT;
        switch (location) {
            case "Upper Left":
                ret = PUT_LOCATION.UPPER_LEFT;
                break;
            case "Upper Right":
                ret = PUT_LOCATION.UPPER_RIGHT;
                break;
            case "Lower Left":
                ret = PUT_LOCATION.LOWER_LEFT;
                break;
            case "Lower Right":
                ret = PUT_LOCATION.LOWER_RIGHT;
                break;
        }

        return ret;
    }

}
