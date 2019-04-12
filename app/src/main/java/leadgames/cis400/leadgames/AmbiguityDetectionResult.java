package leadgames.cis400.leadgames;

import android.provider.Telephony;

import java.util.Map;

public class AmbiguityDetectionResult implements ResultTrial {
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

    @Override
    public Map<String, String> addToParams(Map<String, String> params) {
        params.put("action", "addAmbiguity");
        params.put("studentID", participant.getStudentId());
        params.put("first_name", participant.getFirstName());
        params.put("last_name", participant.getLastName());
        params.put("dob", participant.getDob());
        params.put("sex", participant.getGender());
        params.put("condition", condition);
        params.put("item", item);
        params.put("First_Picture_Selected", first_selection);
        params.put("Second_Picture_Selected", second_selection);
        params.put("Time_for_picture_selection_1", Integer.toString(fs_time));
        params.put("Time_for_picture_selection_2", Integer.toString(ss_time));
        params.put("score", Integer.toString(score));
        return params;
    }
}
