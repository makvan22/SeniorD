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



}
;