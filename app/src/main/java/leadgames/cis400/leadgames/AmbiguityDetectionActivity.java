package leadgames.cis400.leadgames;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;


public class AmbiguityDetectionActivity extends AppCompatActivity {

    private ArrayList<AmbiguityDetectionTrial> trials = new ArrayList<AmbiguityDetectionTrial>();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambiguity_detection);
        //TODO: init views
        initViews();
        loadTrials();
        if (trials.isEmpty()) {
            //TODO: decide on null behaviour & implement
            return;
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

        public Scene (ImageView imageView, String img_src) {
            this.selected = false;
            this.imageView = imageView;
            imageView.setImageResource(ImageFinder.getImageResource(img_src));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                selected = !selected;
                }
            });
        }
    };

    public static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    public void initViews() {
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
        //TODO : get more trials from researchers
        AmbiguityDetectionTrial t1 = new  AmbiguityDetectionTrial("1","Filler",
                "S1",  "dog", "dog_scene_1", "dog_scene_2",
                "bird_scene", "dog_scene_1", "dog_scene_2",
                "mouse_scene");

        AmbiguityDetectionTrial t2 = new  AmbiguityDetectionTrial("2","Lexical_Amb",
                "S1",  "bat", "bat_scene_1", "bat_scene_2",
                "bat_scene_1", "house_scene", "cat_fence_scene",
                "bat_scene_2");


        AmbiguityDetectionTrial t3 = new  AmbiguityDetectionTrial("3","Structural",
                "S1",  "with_brush", "cat_brush_scene",
                "girl_brush_scene","cat_brush_scene", "apple_scene",
                "feather_scene", "girl_brush_scene");

       trials.add(t1);
       trials.add(t2);
       trials.add(t3);
    }

    public void startTrial(AmbiguityDetectionTrial trial) {
        s1 = new Scene(q1, trial.getImg_1());
        s2 = new Scene(q2, trial.getImg_2());
        s3 = new Scene(q3, trial.getImg_3());
        s4 = new Scene(q4, trial.getImg_4());
    }

    public void endTrial() {
        //TODO: add timing and correctness then store results
        if (trialIterator.hasNext()) {
            displayFeedback(false);
            startTrial(trialIterator.next());
        } else {
            //TODO: add feedback
            displayFeedback(true);
            Intent mainIntent = new Intent(
                    AmbiguityDetectionActivity.this,LoginActivity.class);
            AmbiguityDetectionActivity.this.startActivity(mainIntent);
            AmbiguityDetectionActivity.this.finish();
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
                    Intent mainIntent = new Intent(
                            AmbiguityDetectionActivity.this, LoginActivity.class);
                    AmbiguityDetectionActivity.this.startActivity(mainIntent);
                    AmbiguityDetectionActivity.this.finish();
                }
            }
        }, 4000);
    }
}
