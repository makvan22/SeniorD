package leadgames.cis400.leadgames;

public class Trial {
    private Integer id;
    private String targetAnimal;
    private String targetGoal;
    private String targetPlatform;
    private String distractorAnimal;
    private String distractorGoal;
    private String distractorPlatform;

    public Trial (Integer id, String targetAnimal, String targetPlatform, String distractorAnimal,
                  String distractorPlatform, String targetGoal, String distractorGoal) {
        this.id = id;
        this.targetAnimal = targetAnimal;
        this.targetGoal = targetGoal;
        this.targetPlatform = targetPlatform;
        this.distractorAnimal = distractorAnimal;
        this.distractorGoal = targetGoal;
        this.distractorPlatform = targetPlatform;
    }

    public Integer getId (){
        return this.id;
    }

    public String getTargetAnimal() {
        return targetAnimal;
    }

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
}
