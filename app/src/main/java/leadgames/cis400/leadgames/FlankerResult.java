package leadgames.cis400.leadgames;

import java.util.Map;

public class FlankerResult implements ResultTrial{

    private FlankerTrial trial;
    private boolean correct;
    private int time;
    private String response;
    private Participant participant;

    /**
     * Constructor to create the result that will be written to the DB
     *
     * @param trial the OG trial object received from DB
     * @param correct true or false if response was correct
     * @param time time in seconds
     * @param response what the child selected. "left" or "right" or "NA" if avoided for 3 seconds
     * @param participant the participant object received from the intent (see pick up activity for example)
     */
    public FlankerResult(FlankerTrial trial, boolean correct, int time, String response,
                         Participant participant) {
        this.trial = trial;
        this.correct = correct;
        this.time = time;
        this.response = response;
        this.participant = participant;
    }

    public FlankerTrial getTrial() { return trial; }
    public boolean getCorrect() { return correct; }
    public int getTime() { return time; }
    public String getResponse() { return response; }
    public Participant getParticipant() { return participant; }

    /**
     * Creates a map of all parameters of the trial - from parameter title to value
     * @param params
     * @return
     */
    @Override
    public Map<String, String> addToParams(Map<String, String> params) {
        params.put("action", "addFlanker");
        params.put("studentID", participant.getStudentId());
        params.put("firstName", participant.getFirstName());
        params.put("lastName", participant.getLastName());
        params.put("dob", participant.getDob());
        params.put("sex", participant.getGender());
        params.put("block", trial.getBlock());
        params.put("image", trial.getImage());
        params.put("condition", trial.getCondition());
        params.put("direction", trial.getDirection());
        params.put("expectedResponse", trial.getExpectedResponse());
        params.put("response", response);
        params.put("correct", correct ? "1" : "0");
        params.put("switched", trial.getSwitched());
        params.put("switchType", trial.getSwitchType());
        params.put("responseTime", Integer.toString(time));
        return params;
    }
}
