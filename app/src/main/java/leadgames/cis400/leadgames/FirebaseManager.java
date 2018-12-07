package leadgames.cis400.leadgames;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference userRef = database.getReference("lead-games");

    public static void writeScore(Map<String, Integer> scores) {
        if (scores == null) {
            scores = new HashMap<>();
            scores.put("l1", 3);
            scores.put("l2", 4);
            scores.put("l3", 7);
            scores.put("l4", 21);
        }
        userRef.child("Alice Zhou").setValue(scores);
    }

}
