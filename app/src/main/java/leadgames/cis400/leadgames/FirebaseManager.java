package leadgames.cis400.leadgames;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class FirebaseManager {
    private volatile static FirebaseManager sFirebaseManager;

    private DatabaseReference putGameRef;
    private DatabaseReference putResultsRef;

    private PutGameDB mPutGame;
    private FlankerDB mFlanker;
    private AmbiguityDetectionDB mAmbiguity;


    public static synchronized FirebaseManager getInstance() {
        if (sFirebaseManager == null) {
            synchronized (FirebaseManager.class) {
                sFirebaseManager = new FirebaseManager();
            }
        }
        return sFirebaseManager;
    }

    private FirebaseManager(){
        putGameRef = FirebaseDatabase.getInstance().getReference("put-games");
        mPutGame = new PutGameDB();
        mFlanker = new FlankerDB();
        mAmbiguity = new AmbiguityDetectionDB();
        //populatePutGameDb();

        putResultsRef = FirebaseDatabase.getInstance().getReference("put-results");
    }

    public void populatePutGameDb() {
        addValueEventListenerToDb("put-games");
    }

    public void populatePutGameDb(InputStream[] streamArr) {
        for (int i = 0; i < streamArr.length; i++) {
            try {
                mPutGame.readTrialFromInputStream(streamArr[i]);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void populateFlankerDb(InputStream stream) {
        try {
            mFlanker.readTrialFromInputStream(stream);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void populateAmbiguityDB(InputStream stream) {
        try {
            mAmbiguity.readTrialFromInputStream(stream);
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public List<Trial> getAllPutTrials() {
        return mPutGame.getAllPutTrials();
    }

    public List<FlankerTrial> getAllFlankerTrials() { return mFlanker.getAllTrials(); }

    public List<AmbiguityDetectionTrial> getAllAmbiguityDetectionTrials() { return mAmbiguity.getAllTrials(); }

    public void addFlankerResult(FlankerResult flankerResult, Context context) {
        addItemToSheet(flankerResult, context);
        //TODO: ADD TO FIREBASE
//        Participant participant = flankerResult.getParticipant();
//        String studentId = participant.getStudentId();
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        String date = dateFormat.format(new Date());
//
//        //TODO: FIX FOR FIREBASE
//        String trialId = encodeForFirebaseKey(studentId) + "/"
//                + encodeForFirebaseKey(date);
    }

    public void addPutResult(PutResult putResult, Context context) {
        addItemToSheet(putResult, context);
//        Participant participant = putResult.getParticipant();
//        String studentId = participant.getStudentId();
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        String date = dateFormat.format(new Date());
//
//        //TODO: FIX FOR FIREBASE
//        String trialId = encodeForFirebaseKey(studentId) + "/"
//                          + encodeForFirebaseKey(date);
//        List<PutPath> paths = putResult.getPaths();
//
//        putResultsRef.child(trialId + "/time").setValue(putResult.getTime());
//        putResultsRef.child(trialId + "/correct").setValue(putResult.getCorrect());
//
//        String selectedObj = paths.size() == 0 ? "" : paths.get(paths.size() - 1).getPutOjbect();
//        putResultsRef.child(trialId + "/selectedObject").setValue(selectedObj);
//
//        int pathInx = 0;
//        for (PutPath path : paths) {
//            String pathId = "path" + pathInx;
//            putResultsRef.child(trialId + "/paths/" + pathId + "/object").setValue(path.getPutOjbect());
//            putResultsRef.child(trialId + "/paths/" + pathId + "/objectPath").setValue(path.getPutObjectPath());
//            pathInx++;
//        }

    }

    public void addAmbiguityDetectionResult(AmbiguityDetectionResult result, Context context) {
        addItemToSheet(result, context);
    }

    public void addValueEventListenerToDb(String db) {
        putGameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = decodeFromFirebaseKey((String) snapshot.child("/id").getValue());
                    String targetAnimal = (String) snapshot.child("/target").getValue();
                    String targetGoal = (String) snapshot.child("/goal").getValue();
                    String targetPlatform = (String) snapshot.child("/target_plaform").getValue();
                    String distractorAnimal = (String) snapshot.child("/competitor").getValue();
                    String distractorGoal = (String) snapshot.child("/incorrect_goal").getValue();
                    String distractorPlatform = (String) snapshot.child("/comp_platform").getValue();
                    //TODO change from hardcode
                    String soundFile = "sample";

                    mPutGame.addPutTrial(new Trial(id, targetAnimal, targetGoal, targetPlatform, distractorAnimal, distractorGoal, distractorPlatform, soundFile));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addItemToSheet(ResultTrial trial, Context context) {
        final Map<String, String> params = trial.addToParams(new HashMap<String, String>());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxK4t30QzBnhjLx30gqGoNlLUUuzZSY6tFEOsqu-mR_69215TI/exec",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) { }
            }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    public static String encodeForFirebaseKey (String s) {
        s = s.replace(".", "_P%ë5nN*")
                .replace("$", "_D%5nNë*")
                .replace("#", "_H%ë5Nn*")
                .replace("[", "_Oë5n%N*")
                .replace("]", "_5nN*C%ë")
                .replace("/", "*_S%ë5nN")
        ;
        return s;
    }

    public String decodeFromFirebaseKey(String s) {

        s = s.replace("_P%ë5nN*", ".")
                .replace("_D%5nNë*", "$")
                .replace("_H%ë5Nn*", "#")
                .replace("_Oë5n%N*", "[")
                .replace("_5nN*C%ë", "]")
                .replace("*_S%ë5nN", "/");

        return s;
    }


    public void destroy() {
        sFirebaseManager=null;
    }

}
