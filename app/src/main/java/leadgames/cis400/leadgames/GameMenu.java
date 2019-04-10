package leadgames.cis400.leadgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
                Intent mainIntent = new Intent(GameMenu.this,FishActivity.class);
                mainIntent.putExtra("participant", participant);
                GameMenu.this.startActivity(mainIntent);
                GameMenu.this.finish();
            }
        });

        ImageView putView = findViewById(R.id.imageView3);
        putView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameMenu.this,PickUpActivity.class);
                mainIntent.putExtra("participant", participant);
                GameMenu.this.startActivity(mainIntent);
                GameMenu.this.finish();
            }
        });

        ImageView matchingView = findViewById(R.id.imageView4);
        matchingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameMenu.this,FishActivity.class);
                mainIntent.putExtra("participant", participant);
                GameMenu.this.startActivity(mainIntent);
                GameMenu.this.finish();
            }
        });
    }


}
