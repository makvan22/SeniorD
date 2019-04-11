package leadgames.cis400.leadgames;

public final class ImageFinder {

    public static int getImageResource(String img) {
        switch (img) {
            case "balloon":
                return R.drawable.balloon;
            case "bear":
                return R.drawable.bear;
            case "book":
                return R.drawable.book;
            case "box":
                return R.drawable.box;
            case "circle":
                return R.drawable.circle;
            case "cow":
                return R.drawable.cow;
            case "dog":
                return R.drawable.dog;
            case "elephant":
                return R.drawable.elephant;
            case "frog":
                return R.drawable.frog;
            case "horse":
                return R.drawable.horse;
            case "leaf":
                return R.drawable.leaf;
            case "lion":
                return R.drawable.lion;
            case "napkin":
                return R.drawable.napkin;
            case "pan":
                return R.drawable.pan;
            case "pig":
                return R.drawable.pig;
            case "towel":
                return R.drawable.towel;
            case "cat_with_apple":
                return R.drawable.cat_with_apple;
            case "bat_animal":
                return R.drawable.bat_animal;
            case "bat_baseball":
                return R.drawable.bat_baseball;
            case "filler_bird":
                return R.drawable.filler_bird;
            case "cat_with_brush":
                return R.drawable.cat_with_brush;
            case "bat_distractor_animal":
                return R.drawable.bat_distractor_animal;
            case "filler_dog1":
                return R.drawable.filler_dog1;
            case "filler_dog2":
                return R.drawable.filler_dog2;
            case "lady_with_feather":
                return R.drawable.lady_with_feather;
            case "lady_with_brush":
                return R.drawable.lady_with_brush;
            case "bat_distractor_object":
                return R.drawable.bat_distractor_object;
            case "filler_mouse":
                return R.drawable.filler_mouse;
            default:
                return 0;
        }
    }
}
