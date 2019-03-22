package leadgames.cis400.leadgames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PutGameDB {
    private List<Trial> trials;

    public PutGameDB() {
        trials = new ArrayList<>();
    }

    public List<Trial> getAllPutTrials() {
        return trials;
    }

    public void addPutTrial(Trial trial) {
        trials.add(trial);
    }
}
