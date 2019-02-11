package leadgames.cis400.leadgames;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FirebaseManager {
    private volatile static FirebaseManager sFirebaseManager;

    private DatabaseReference putGameRef;
    private DatabaseReference putResultsRef;
    private PutGameDB mPutGame;

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
        populatePutGameDb();

        putResultsRef = FirebaseDatabase.getInstance().getReference("put-games");
    }

    public void populatePutGameDb() {
        addValueEventListenerToDb("put-games");
    }

    public List<Trial> getAllPutTrials() {
        return mPutGame.getAllPutTrials();
    }

    public void addPutResult(PutResult putResult) {

    }

    public void addValueEventListenerToDb(String db) {
        putGameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = (String) snapshot.child("/id").getValue();
                    String targetAnimal = (String) snapshot.child("/targetAnimal").getValue();
                    String targetGoal = (String) snapshot.child("/targetGoal").getValue();
                    String targetPlatform = (String) snapshot.child("/targetPlatform").getValue();
                    String distractorAnimal = (String) snapshot.child("/distractorAnimal").getValue();
                    String distractorGoal = (String) snapshot.child("/distractorGoal").getValue();
                    String distractorPlatform = (String) snapshot.child("/distractorPlatform").getValue();

                    mPutGame.addPutTrial(new Trial(id, targetAnimal, targetGoal, targetPlatform, distractorAnimal, distractorGoal, distractorPlatform));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void destroy() {
        sFirebaseManager=null;
    }

}
