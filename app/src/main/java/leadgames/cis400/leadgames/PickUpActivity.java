package leadgames.cis400.leadgames;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class PickUpActivity extends AppCompatActivity {
    private HashSet<Trial> trials = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        loadTrials();
        if (trials == null || trials.isEmpty()) {
            //TODO: decide on null behaviour  & implement
           return;
        }
        Iterator<Trial> trialIterator = trials.iterator();


    }

    private void init() {
        //load trials
        loadTrials();
        //set images
    }

    //TODO: Load trials from database
    private void loadTrials() {
        //temporary hardcoded trials
        Trial t1 = new Trial(1, "pig", "towel", "dog",
                "leaf", "book", "towel");

        Trial t2 = new Trial(2, "frog", "balloon",
                "frog","napkin", "box",
                "balloon");

        Trial t3 = new Trial(3, "frog", "leaf", "bear",
                "book", "circle", "leaf");

        Trial t4 = new Trial(4, "elephant", "pan",
                "elephant","balloon", "book",
                "pan");
        trials.add(t1);
        trials.add(t2);
        trials.add(t3);
        trials.add(t4);
    }
}
