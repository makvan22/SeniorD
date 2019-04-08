package leadgames.cis400.leadgames;

import java.util.ArrayList;

public class AmbiguityDetectionTrial {
    //TODO: fetch inputs from researchers in order to model this class
    private String trial;
    private String condition;
    private String subject;
    private String item;
    private String correct_1;
    private String correct_2;
    private String img_1;
    private String img_2;
    private String img_3;
    private String img_4;

    public AmbiguityDetectionTrial(String trial, String condition, String subject, String item,
                                   String correct_1, String correct_2, String img_1, String img_2,
                                   String img_3, String img_4) {
        this.trial = trial;
        this.condition = condition;
        this.subject = subject;
        this.item = item;
        this.correct_1 = correct_1;
        this.correct_2 = correct_2;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.img_4 = img_4;

    }

    public String getTrial() {
        return trial;
    }
    public String getCondition() {
        return condition;
    }
    public String getSubject() {
        return subject;
    }
    public String getItem() {
        return item;
    }
    public String getCorrect_1() { return correct_1; }
    public String getCorrect_2() { return correct_2; }

    public String getImg_1() {
        return img_1;
    }
    public String getImg_2() {
        return img_2;
    }
    public String getImg_3() {
        return img_3;
    }
    public String getImg_4() {
        return img_4;
    }
}
