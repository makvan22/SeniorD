package leadgames.cis400.leadgames;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;


public class GameThreeActivity extends AppCompatActivity {

    private ArrayList<StructuralTrial> Trails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_three);
        //TODO: init views
        //TODO: run trials
        //TODO: record trials / update database
    }
    
    private final class Scene {
        private ImageView imageView;
        private boolean selected;
        private boolean ambiguous;

        public Scene (ImageView imageView, boolean ambiguous) {
            this.selected = false;
            this.ambiguous = ambiguous;
            this.imageView = imageView;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                selected = !selected;
                }
            });
        }

        public boolean isAmbiguous() {
            return ambiguous;
        }
    };
}
