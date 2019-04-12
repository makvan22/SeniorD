package leadgames.cis400.leadgames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PutGameDB {
    private InputStream is;
    private List<Trial> trials;
    private List<List<Trial>> localTrials;

    public PutGameDB() {
        localTrials = new ArrayList<>();
        trials = new ArrayList<>();
    }

    public List<Trial> getAllPutTrials() {
        if (localTrials.isEmpty()) {
            return trials;
        }

        return new ArrayList<>(localTrials.get(1));
    }

    public void addPutTrial(Trial trial) {
        trials.add(trial);
    }

    public void readTrialFromInputStream(InputStream is) throws IOException {

        List<Trial> listTrials = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 18) continue;

                String id = "";
                int list = Integer.parseInt(values[0]);
                String item = values[1];
                int trialNum = Integer.parseInt(values[2]);
                String soundFile = values[3];
                String sentence = values[4];
                int referents = Integer.parseInt(values[5]);
                String ambiguity = values[6];
                String type = values[7];
                String target = values[8];
                String targetPlatform = values[9];
                String goal = values[10];
                String incorrectGoal = values[11];
                String competitorPlatform = values[12];
                String competitor = values[13];
                String targetLocation = values[14];
                String goalLocation = values[15];
                String incorrectGoalLocation = values[16];
                String competitorLocation = values[17];
                listTrials.add(new Trial(id, list, item, trialNum, soundFile,
                        sentence, referents, ambiguity, type, target, targetPlatform, goal,
                        incorrectGoal, competitorPlatform, competitor, targetLocation, goalLocation,
                        incorrectGoalLocation, competitorLocation));
            }

        }
        localTrials.add(listTrials);
        is.close();
    }

}
