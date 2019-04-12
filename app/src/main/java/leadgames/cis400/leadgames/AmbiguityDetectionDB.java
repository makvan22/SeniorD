package leadgames.cis400.leadgames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AmbiguityDetectionDB {

    private List<AmbiguityDetectionTrial> trials;

    public AmbiguityDetectionDB() {
        trials = new ArrayList<>();
    }

    public List<AmbiguityDetectionTrial> getAllTrials() {
        return new ArrayList<>(trials);
    }

    public void readTrialFromInputStream(InputStream is) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 9) continue;

                String subject = values[0];
                String trial = values[1];
                String condition = values[2];
                String item = values[3];
                String correct1 = values[4];
                String correct2 = values[5];
                String distractor1 = values[6];
                String distractor2 = values[7];
                String sentence = values[8];
                trials.add(new AmbiguityDetectionTrial(trial, condition, subject, item, correct1,
                        correct2, correct1, correct2, distractor1, distractor2, sentence));
            }

        }
        is.close();
    }
}
