package leadgames.cis400.leadgames;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference userRef = database.getReference("lead-games");

    public static String test() {
        return "hi";
    }

    public static void writeUser() {
        userRef.child("Alice").setValue("hello");
    }



}
