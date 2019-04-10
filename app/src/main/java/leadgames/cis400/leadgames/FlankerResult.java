package leadgames.cis400.leadgames;

public class FlankerResult {


    private FlankerTrial trial;
    private boolean correct;
    private int time;
    private String response;
    private Participant participant;

    /**
     *
     * @param trial the OG trial object received from DB
     * @param correct true or false if response was correct
     * @param time time in MS(??)
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
}
