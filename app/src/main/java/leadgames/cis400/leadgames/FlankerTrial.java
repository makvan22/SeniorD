package leadgames.cis400.leadgames;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

public class FlankerTrial {
    String image;
    //left or right
    String direction;
    //practice or test
    String block;

    private String id;
    private String condition;


    public FlankerTrial (DataSnapshot dataSnapshot) {
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
        this.id = dataSnapshot.getKey();
        this.image=object.get("image").toString();
        this.direction=object.get("direction").toString();
        this.block=object.get("block").toString();
        this.condition=object.get("condition").toString();
    }

    public FlankerTrial (String id, String image, String direction, String block, String condition) {
        this.id = id;
        this.image=image;
        this.direction=direction;
        this.block=block;
        this.condition=condition;
    }

    public String getId (){
        return this.id;
    }

    public String getImage() { return image; }

    public String getDirection() {return direction; }

    public String getBlock() {
        return block;
    }

    public String getCondition() { return condition; }


}
