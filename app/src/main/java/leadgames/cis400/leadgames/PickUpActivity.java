package leadgames.cis400.leadgames;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_ANIMAL;
import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_GOAL;
import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_PLATFORM;
import static leadgames.cis400.leadgames.PutObject.TARGET_ANIMAL;
import static leadgames.cis400.leadgames.PutObject.TARGET_GOAL;
import static leadgames.cis400.leadgames.PutObject.TARGET_PLATFORM;
import android.os.SystemClock;
public class PickUpActivity extends AppCompatActivity {

    private FirebaseManager db = FirebaseManager.getInstance();
    private Participant participant;

    private ImageView q1;
    private ImageView q2;
    private ImageView q3;
    private ImageView q4;

    private ImageView p1;
    private ImageView p2;
    private ImageView p3;
    private ImageView p4;

    private ImageView submit;
    private TextView feedback_panel;
    private TextView feedback_text;
    private LikeButtonView feedback_anim;

    private HashSet<ImageView> animals = new HashSet<ImageView>();
    private HashSet<ImageView> platforms = new HashSet<ImageView>();
    private ArrayList<Integer> quadrants = new ArrayList<>();

    private HashSet<Trial> trials = new HashSet<Trial>();
    private Iterator<Trial> trialIterator = null;
    private Trial currentTrial;
    boolean not_first = false;
    MediaPlayer mediaPlayer = null;

    String currTrial = "";
    PutObject currAnimal = null;
    PutObject currPlatform = null;
    ArrayList<PutObject> currPath = new ArrayList<>();
    ArrayList<PutPath> trialPath = new ArrayList<>();
    long startTime = 0;

    Integer moves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        Intent intent = getIntent();
        this.participant = (Participant) intent.getExtras().getSerializable("participant");

        // Load quadrants
        for (int i = 1; i <= 4; i++) {
            quadrants.add(i);
        }
        // Initialize views
        // Update xml to display current trail's objects
        initViews();
        loadTrials();
        if (trials.isEmpty()) {
            backToMenu();
        }
        trialIterator = trials.iterator();
        if (trialIterator.hasNext()) {
            currentTrial = trialIterator.next();
            startTrial(currentTrial);
        }
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

    private void startTrial(Trial trial) {
        /* XML is divided into 4 quadrants
           Q1 - upper left  Q2 - upper right  Q3 - bottom left Q4 - bottom right
         */

        startTime = 0;
        currTrial = trial.getId();
        //removing the 'wav' at the end
        String sound = trial.getSoundFile();
        sound = sound.substring(0, sound.length() - 3);

        clearPlatforms();
        clearAnimals();
        // Shuffle quadrants to determine randomly assign position of objects
        System.out.println("Running trial: " + trial.getId());

        Collections.shuffle(quadrants);
        clearPlatforms();
        clearAnimals();
        //Log.d("myTag2", sound);
        int soundid = getResourceId(sound, "raw", getPackageName());
        //Log.d("myTag2", Integer.toString(getResourceId(t1.getSoundFile(), "raw", getPackageName())));
        //Log.d("myTag3", Integer.toString(soundid));
        // Update xml to display current trail's objects
        //MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), soundid);
        if (not_first) {
            Log.d("not first", "not first");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 3000ms
                    //if (sound != null && sound.length() > 0) {

                    mediaPlayer.start();
                    //}
                }
            }, 3000);
        }
        else {
            //shorter delay for first time
            Log.d("first", "first time");
            not_first = true;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 3000ms
                    //if (sound != null && sound.length() > 0) {

                    mediaPlayer.start();
                    //}
                }
            }, 1000);
        }


        //MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        //mediaPlayer.start();

        startTime = SystemClock.elapsedRealtime();
        setAnimalView(trial.getTargetAnimal(), quadrants.get(0), TARGET_ANIMAL);
        setPlatformView(trial.getTargetPlatform(), quadrants.get(0), TARGET_PLATFORM);

        setAnimalView(trial.getDistractorAnimal(), quadrants.get(1), DISTRACTOR_ANIMAL);
        setPlatformView(trial.getDistractorPlatform(), quadrants.get(1), DISTRACTOR_PLATFORM);

        setPlatformView(trial.getTargetGoal(), quadrants.get(2), TARGET_GOAL);
        setPlatformView(trial.getDistractorGoal(), quadrants.get(3), DISTRACTOR_GOAL);

    }

    public void endTrial() {
        //Gather Trial results
        //Store last path animal path
        if (currAnimal != null) {
            PutPath animalPath = new PutPath(currAnimal, currPath);
            trialPath.add(animalPath);
            System.out.println("Trial path: " + trialPath.toString());
        }

        //Determine correctness of trial
        Boolean correct = false;
        if (currAnimal != null && currAnimal == TARGET_ANIMAL
                && currPlatform != null && currPlatform == TARGET_GOAL) {
            correct = true;
        }

        long difference = System.currentTimeMillis() - startTime;
        System.out.println(difference);
        int t = (int) (difference / 1000.0);
        PutResult result = new PutResult(currTrial, correct, t, trialPath, participant, currentTrial);
        System.out.println(result.toString());
        //TODO: add warning suppress
        db.addPutResult(result, getApplicationContext());
        //run next trial
        if (trialIterator.hasNext()) {
            //display feedback(0)
            displayFeedback(false);
            currentTrial = trialIterator.next();
            startTrial(currentTrial);
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

    private void initViews() {
        //Initialize animal views
        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        animals.add(q1);
        animals.add(q2);
        animals.add(q3);
        animals.add(q4);

        //Initialize platform views
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);

        platforms.add(p1);
        platforms.add(p2);
        platforms.add(p3);
        platforms.add(p4);

        //Initialize submit view
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

     private void loadTrials() {
        for (Trial t : db.getAllPutTrials()) {
            trials.add(t);
        }
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

    private void backToMenu() {
        Intent mainIntent = new Intent(PickUpActivity.this, GameMenu.class);
        PickUpActivity.this.startActivity(mainIntent);
        PickUpActivity.this.finish();
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

