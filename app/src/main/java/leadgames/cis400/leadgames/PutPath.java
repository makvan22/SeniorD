package leadgames.cis400.leadgames;

import java.util.List;

public class PutPath {

    private PutObject putObject;
    private List<PutObject> path;

    public PutPath(PutObject putObject, List<PutObject> path) {
        this.putObject = putObject;
        this.path = path;
    }

    public String toString() {
        return "Put Object: " + putObject + " Object path: " + path.toString();
    }

    public String getPutOjbect() { return putObject.toString(); }

    public String getPutObjectPath() {
        if (path.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (PutObject obj: path) {
            sb.append(obj.toString());
            sb.append('-');
        }

        String sbString = sb.toString();
        return sbString.substring(0, sbString.length() - 1);
    }
}
