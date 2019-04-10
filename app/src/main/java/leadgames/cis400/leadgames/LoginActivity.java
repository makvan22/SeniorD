package leadgames.cis400.leadgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                boolean cleanInput = true;
                String firstName = ((EditText) findViewById(R.id.first_name)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.last_name)).getText().toString();
                String studentId = ((EditText) findViewById(R.id.id)).getText().toString();
                String dob = ((EditText) findViewById(R.id.dob)).getText().toString();
                String gender = ((EditText) findViewById(R.id.gender)).getText().toString();

                TextInputLayout firstNamelayout = findViewById(R.id.first_name_layout);
                if (firstName == null || firstName.equals("")) {
                    firstNamelayout.setError("Must enter first name");
                    cleanInput = false;
                }

                TextInputLayout lastNamelayout = findViewById(R.id.last_name_layout);
                if (lastName == null || lastName.equals("")) {
                    lastNamelayout.setError("Must enter last name");
                    cleanInput = false;
                }

                TextInputLayout studentIdLayout = findViewById(R.id.id_layout);
                if (studentId == null || studentId.equals("")) {
                    studentIdLayout.setError("Must enter student ID");
                    cleanInput = false;
                }

                TextInputLayout dobLayout = findViewById(R.id.dob_layout);
                if (dob == null || dob.equals("")) {
                    dobLayout.setError("Must enter date of birth (MM/DD/YYYY)");
                    cleanInput = false;
                }
                String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(dob);
                if (!matcher.matches()) {
                    dobLayout.setError("Format: MM/DD/YYYY");
                    cleanInput = false;
                }

                TextInputLayout genderLayout = findViewById(R.id.gender_layout);
                if (dob == null || dob.equals("")) {
                    dobLayout.setError("Must enter gender: female, male, N/A");
                    cleanInput = false;
                }

                if (!gender.equalsIgnoreCase("female")
                        && !gender.equalsIgnoreCase("male")
                        && !gender.equalsIgnoreCase("N/A")) {
                    genderLayout.setError("female, male, N/A");
                    cleanInput = false;
                }

                if (cleanInput) {
                    Participant p = new Participant(firstName, lastName, gender, dob, studentId);
                    Intent mainIntent = new Intent(LoginActivity.this,GameMenu.class);
                    mainIntent.putExtra("participant", (Serializable) p);
                    LoginActivity.this.startActivity(mainIntent);
                    LoginActivity.this.finish();
                }
            }
        });

    }

}
