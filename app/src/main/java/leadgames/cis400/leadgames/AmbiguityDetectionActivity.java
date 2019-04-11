package leadgames.cis400.leadgames;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AmbiguityDetectionActivity extends AppCompatActivity {

    private List<AmbiguityDetectionTrial> trials = new ArrayList<AmbiguityDetectionTrial>();
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

    private int num_selected = 0;
    private Participant participant;

    private FirebaseManager db;

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
        //TODO: record trials / update database
    }

    private final class Scene {
        private ImageView imageView;
        private boolean selected;

        public Scene (ImageView iv, String img_src) {
            this.selected = false;
            this.imageView = iv;

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
                    }
                    imageView.setBackgroundColor(getResources().getColor(R.color.green));
                    System.out.println("View clicked!");
                }
            });
        }
    };

    private void initViews() {
        //Initialize animal views
        q1 = (ImageView) findViewById(R.id.q1);
        q2 = (ImageView) findViewById(R.id.q2);
        q3 = (ImageView) findViewById(R.id.q3);
        q4 = (ImageView) findViewById(R.id.q4);

        submit =  (ImageView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Submit clicked!");
                endTrial();
            }
        });

        //Initialize feedback view
        feedback_panel = (TextView) findViewById(R.id.feedback_panel);
        feedback_text = (TextView) findViewById(R.id.feedback_text);
        feedback_anim = findViewById(R.id.star);
    }



    private void loadTrials() {
        trials = db.getAllAmbiguityDetectionTrials();
        System.out.println(trials.get(0));
        System.out.println(trials.get(1));
    }

    private void backToMenu() {
        Intent mainIntent = new Intent(
                AmbiguityDetectionActivity.this, LoginActivity.class);
        AmbiguityDetectionActivity.this.startActivity(mainIntent);
        AmbiguityDetectionActivity.this.finish();
    }

    private void startTrial(AmbiguityDetectionTrial trial) {
        s1 = new Scene(q1, trial.getImg_1());
        s2 = new Scene(q2, trial.getImg_2());
        s3 = new Scene(q3, trial.getImg_3());
        s4 = new Scene(q4, trial.getImg_4());
    }

    private void endTrial() {
        //TODO: add timing and correctness then store results
        if (trialIterator.hasNext()) {
            displayFeedback(false);
            startTrial(trialIterator.next());
        } else {
            displayFeedback(true);
            backToMenu();
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
        }, 1750);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!game_over) {
                    feedback_panel.setVisibility(View.INVISIBLE);
                    feedback_anim.setVisibility(View.INVISIBLE);
                    feedback_text.setVisibility(View.INVISIBLE);

                } else {
                    backToMenu();
                }
            }
        }, 4000);
    }

    @Override
    public void onBackPressed() { }
}
