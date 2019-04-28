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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_ANIMAL;
import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_GOAL;
import static leadgames.cis400.leadgames.PutObject.DISTRACTOR_PLATFORM;
import static leadgames.cis400.leadgames.PutObject.TARGET_ANIMAL;
import static leadgames.cis400.leadgames.PutObject.TARGET_GOAL;
import static leadgames.cis400.leadgames.PutObject.TARGET_PLATFORM;

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

    private List<ImageView> quadrantViews = new LinkedList<ImageView>();

    private List<Trial> trials = new LinkedList<>();
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

        // Initialize views
        // Update xml to display current trail's objects
        initViews();
        loadTrials();
        if (trials.isEmpty()) {
            //backToMenu();
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


    private void loadTrialViews(Trial trial) {
        setQuadrantView(trial.getTargetAnimal(), trial.getTargetLocation(), TARGET_ANIMAL);
        setQuadrantView(trial.getTargetPlatform(), trial.getTargetLocation(), TARGET_PLATFORM);

        setQuadrantView(trial.getDistractorAnimal(), trial.getDistractorPlatformLocation(), DISTRACTOR_ANIMAL);
        setQuadrantView(trial.getDistractorPlatform(), trial.getDistractorPlatformLocation(), DISTRACTOR_PLATFORM);

        setQuadrantView(trial.getTargetGoal(), trial.getTargetGoalLocation(), TARGET_GOAL);
        setQuadrantView(trial.getDistractorGoal(),trial.getCompetitorLocation(), DISTRACTOR_GOAL);
    }

    private void identifyViews() {
        hideQuadrantViews();
        Iterator<ImageView> views = quadrantViews.iterator();
        ImageView currView = null;
        while (views.hasNext()) {
            currView = views.next();
            currView.setVisibility(View.VISIBLE);
            String sound = (String) currView.getTag();
            sound = sound.split(",")[1];
            int soundid = getResourceId(sound, "raw", getPackageName());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), soundid);
            mediaPlayer.start();
        }
    }

    private void startTrial(Trial trial) {
        /* XML is divided into 4 quadrants
           Q1 - upper left  Q2 - upper right  Q3 - bottom left Q4 - bottom right
         */

        startTime = 0;
        currTrial = trial.getId();
        clearQuadrantViews();

        System.out.println("Running trial: " + trial.getId());

        String sound = trial.getSoundFile();
        sound = sound.substring(0, sound.length() - 3);
        int soundid = getResourceId(sound, "raw", getPackageName());
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
        } else {
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
        loadTrialViews(trial);
        startTime = System.currentTimeMillis();
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

        //Initialize platform views
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);

        quadrantViews.add(p1);
        quadrantViews.add(p2);
        quadrantViews.add(p3);
        quadrantViews.add(p4);
        quadrantViews.add(q1);
        quadrantViews.add(q2);
        quadrantViews.add(q3);
        quadrantViews.add(q4);


        //Initialize submit view
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Submit clicked!");
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                endTrial();
            }
        });

        //Initialize feedback view
        feedback_panel = findViewById(R.id.feedback_panel);
        feedback_text = findViewById(R.id.feedback_text);
        feedback_anim = findViewById(R.id.star);
    }

    private void clearQuadrantViews() {
        for (ImageView view : quadrantViews) {
            view.setOnTouchListener(null);
            view.setOnDragListener(null);
            view.setVisibility(View.INVISIBLE);
        }
    }

    private void hideQuadrantViews() {
        for (ImageView view : quadrantViews) {
            view.setVisibility(View.INVISIBLE);
        }
    }

     private void loadTrials() {
        trials =  db.getAllPutTrials();
        for (Trial t : db.getAllPutTrials()) {
            trials.add(t);
        }
    }



    public void setQuadrantView(String occupant, PUT_LOCATION loc, PutObject objectType) {
//        this.objectType = objectType;
//        this.occupant = occupant;
        Boolean isPlatform = (objectType == TARGET_PLATFORM ||
                objectType == DISTRACTOR_PLATFORM  ||
                objectType == TARGET_GOAL ||
                objectType == DISTRACTOR_GOAL);

        ImageView platformView;
        ImageView animalView;

        switch (loc) {
            case UPPER_LEFT :
                platformView = p1;
                animalView = q1;
                break;
            case UPPER_RIGHT:
                platformView = p2;
                animalView = q2;
                break;
            case LOWER_LEFT:
                platformView = p3;
                animalView = q3;
                break;
            default:
                platformView = p4;
                animalView = q4;
                break;
        }

        if (isPlatform) {
            platformView.setImageResource(ImageFinder.getImageResource(occupant));
            platformView.setVisibility(View.VISIBLE);
            platformView.setOnDragListener(new DragListener(animalView, objectType));
            platformView.setTag(objectType + "," + occupant);
            // Sanity check: no platform can be draggable
            platformView.setClickable(false);
            platformView.setOnTouchListener(new TouchListener());
        } else {
            animalView.setImageResource(ImageFinder.getImageResource(occupant));
            //Tag =>  "animalType , imageName"
            animalView.setTag(objectType + "," + occupant);
            animalView.setVisibility(View.VISIBLE);
            animalView.setClickable(true);
            animalView.setOnTouchListener(new TouchListener());
            // Sanity check: no animal should act as a drop zone
            animalView.setOnDragListener(null);
        }
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

//    private final class TouchListener implements View.OnTouchListener {
//        private QuadrantView qView;
//        public TouchListener(QuadrantView qView) {
//            this.qView = qView;
//        }
//
//        public boolean onTouch(ImageView imageView, MotionEvent motionEvent) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                //Set curr animal to view being selected
//                String tag = qView.getOccupant();
//                System.out.println("Tag: " + tag);
//                //Fetch animal type from tag
//                PutObject animalUp = PutObject.valueOf(tag);
//                if (currAnimal == null) {
//                    currAnimal = animalUp;
//                    currPath = new ArrayList<PutObject>();
//                } else if (currAnimal != animalUp) {
//                    //previous animal's path initiate new animal's path
//                    PutPath animalPath = new PutPath(currAnimal, currPath);
//                    trialPath.add(animalPath);
//                    System.out.println("Trial path: " + trialPath.toString());
//                    currAnimal = animalUp;
//                    currPath = new ArrayList<PutObject>();
//                }
//                System.out.println("Animal up: " + animalUp);
//                System.out.println("Currpath: " + currPath.toString());
//
//                //Start drag
//                ClipData data = ClipData.newPlainText("", "");
//                ImageView view = qView.getImageView();
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
//                      view);
//                view.startDrag(data, shadowBuilder, view, 0);
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }

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

    private final class QuadrantView {
        private ImageView imageView;
        private PutObject objectType;
        private String occupant;

        public QuadrantView(PUT_LOCATION loc, PutObject objectType, String occupant) {
            this.objectType = objectType;
            this.occupant = occupant;
            setQuadrantView(occupant, loc, objectType);
        }

        public void setOccupant(String occupant) {
           this.occupant = occupant;
        }

        public void setPlatform(PutObject putObject) {
            this.objectType = putObject;
        }

        public String getOccupant() {
            return this.occupant;
        }

        public PutObject getObjectType() {
            return this.objectType;
        }

        public ImageView getImageView() {
            return this.imageView;
        }

    }
}

