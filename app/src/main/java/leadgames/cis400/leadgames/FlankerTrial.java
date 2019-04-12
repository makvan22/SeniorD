package leadgames.cis400.leadgames;

import com.google.firebase.database.DataSnapshot;
import java.util.HashMap;
import java.util.Map;

public class FlankerTrial {
    String image;
    //left or right
    String direction;
    //practice or test
    String block;

    private String id;
    private String condition;
    private String expectedResponse;
    private String switched;
    private String switchType;


    public FlankerTrial (DataSnapshot dataSnapshot) {
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
        this.id = dataSnapshot.getKey();
        this.image=object.get("image").toString();
        this.direction=object.get("direction").toString();
        this.block=object.get("block").toString();
        this.condition=object.get("condition").toString();
    }

    public FlankerTrial (String id, String image, String direction, String block, String condition,
                         String expectedResponse, String switched, String switchType) {
        this.id = id;
        this.image=image;
        this.direction=direction;
        this.block=block;
        this.condition=condition;
        this.expectedResponse=expectedResponse;
        this.switched = switched;
        this.switchType = switchType;
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

    public String getExpectedResponse() { return expectedResponse; }

    public String getSwitched() { return switched; }

    public String getSwitchType() { return switchType; }

}
