package leadgames.cis400.leadgames;

import java.util.List;

public class PutResult {

    private String trialId;
    private boolean correct;
    private int time;
    private List<PutPath> paths;
    private Participant participant;

    public PutResult(String trialId, boolean correct, int time,
                     List<PutPath> paths, Participant participant) {
        this.trialId = trialId;
        this.correct = correct;
        this.time = time;
        this.paths = paths;
        this.participant = participant;
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

}