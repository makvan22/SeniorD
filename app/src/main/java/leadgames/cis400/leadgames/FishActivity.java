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

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */


public class FishActivity extends AppCompatActivity {
    final int rightImgId = R.mipmap.righty;
    final int leftImgId = R.mipmap.lefty;

    private ArrayList<ImageView> allFish;
    private ArrayList<ImageView> leftFish;
    private ArrayList<ImageView> rightFish;
    private ImageView fish1;
    private ImageView fish2;
    private ImageView fish3;
    private ImageView fish4;
    private ImageView fish5;

    private ImageView leftButton;
    private ImageView rightButton;

    private int wins = 0;
    private int plays=0;
    //LEFT = 0, RIGHT = 1;
    private int midFishDir = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish);
        init();
    }

    private void init() {
        leftButton = (ImageView) findViewById(R.id.left_arrow);
        leftButton.setClickable(true);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (midFishDir == 0) {
                    wins += 1;
                    Log.d("Played",  "left win");
                }
                plays += 1;
                shuffle();
            }
        });

        rightButton = (ImageView) findViewById(R.id.right_arrow);
        rightButton.setClickable(true);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (midFishDir == 1) {
                    wins += 1;
                    Log.d("Played",  "right win");
                }
                plays += 1;
                shuffle();
            }
        });

        fish1 = (ImageView) findViewById(R.id.fish1);
        fish2 = (ImageView) findViewById(R.id.fish2);
        fish3 = (ImageView) findViewById(R.id.fish3);
        fish4 = (ImageView) findViewById(R.id.fish4);
        fish5 = (ImageView) findViewById(R.id.fish5);

        allFish = new ArrayList<ImageView>();
        allFish.add(fish1);
        allFish.add(fish2);
        allFish.add(fish3);
        allFish.add(fish4);
        allFish.add(fish5);

        leftFish = new ArrayList<ImageView>();
        leftFish.add(fish1);
        leftFish.add(fish2);

        rightFish = new ArrayList<ImageView>();
        rightFish.add(fish4);
        rightFish.add(fish5);
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

    private void shuffle() {
        int dir1;
        int dir2;
        //all in same direction
        if (wins < 4) {
            dir1 = setMidDirection();
            for (ImageView fish : allFish) {
                fish.setImageResource(dir1);
            }
            return;
        }
        //surrounding in opposite side
        if (wins < 7 ) {
            dir1 = setMidDirection();
            if (dir1 == rightImgId) {
                dir2 = leftImgId;
            } else {
                dir2 = rightImgId;
            }
            fish3.setImageResource(dir1);
            for (ImageView fish : leftFish) {
                fish.setImageResource(dir2);
            }
            for (ImageView fish : rightFish) {
                fish.setImageResource(dir2);
            }
            return;
        }
        //left and right not necessarily in same direction
       if (wins < 11) {
            dir1 = setMidDirection();
            dir2 = getDirection();
            int dir3 = getDirection();
            fish3.setImageResource(dir1);
            fish2.setImageResource(dir2);
            fish4.setImageResource(dir2);
            fish1.setImageResource(dir3);
            fish5.setImageResource(dir3);
            return;
        }
        //fully randomized
        fish3.setImageResource(setMidDirection());
        for (ImageView fish : leftFish) {
            fish.setImageResource(getDirection());
        }
        for (ImageView fish : leftFish) {
            fish.setImageResource(getDirection());
        }
        return;
    }
}
