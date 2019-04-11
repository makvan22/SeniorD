package leadgames.cis400.leadgames;

public class AmbiguityDetectionResult {
    //TODO: fetch inputs from researchers in order to model this class
    private String subject;
    private String item;
    private String trial;
    private String condition;
    private String first_selection;
    private String second_selection;
    private int fs_time;
    private int ss_time;
    private int score;
    private Participant participant;

    public AmbiguityDetectionResult(String subject, String item, String  trial, String condition,
                                    String first_selection, String second_selection, int fs_time,
                                    int ss_time, int score, Participant participant) {
        this.subject = subject;
        this.item = item;
        this.trial = trial;
        this.condition = condition;
        this.first_selection = first_selection;
        this.second_selection = second_selection;
        this.fs_time = fs_time;
        this.ss_time = ss_time;
        this.score = score;
        this.participant = participant;
    }
}
