package leadgames.cis400.leadgames;

import java.util.List;

public class PutResult {

    private String trialId;
    private boolean correct;
    private int time;
    private PutObject putObject;
    private List<PutObject> path;

    public PutResult(String trialId, boolean correct, int time, PutObject putObject,
                     List<PutObject> path) {
        this.trialId = trialId;
        this.correct = correct;
        this.time = time;
        this.putObject = putObject;
        this.path = path;
    }



}
;