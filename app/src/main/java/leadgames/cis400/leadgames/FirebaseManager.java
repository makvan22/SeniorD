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

/**
 * FirebaseManager controls access to database resources and acts as a "one-stop-shop" for reading
 * and writing trial and experimental data.
 */

public class FirebaseManager {
    private volatile static FirebaseManager sFirebaseManager;

    private DatabaseReference putGameRef;
    private DatabaseReference putResultsRef;

    private PutGameDB mPutGame;
    private FlankerDB mFlanker;
    private AmbiguityDetectionDB mAmbiguity;


    /**
     * Singleton getter
     * @return
     */
    public static synchronized FirebaseManager getInstance() {
        if (sFirebaseManager == null) {
            synchronized (FirebaseManager.class) {
                sFirebaseManager = new FirebaseManager();
            }
        }
        return sFirebaseManager;
    }

    /**
     * Singleton constructor. Sets up the access points and data structures for all 3 games.
     */
    private FirebaseManager(){
        putGameRef = FirebaseDatabase.getInstance().getReference("put-games");
        mPutGame = new PutGameDB();
        mFlanker = new FlankerDB();
        mAmbiguity = new AmbiguityDetectionDB();

        putResultsRef = FirebaseDatabase.getInstance().getReference("put-results");
    }

    /**
     * Reads input streams
     * @param streamArr
     */
    public void populatePutGameDb(InputStream[] streamArr) {
        for (int i = 0; i < streamArr.length; i++) {
            try {
                mPutGame.readTrialFromInputStream(streamArr[i]);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Reads input streams
     * @param stream
     */

    public void populateFlankerDb(InputStream stream) {
        try {
            mFlanker.readTrialFromInputStream(stream);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Reads input streams
     * @param stream
     */

    public void populateAmbiguityDB(InputStream stream) {
        try {
            mAmbiguity.readTrialFromInputStream(stream);
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    /** returns trials from individual data storages */
    public List<Trial> getAllPutTrials() { return mPutGame.getAllPutTrials(); }
    public List<FlankerTrial> getAllFlankerTrials() { return mFlanker.getAllTrials(); }
    public List<AmbiguityDetectionTrial> getAllAmbiguityDetectionTrials() { return mAmbiguity.getAllTrials(); }

    /** adds trial results to Google sheet */
    public void addFlankerResult(FlankerResult flankerResult, Context context) {
        addItemToSheet(flankerResult, context);
    }

    public void addPutResult(PutResult putResult, Context context) {
        addItemToSheet(putResult, context);
    }

    public void addAmbiguityDetectionResult(AmbiguityDetectionResult result, Context context) {
        addItemToSheet(result, context);
    }

    /**
     * Issues a post request to a Google script which writes into the Google Sheet
     * @param trial
     * @param context
     */
    private void addItemToSheet(ResultTrial trial, Context context) {
        final Map<String, String> params = trial.addToParams(new HashMap<String, String>());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://script.google.com/macros/s/AKfycbxK4t30QzBnhjLx30gqGoNlLUUuzZSY6tFEOsqu-mR_69215TI/exec",
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

}
