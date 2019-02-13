package leadgames.cis400.leadgames;

import java.util.List;

public class PutPath {

    private PutObject putObject;
    private List<PutObject> path;

    public PutPath(PutObject putObject, List<PutObject> path) {
        this.putObject = putObject;
        this.path = path;
    }
}
