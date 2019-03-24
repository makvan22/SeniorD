package leadgames.cis400.leadgames;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.media.MediaPlayer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_ANIMAL;
import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_GOAL;
import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_PLATFORM;
import static leadgames.cis400.leadgames.PutObject.TARGET_ANIMAL;
import static leadgames.cis400.leadgames.PutObject.TARGET_GOAL;
import static leadgames.cis400.leadgames.PutObject.TARGET_PLATFORM;


public class PickUpActivity extends AppCompatActivity {

    private FirebaseManager db = FirebaseManager.getInstance();
    private ImageView q1;
    private ImageView q2;
    private ImageView q3;
    private ImageView q4;

    private ImageView p1;
    private ImageView p2;
    private ImageView p3;
    private ImageView p4;

    private ImageView submit;
    private TextView feedback;

    private HashSet<ImageView> animals = new HashSet<ImageView>();
    private HashSet<ImageView> platforms = new HashSet<ImageView>();
    private ArrayList<Integer> quadrants = new ArrayList<>();

    private HashSet<Trial> trials = new HashSet<Trial>();
    private Iterator<Trial> trialIterator = null;

    String currTrial = "";
    PutObject currAnimal = null;
    PutObject currPlatform = null;
    ArrayList<PutObject> currPath = new ArrayList<>();
    ArrayList<PutPath> trialPath = new ArrayList<>();


