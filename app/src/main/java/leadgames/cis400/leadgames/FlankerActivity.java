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

 Main game logic for Flanker Game
 This is the controller class, it pulls in views from the XML associated in onCreate,
 populates it using trial information pulled from the FlankerTrial classes which use the input cvs

 */


public class FlankerActivity extends AppCompatActivity {
    //Initializes variables
    //Initializes fish image by name from the res/drawable folder,
    // eg R.drawable.left_fish pulls left_fish/png
    final int rightImgId = R.drawable.left_fish;
    final int leftImgId = R.drawable.right_fish;
    //Initializes fish as an Image
    private ArrayList<ImageView> allFish;
    private ImageView fish1;
    //Initializes trials as an Iterator so we can move from one trial to the next easily
    private List<FlankerTrial> trials;
    private Iterator<FlankerTrial> trialIterator = null;
    private FlankerTrial currTrial = null;
    //Declares ImageViews of the buttons
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
        //Sets flanker_layout.xml as the view
        setContentView(R.layout.flanker_layout);

        Intent intent = getIntent();
        this.participant = (Participant) intent.getExtras().getSerializable("participant");
        db = FirebaseManager.getInstance();

        initViews();
        loadTrials();
        //If no information from trials pulled from the cvs, return to menu to prevent a crash
        if (trials.isEmpty()) {
            backToMenu();
        }
        trialIterator = trials.iterator();
        if (trialIterator.hasNext()) {
            //Call startTrial once on each non-empty trial in the trial iterator
            startTrial(trialIterator.next());
        }
    }

    //This function is to convert string names eg with ballon under pVariableName under a certain
    //folder under the folder pResourceName under res. with eg drawable as pResourcename we would
    //be looking for R.drawable.ballon under res/drawable/ballon/png
    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    //This function is one individual trial
    private void startTrial(FlankerTrial trial) {
        context = this;
        this.currTrial = trial;
        String image = trial.getImage();
        //Trim . and png, eg trim ballon.png to ballon
        image = image.substring(0, image.length() - 4);
        Log.d("image", image);
        int imageid = getResourceId(image, "drawable", getPackageName());
        for (ImageView fish : allFish) {
            fish.setImageResource(imageid);
        }
        //Special situation for bowl where it needs to time out, other screens do not time out
        if (image.equals("bowl") || image.charAt(0) == 'n') {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 5000ms, accounting for an additional 2 seconds of the
                    //star animation
                    endTrial(5, "NA");
                    //}
                }
            }, 7000);
        }
        else {
            //Remove the time out if something else has happened, such as the button being pressed
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }
        //reset  trial variables
        startTime = System.currentTimeMillis();
    }

    //Function to get trials from database
    private void loadTrials() {
        trials = db.getAllFlankerTrials();
    }

    //Function to return to menu using intent
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
        leftButton = (ImageView) findViewById(R.id.left_arrow);
        leftButton.setClickable(true);
        //Set a clicklistener to record the time after any click and take the difference as
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeedback(false);
                endTrial((int)((System.currentTimeMillis()-startTime) / 1000.0), "left");
            }
        });

        rightButton = (ImageView) findViewById(R.id.right_arrow);
        rightButton.setClickable(true);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeedback(false);
                endTrial((int)((System.currentTimeMillis()-startTime) / 1000.0), "right");
            }
        });

        fish1 = (ImageView) findViewById(R.id.fish1);

        allFish = new ArrayList<ImageView>();
        allFish.add(fish1);

        //Initialize feedback view for focusing animated star after each trial
        feedback_panel = (TextView) findViewById(R.id.feedback_panel);
        feedback_text = (TextView) findViewById(R.id.feedback_text);
        feedback_anim = findViewById(R.id.star);
        feedback_anim.setClickable(false);
    }
    private void endTrial(int time, String response) {
        //Remove handler if there is one, so that the handler doesn't move the trial
        //to the next screen
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        FlankerResult result = new FlankerResult(
                currTrial, response.equals(currTrial.direction), time,
                response,  participant
        );

        db.addFlankerResult(result, context);

        if (trialIterator.hasNext()) {
            //If there's more trials move to the next trial 
            displayFeedback(false);
            startTrial(trialIterator.next());
        } else {
            //else there's no more trials, finish the game and return to menu
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
