package leadgames.cis400.leadgames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FlankerDB {

    private InputStream is;
    private List<FlankerTrial> trials;

    public FlankerDB() {
        trials = new ArrayList<>();
    }

    public List<FlankerTrial> getAllTrials() {
        return new ArrayList<>(trials);
    }

    /** reads trials from input stream */
    public void readTrialFromInputStream(InputStream is) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 7) continue;

                String id = "";
                String block = values[0];
                String image = values[1];
                String condition = values[2];
                String direction = values[3];
                String expectedResponse = values[4];
                String switched = values[5];
                String switchType = values[6];
                trials.add(new FlankerTrial(id, image, direction, block, condition, expectedResponse,
                        switched, switchType));
            }

        }
        is.close();
    }

}
