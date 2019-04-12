package leadgames.cis400.leadgames;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class AmbiguityDetectionActivity extends AppCompatActivity {

    private List<AmbiguityDetectionTrial> trials = new ArrayList<>();
    private Iterator<AmbiguityDetectionTrial> trialIterator = null;

    private ImageView q1;
    private ImageView q2;
    private ImageView q3;
    private ImageView q4;

    private Scene s1;
    private Scene s2;
    private Scene s3;
    private Scene s4;

    private ImageView submit;
    private TextView feedback_panel;
    private TextView feedback_text;
    private LikeButtonView feedback_anim;

    int fs_time = 0;
    int ss_time = 0;
    long startTime = 0;
    String first_selection = "";
    String second_selection = "";
    private int num_selected = 0;

    private Participant participant;
    private FirebaseManager db;
    MediaPlayer mediaPlayer = null;

    private AmbiguityDetectionTrial currTrial = null;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambiguity_detection);

        Intent intent = getIntent();
        this.participant = (Participant) intent.getExtras().getSerializable("participant");
        db = FirebaseManager.getInstance();

        initViews();
        loadTrials();
        if (trials.isEmpty()) {
             backToMenu();
        }
        trialIterator = trials.iterator();
        if (trialIterator.hasNext()) {
            startTrial(trialIterator.next());
        }
    }

    private final class Scene {
        private ImageView imageView;
        private boolean selected;
        private String image;

        public Scene (ImageView iv, final String img_src) {
            this.selected = false;
            this.imageView = iv;
            this.image = img_src;

            imageView.setImageResource(ImageFinder.getImageResource(img_src));
            imageView.setBackgroundColor(getResources().getColor(R.color.white));
            imageView.setClickable(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (num_selected >= 2 ) {
                        return;
                    }
                    if (!selected) {
                        selected = true;
                        num_selected += 1;
                        if (num_selected == 1) {
                            long difference = System.currentTimeMillis() - startTime;
                            fs_time = (int) (difference / 1000.0);
                            first_selection = image;
                        } else {
                            second_selection = image;
                            long difference = System.currentTimeMillis() - startTime;
                            ss_time = (int) (difference / 1000.0);
                        }
                    }
                    imageView.setBackgroundColor(getResources().getColor(R.color.green));
                    System.out.println("View clicked!");
                }
            });
        }
    }

    private void initViews() {
        //Initialize animal views
        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Submit clicked!");
                endTrial();
            }
        });

        //Initialize feedback view
        feedback_panel = findViewById(R.id.feedback_panel);
        feedback_text = findViewById(R.id.feedback_text);
        feedback_anim = findViewById(R.id.star);
    }

    private void loadTrials() {
        trials = db.getAllAmbiguityDetectionTrials();
    }

    private void backToMenu() {
        Intent mainIntent = new Intent(
                AmbiguityDetectionActivity.this, LoginActivity.class);
        AmbiguityDetectionActivity.this.startActivity(mainIntent);
        AmbiguityDetectionActivity.this.finish();
    }

    private void startTrial(AmbiguityDetectionTrial trial) {
        // TODO: add sounds
        //Reset scenes
        List<String> trial_imgs = new ArrayList<String>();
        trial_imgs.add(trial.getImg_1());
        trial_imgs.add(trial.getImg_2());
        trial_imgs.add(trial.getImg_3());
        trial_imgs.add(trial.getImg_4());
        Collections.shuffle(trial_imgs);

        this.currTrial = trial;
        s1 = new Scene(q1, trial_imgs.get(0));
        s2 = new Scene(q2, trial_imgs.get(1));
        s3 = new Scene(q3, trial_imgs.get(2));
        s4 = new Scene(q4, trial_imgs.get(3));

        //Reset  trial variables
        fs_time = 0;
        ss_time = 0;
        int correct = 0;
        num_selected = 0;
        first_selection = "";
        second_selection = "";
        startTime = SystemClock.elapsedRealtime();

        //removing the 'wav' at the end
        String sound = trial.getSoundFile();
        sound = sound.substring(0, sound.length() - 4);
        int soundid = getResourceId(sound, "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), soundid);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.start();
                    //}
                }
            }, isFirst ? 500 : 3000);
        isFirst = false;
    }

    private void endTrial() {
        System.out.println("fs: " + first_selection);
        System.out.println("ss: " + second_selection);
        System.out.println("fs_time: " + fs_time);
        System.out.println("ss_time: " + ss_time);
        System.out.println("score: " + score());

        AmbiguityDetectionResult result = new AmbiguityDetectionResult(
                currTrial.getSubject(), currTrial.getItem(), currTrial.getTrial(),
                currTrial.getCondition(), first_selection, second_selection,
                fs_time, ss_time, score(),  participant
        );

        db.addAmbiguityDetectionResult(result, getApplicationContext());

        if (trialIterator.hasNext()) {
            displayFeedback(false);
            startTrial(trialIterator.next());
        } else {
            displayFeedback(true);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    backToMenu();
                }
            }, 1500);
        }
    }

    private int score() {
        /* 2 correct w/o prompt -> 4
         * 2 correct w/ prompt -> 2
         * 1 correct -> 1
         * 0 correct -> 0
         */
        int num_correct = 0;
        if (first_selection.equals(currTrial.getCorrect_1()) ||
                first_selection.equals(currTrial.getCorrect_2())) {
            num_correct += 1;
        }
        if (second_selection.equals(currTrial.getCorrect_1()) ||
                second_selection.equals(currTrial.getCorrect_2())) {
            num_correct += 1;
        }
        if (num_correct < 2) {
            return num_correct;
        } else {
            //TODO: implement prompting for second choice.
            //Assuming no prompt for now
            return 4;
        }
    }

    private void displayFeedback(final boolean game_over) {
        feedback_panel.setVisibility(View.VISIBLE);
        feedback_anim.setVisibility(View.VISIBLE);
        if (game_over) {
            feedback_text.setText(R.string.game_done);
            feedback_text.setVisibility(View.VISIBLE);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                feedback_anim.callOnClick();
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!game_over) {
                    feedback_panel.setVisibility(View.INVISIBLE);
                    feedback_anim.setVisibility(View.INVISIBLE);
                    feedback_text.setVisibility(View.INVISIBLE);
                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() { }
        public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
        {
            try {
                return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
}