    Integer moves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        // Load quadrants
        for (int i = 1; i <= 4; i++) {
            quadrants.add(i);
        }
        // Initialize views
        // Update xml to display current trail's objects
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
    }

    private void startTrial(Trial trial) {
        /* XML is divided into 4 quadrants
           Q1 - upper left  Q2 - upper right  Q3 - bottom left Q4 - bottom right
         */
        currTrial = trial.getId();
        clearPlatforms();
        clearAnimals();
        // Shuffle quadrants to determine randomly assign position of objects
        System.out.println("Running trial: " + trial.getId());

        Collections.shuffle(quadrants);
        clearPlatforms();
        clearAnimals();
        // Update xml to display current trail's objects
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        mediaPlayer.start();
        
        setAnimalView(trial.getTargetAnimal(), quadrants.get(0), TARGET_ANIMAL);
        setPlatformView(trial.getTargetPlatform(), quadrants.get(0), TARGET_PLATFORM);

        setAnimalView(trial.getDistractorAnimal(), quadrants.get(1), DISTRACTOR_ANIMAL);
        setPlatformView(trial.getDistractorPlatform(), quadrants.get(1), DISTRACTOR_PLATFORM);

        setPlatformView(trial.getTargetGoal(), quadrants.get(2), TARGET_GOAL);
        setPlatformView(trial.getDistractorGoal(), quadrants.get(3), DISTRACTOR_GOAL);

    }

    public void endTrial() {

        //Store last path animal path
        if (currAnimal != null) {
            PutPath animalPath = new PutPath(currAnimal, currPath);
            trialPath.add(animalPath);
            System.out.println("Trial path: " + trialPath.toString());
        }
        //Gather Trial results
        //Determine correctness of trial
        Boolean correct = false;
        if (currAnimal != null && currAnimal == TARGET_ANIMAL
                && currPlatform != null && currPlatform == TARGET_GOAL) {
            correct = true;
        }

        //TODO: replace time with actual trial time.
        PutResult result = new PutResult(currTrial, correct, 0, trialPath);
        System.out.println(result.toString());
        db.addPutResult(result);
        //run next trial
        if (trialIterator.hasNext()) {
            //display feedback(0)
            displayFeedback(false);
            startTrial(trialIterator.next());
        } else {
            //TODO: create an end of game display before return to menu page
            displayFeedback(true);
            Intent mainIntent = new Intent(PickUpActivity.this,MenuActivity.class);
            PickUpActivity.this.startActivity(mainIntent);
            PickUpActivity.this.finish();
        }
    }

    private void initViews() {
        //Initialize animal views
        q1 = (ImageView) findViewById(R.id.q1);
        q2 = (ImageView) findViewById(R.id.q2);
        q3 = (ImageView) findViewById(R.id.q3);
        q4 = (ImageView) findViewById(R.id.q4);
        animals.add(q1);
        animals.add(q2);
        animals.add(q3);
        animals.add(q4);

        //Initialize platform views
        p1 = (ImageView) findViewById(R.id.p1);
        p2 = (ImageView) findViewById(R.id.p2);
        p3 = (ImageView) findViewById(R.id.p3);
        p4 = (ImageView) findViewById(R.id.p4);

        platforms.add(p1);
        platforms.add(p2);
        platforms.add(p3);
        platforms.add(p4);

        //Initialize submit view
        submit = (ImageView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Submit clicked!");
                endTrial();
            }
        });

        //Initialize feedback view
        feedback = (TextView) findViewById(R.id.feedback);
    }

    private void clearPlatforms() {
        for (ImageView view : platforms) {
            view.setOnDragListener(null);
            view.setOnTouchListener(null);
            view.setVisibility(View.INVISIBLE);
        }
    }
    private void clearAnimals() {
        for (ImageView view : animals) {
            view.setOnTouchListener(null);
            view.setOnDragListener(null);
            view.setVisibility(View.INVISIBLE);
        }
    }

     // TODO: Load trials from database
     private void loadTrials() {

//        for (Trial t : fbm.getAllPutTrials()) {
//            trials.add(t);
//        }

        // Temporarily hardcoded trials
        Trial t1 = new Trial("1", "pig", "towel", "leaf",
                "dog", "book", "towel");

        Trial t2 = new Trial("2", "bear", "box", "balloon",
                "bear", "balloon", "napkin");

        Trial t3 = new Trial("3", "elephant", "circle", "leaf",
                "bear", "leaf", "book");

        Trial t4 = new Trial("4", "frog", "circle", "leaf",
                 "cow", "leaf", "book");

        Trial t5 = new Trial("5", "horse", "circle", "leaf",
                 "lion", "leaf", "book");

        trials.add(t1);
        trials.add(t2);
        trials.add(t3);
        trials.add(t4);
        trials.add(t5);
    }

    private void setAnimalView(String imageName, int quad, PutObject animalType) {
        ImageView view;
        switch (quad) {
            case 1:
                view = q1;
                break;
            case 2:
                view = q2;
                break;
            case 3:
                view = q3;
                break;
            default:
                view = q4;
                break;
        }
        view.setImageResource(ImageFinder.getImageResource(imageName));
        //Tag =>  "animalType , imageName"
        view.setTag(animalType + "," + imageName);
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
        view.setOnTouchListener(new TouchListener());
        // Sanity check: no animal should act as a drop zone
        view.setOnDragListener(null);
    }

    private void setPlatformView(String obj, int quad, PutObject platformType) {
        ImageView view;
        ImageView animalQ;
        switch (quad) {
            case 1:
                view = p1;
                animalQ = q1;
                break;
            case 2:
                view = p2;
                animalQ = q2;
                break;
            case 3:
                view = p3;
                animalQ = q3;
                break;
            default:
                view = p4;
                animalQ = q4;
                break;
        }
        view.setImageResource(ImageFinder.getImageResource(obj));
        view.setVisibility(View.VISIBLE);
        view.setOnDragListener(new DragListener(animalQ, platformType));
        view.setTag(platformType);
        // Sanity check: no platform can be draggable
        view.setClickable(false);
        view.setOnTouchListener(new TouchListener());
    }

    private void displayFeedback(final boolean game_over) {
        if (game_over) {
            feedback.setText(R.string.game_done);
        } else {
            feedback.setText(R.string.trial_done);
        }
        feedback.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!game_over) {
                    feedback.setVisibility(View.INVISIBLE);
                }
            }
        }, 2000);
    }

    private final class TouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //Set curr animal to view being selected
                String tag = (String) view.getTag();
                System.out.println("Tag: " + tag);
                //Fetch animal type from tag
                PutObject animalUp = PutObject.valueOf(tag.split(",")[0]);
                if (currAnimal == null) {
                    currAnimal = animalUp;
                    currPath = new ArrayList<PutObject>();
                } else if (currAnimal != animalUp) {
                    //previous animal's path initiate new animal's path
                    PutPath animalPath = new PutPath(currAnimal, currPath);
                    trialPath.add(animalPath);
                    System.out.println("Trial path: " + trialPath.toString());
                    currAnimal = animalUp;
                    currPath = new ArrayList<PutObject>();
                }
                System.out.println("Animal up: " + animalUp);
                System.out.println("Currpath: " + currPath.toString());

                //Start drag
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class DragListener implements View.OnDragListener {
        private ImageView animalQ;
        private PutObject platformType;
        public DragListener(ImageView animalQ, PutObject platformType){
            this.animalQ = animalQ;
            this.platformType =  platformType;
        }
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View view = (ImageView) event.getLocalState();
                    view.setVisibility(View.INVISIBLE);

                    //Replace old animal view with that being dropped on current platform
                    String tag = (String) view.getTag();
                    System.out.println("Tag: " + tag);
                    animalQ.setTag(tag);
                    //Fetch image name from tag
                    animalQ.setImageResource(ImageFinder.getImageResource(tag.split(",")[1]));
                    animalQ.setVisibility(View.VISIBLE);
                    animalQ.setOnTouchListener(new TouchListener());
                    currPath.add(this.platformType);
                    currPlatform = this.platformType;
                    System.out.println("Curr path: " + currPath.toString());
                    moves += 1;
                    System.out.println("Moves: " + moves);
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}

