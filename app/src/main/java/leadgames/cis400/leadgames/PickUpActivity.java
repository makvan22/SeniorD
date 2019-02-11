package leadgames.cis400.leadgames;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class PickUpActivity extends AppCompatActivity {

    private ImageView q1;
    private ImageView q2;
    private ImageView q3;
    private ImageView q4;

    private ImageView p1;
    private ImageView p2;
    private ImageView p3;
    private ImageView p4;

    private ImageView animalUp = null;
    private ImageView platformDown = null;
    private HashSet<ImageView> animals = new HashSet<ImageView>();
    private HashSet<ImageView> platforms = new HashSet<ImageView>();
    private HashSet<Trial> trials = new HashSet<Trial>();
    private ArrayList<Integer> quadrants = new ArrayList<>();

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
        Iterator<Trial> trialIterator = trials.iterator();
        while (trialIterator.hasNext()) {
            runTrial(trialIterator.next());
        }
    }

    private void runTrial(Trial trial) {
        /* XML is divided into 4 quadrants
           Q1 - upper left  Q2 - upper right  Q3 - bottom left Q4 - bottom right
         */

        clearPlatforms();
        clearAnimals();
        // Shuffle quadrants to determine randomly assign position of objects
        System.out.println("Running trial: " + trial.getId());
        Collections.shuffle(quadrants);
        clearPlatforms();
        clearAnimals();
        // Update xml to display current trail's objects
        setAnimalView(trial.getTargetAnimal(), quadrants.get(0));
        setPlatformView(trial.getTargetPlatform(), quadrants.get(0));

        setAnimalView(trial.getDistractorAnimal(), quadrants.get(1));
        setPlatformView(trial.getDistractorPlatform(), quadrants.get(1));

        setPlatformView(trial.getTargetGoal(), quadrants.get(2));
        setPlatformView(trial.getDistractorGoal(), quadrants.get(3));

        Integer targetAnimalQ = quadrants.get(0);
        Integer distractorAnimalQ = quadrants.get(1);
        Integer targetGoalQ = quadrants.get(2);
        Integer distractorGoalQ = quadrants.get(3);
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

    //TODO: Load trials from database
    private void loadTrials() {
        //temporary hardcoded trials
        Trial t1 = new Trial(1, "pig", "towel", "dog",
                "leaf", "book", "towel");

        Trial t2 = new Trial(2, "frog", "balloon",
                "frog", "napkin", "box",
                "balloon");

        Trial t3 = new Trial(3, "frog", "leaf", "bear",
                "book", "circle", "leaf");

        Trial t4 = new Trial(4, "elephant", "pan",
                "elephant", "balloon", "book",
                "pan");
        trials.add(t1);
//        trials.add(t2);
//        trials.add(t3);
//        trials.add(t4);
    }

    private void setAnimalView(String imageName, int quad) {
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
        view.setImageResource(getImageResource(imageName));
        view.setTag(getImageResource(imageName));
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
        view.setOnTouchListener(new TouchListener());
        // Sanity check: no animal should act as a drop zone
        view.setOnDragListener(null);
    }

    private void setPlatformView(String obj, int quad) {
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
        view.setImageResource(getImageResource(obj));
        view.setVisibility(View.VISIBLE);
        view.setOnDragListener(new DragListener(animalQ));
        // Sanity check: no platform can be draggable
        view.setClickable(false);
        view.setOnTouchListener(new TouchListener());
    }

    private int getImageResource(String img) {
        switch (img) {
            case "balloon":
                return R.drawable.balloon;
            case "bear":
                return R.drawable.bear;
            case "book":
                return R.drawable.book;
            case "box":
                return R.drawable.box;
            case "circle":
                return R.drawable.circle;
            case "dog":
                return R.drawable.dog;
            case "elephant":
                return R.drawable.elephant;
            case "leaf":
                return R.drawable.leaf;
            case "napkin":
                return R.drawable.napkin;
            case "pan":
                return R.drawable.pan;
            case "pig":
                return R.drawable.pig;
            case "towel":
                return R.drawable.towel;
            default:
                return 0;
        }
    }

    private final class TouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                animalUp = (ImageView) view;
                System.out.println("animal up" + view.toString());
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class DragListener implements View.OnDragListener {
        private ImageView animalQ;
        public DragListener(ImageView animalQ){
            this.animalQ = animalQ;
        }
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View view = (ImageView) event.getLocalState();
                    view.setVisibility(View.INVISIBLE);
                    System.out.println("view tag: " + view.getTag());
                    animalQ.setImageResource((int) view.getTag());
                    animalQ.setTag(view.getTag());
                    animalQ.setVisibility(View.VISIBLE);
                    animalQ.setOnTouchListener(new TouchListener());
                    platformDown = (ImageView) view;
                    moves += 1;
                    System.out.println("Platform down: " + view.toString());
                    System.out.println("Moves: " + moves);
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}

