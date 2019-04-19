package leadgames.cis400.leadgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GameMenu extends AppCompatActivity {

    private Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.participant = (Participant) intent.getExtras().getSerializable("participant");

        ImageView flankerView = findViewById(R.id.imageView2);
        flankerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameMenu.this,FlankerActivity.class);
                mainIntent.putExtra("participant", participant);
                GameMenu.this.startActivity(mainIntent);
            }
        });

        ImageView putView = findViewById(R.id.imageView3);
        putView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAlpha(0.3f);
                showListButtions();
//                Intent mainIntent = new Intent(GameMenu.this,PickUpActivity.class);
//                mainIntent.putExtra("participant", participant);
//                GameMenu.this.startActivity(mainIntent);
            }
        });

        ImageView matchingView = findViewById(R.id.imageView4);
        matchingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameMenu.this,AmbiguityDetectionActivity.class);
                mainIntent.putExtra("participant", participant);
                GameMenu.this.startActivity(mainIntent);
            }
        });
    }

    private void showListButtions() {
        Button[] buttons = { findViewById(R.id.list1_button)
                ,findViewById(R.id.list2_button)
                ,findViewById(R.id.list3_button)
                ,findViewById(R.id.list4_button)
                ,findViewById(R.id.list5_button)
                ,findViewById(R.id.list6_button)
                ,findViewById(R.id.list7_button)
                ,findViewById(R.id.list8_button) };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.VISIBLE);
            final int currList = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    participant.setListSelection(currList);
                    Intent mainIntent = new Intent(GameMenu.this,PickUpActivity.class);
                    mainIntent.putExtra("participant", participant);
                    GameMenu.this.startActivity(mainIntent);
                }
            });

        }

    }




}
