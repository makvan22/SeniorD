package leadgames.cis400.leadgames;

import java.util.List;

public class PutResult {

    private String trialId;
    private boolean correct;
    private int time;
    private List<PutPath> paths;

    public PutResult(String trialId, boolean correct, int time,
                     List<PutPath> paths) {
        this.trialId = trialId;
        this.correct = correct;
        this.time = time;
        this.paths = paths;
    }

    public String toString() {
        return "Trial ID: " + trialId + "\n" + "Success: " + correct + "\n" +
                "Time: " + time + "\n" + paths.toString();
    }

    public String getTrialId() { return trialId; }
    public boolean getCorrect() { return correct; }
    public int getTime() { return time; }
    public List<PutPath> getPaths() { return paths; }

};