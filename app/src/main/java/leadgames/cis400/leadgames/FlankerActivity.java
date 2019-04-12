package leadgames.cis400.leadgames;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */


public class FlankerActivity extends AppCompatActivity {
    final int rightImgId = R.drawable.left_fish;
    final int leftImgId = R.drawable.right_fish;

    private ArrayList<ImageView> allFish;
    private ImageView fish1;

    private List<FlankerTrial> trials;
    private Iterator<FlankerTrial> trialIterator = null;
    private FlankerTrial currTrial = null;

    private ImageView leftButton;
    private ImageView rightButton;

    private TextView feedback_panel;
    private TextView feedback_text;
    private LikeButtonView feedback_anim;
    private FirebaseManager db;
    long startTime = 0;
    Context context = null;

    private int wins = 0;
    private int plays=0;
    //LEFT = 0, RIGHT = 1;
    private int midFishDir = 0;

    private Participant participant;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flanker_layout);

        //trials = db.getAllFlankerTrials();

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
    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void startTrial(FlankerTrial trial) {
        context = this;
        this.currTrial = trial;
        String image = trial.getImage();
        image = image.substring(0, image.length() - 4);
        Log.d("image", image);
        int imageid = getResourceId(image, "drawable", getPackageName());
        for (ImageView fish : allFish) {
            fish.setImageResource(imageid);
        }

/*
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                // Replace with your logic.
                leftButton.setOnClickListener(null);
                rightButton.setOnClickListener(null);
                endTrial(5, "NA");
            }
        };
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeedback(false);
                endTrial((int)(SystemClock.elapsedRealtime()-startTime / 1000.0), "left");
                leftButton.removeCallbacks(runnable);
            }
        });
        */
       /*
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View view) {
                //Removing 5 Seconds timer which will remove click listener.
                leftButton.removeCallbacks(runnable);
            }
        });*/

        //Removed Click Listener after 5 Seconds.
        /*
        leftButton.postDelayed(runnable, 7500);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeedback(false);
                endTrial((int)(SystemClock.elapsedRealtime()-startTime / 1000.0), "right");
                rightButton.removeCallbacks(runnable);
            }
        });
        rightButton.postDelayed(runnable, 7500);
*/
        if (image.equals("bowl") || image.charAt(0) == 'n') {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 5000ms
                    endTrial(5, "NA");
                    //}
                }
            }, 7500);
        }
        else {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }

        //reset  trial variables

        int correct = 0;
        startTime = SystemClock.elapsedRealtime();
    }

    private void loadTrials() {
        trials = db.getAllFlankerTrials();
    }
    private void backToMenu() {
        Intent mainIntent = new Intent(
                FlankerActivity.this, LoginActivity.class);
        FlankerActivity.this.startActivity(mainIntent);
        FlankerActivity.this.finish();
    }
    @Override
    public void onBackPressed() { }

    private void initViews() {
        //TODO: send FlankerResult back to db.addFlankerResult
        //db.addFlankerResult(new FlankerResult(trial, 1, 100, "left", participant);
        //System.out.println(trials.get(0).getDirection());
        leftButton = (ImageView) findViewById(R.id.left_arrow);
        leftButton.setClickable(true);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeedback(false);
                endTrial((int)(SystemClock.elapsedRealtime()-startTime / 1000.0), "left");
            }
        });

        rightButton = (ImageView) findViewById(R.id.right_arrow);
        rightButton.setClickable(true);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeedback(false);
                endTrial((int)(SystemClock.elapsedRealtime()-startTime / 1000.0), "right");
            }
        });

        fish1 = (ImageView) findViewById(R.id.fish1);


        allFish = new ArrayList<ImageView>();
        allFish.add(fish1);


        //Initialize feedback view
        feedback_panel = (TextView) findViewById(R.id.feedback_panel);
        feedback_text = (TextView) findViewById(R.id.feedback_text);
        feedback_anim = findViewById(R.id.star);
        feedback_anim.setClickable(false);
    }
    private void endTrial(int time, String response) {
        //TODO: Determine correctness
        //TODO: Upload result to database
        //TODO: send FlankerResult back to db.addFlankerResult
        //db.addFlankerResult(new FlankerResult(trial, 1, 100, "left", participant);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        FlankerResult result = new FlankerResult(
                currTrial, response.equals(currTrial.direction), time,
                response,  participant
        );

        db.addFlankerResult(result, context);

        if (trialIterator.hasNext()) {
            displayFeedback(false);
            startTrial(trialIterator.next());
        } else {
            displayFeedback(true);
            backToMenu();
        }
    }
    private void restartGame() {
        wins = 0;
    }

    private int setMidDirection() {
        if (Math.random() < 0.5) {
            midFishDir = 0;
            return leftImgId;
        }
        midFishDir = 1;
        return rightImgId;
    }
    private int getDirection() {
        if (Math.random() < 0.5) {
            return leftImgId;
        }
        return rightImgId;
    }

    private void displayFeedback(final boolean game_over) {
        if (game_over) {
            feedback_text.setText(R.string.game_done);
            feedback_text.setVisibility(View.VISIBLE);
        }
        feedback_panel.setVisibility(View.VISIBLE);
        feedback_anim.setVisibility(View.VISIBLE);
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
}
