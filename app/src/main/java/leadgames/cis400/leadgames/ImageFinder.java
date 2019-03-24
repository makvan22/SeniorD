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
            default:
                return 0;
        }
    }
}
