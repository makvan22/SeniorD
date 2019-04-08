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
            case "apple_scene":
                return R.drawable.apple_scene;
            case "bat_scene_1":
                return R.drawable.bat_scene_1;
            case "bat_scene_2":
                return R.drawable.bat_scene_2;
            case "bird_scene":
                return R.drawable.bird_scene;
            case "cat_brush_scene":
                return R.drawable.cat_brush_scene;
            case "cat_fence_scene":
                return R.drawable.cat_fence_scene;
            case "dog_scene_1":
                return R.drawable.dog_scene_1;
            case "dog_scene_2":
                return R.drawable.dog_scene_2;
            case "feather_scene":
                return R.drawable.feather_scene;
            case "girl_brush_scene":
                return R.drawable.girl_brush_scene;
            case "house_scene":
                return R.drawable.house_scene;
            case "mouse_scene":
                return R.drawable.mouse_scene;
            default:
                return 0;
        }
    }
}
