package leadgames.cis400.leadgames;

import java.util.List;
import java.util.Map;

public class PutResult implements ResultTrial {

    private String trialId;
    private boolean correct;
    private int time;
    private List<PutPath> paths;
    private Participant participant;
    private Trial trial;

    public PutResult(String trialId, boolean correct, int time,
                     List<PutPath> paths, Participant participant, Trial trial) {
        this.trialId = trialId;
        this.correct = correct;
        this.time = time;
        this.paths = paths;
        this.participant = participant;
        this.trial = trial;
    }

    public String toString() {
        return "Trial ID: " + trialId + "\n" + "Success: " + correct + "\n" +
                "Time: " + time + "\n" + paths.toString();
    }

    public String getTrialId() { return trialId; }
    public boolean getCorrect() { return correct; }
    public int getTime() { return time; }
    public List<PutPath> getPaths() { return paths; }
    public Participant getParticipant() { return participant; }

    @Override
    public Map<String, String> addToParams(Map<String, String> params) {
        params.put("action", "addPut");
        params.put("studentID", participant.getStudentId());
        params.put("firstName", participant.getFirstName());
        params.put("lastName", participant.getLastName());
        params.put("dob", participant.getDob());
        params.put("sex", participant.getGender());
        params.put("list", Integer.toString(1));
        params.put("item", "item");
        params.put("sound_file", trial.getSoundFile());
        params.put("sentence", trial.getSentence());
        params.put("referents", Integer.toString(trial.getReferents()));
        params.put("ambiguity", trial.getAmbiguity());
        params.put("type_of_trial", trial.getTypeOfTrial());
        params.put("target", trial.getTargetAnimal());
        params.put("target_platform", trial.getTargetPlatform());
        params.put("goal", trial.getTargetGoal());
        params.put("incorrect_goal", trial.getTargetPlatform());
        params.put("comp_platform", trial.getDistractorPlatform());
        params.put("competitor", trial.getDistractorAnimal());
        params.put("Location_Target", trial.getTargetLocation().toString());
        params.put("Location_Goal", trial.getTargetGoalLocation().toString());
        params.put("Location_Incorrect_Goal", trial.getDistractorPlatformLocation().toString());
        params.put("Location_Competitor", trial.getCompetitorLocation().toString());
        if (paths.size() > 0) {
            PutPath selected = paths.get(paths.size() - 1);
            params.put("selected_object", selected.getPutObject());
            params.put("selected_path", selected.getPutObjectPath());
            params.put("destination", selected.getPutObjectPath());
        } else {
            params.put("selected_object", "");
            params.put("selected_path", "");
            params.put("destination", "");
        }

        params.put("correct", correct ? "1" : "0");
        params.put("responseTime", Integer.toString(time));
        return params;
    }
}
