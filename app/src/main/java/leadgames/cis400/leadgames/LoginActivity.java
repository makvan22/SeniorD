package leadgames.cis400.leadgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseManager fbm = FirebaseManager.getInstance();
        fbm.populatePutGameDb(new InputStream[]{
                        getResources().openRawResource(R.raw.putlist_1),
                        getResources().openRawResource(R.raw.putlist_2),
                        getResources().openRawResource(R.raw.putlist_3),
                        getResources().openRawResource(R.raw.putlist_4),
                        getResources().openRawResource(R.raw.putlist_5),
                        getResources().openRawResource(R.raw.putlist_6),
                        getResources().openRawResource(R.raw.putlist_7),
                        getResources().openRawResource(R.raw.putlist_8)
                }
        );

        Button submit = findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textFirstName = findViewById(R.id.first_name);
                EditText textLastName = findViewById(R.id.last_name);
                EditText textStudentID = findViewById(R.id.id);
                EditText textDob = findViewById(R.id.dob);
                EditText textGender = findViewById(R.id.gender);


                Intent mainIntent = new Intent(LoginActivity.this,GameMenu.class);
                LoginActivity.this.startActivity(mainIntent);
                LoginActivity.this.finish();
            }
        });

    }

}
