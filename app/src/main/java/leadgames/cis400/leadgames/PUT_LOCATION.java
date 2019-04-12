package leadgames.cis400.leadgames;

public enum PUT_LOCATION {
    UPPER_LEFT(1, "Upper Left"), UPPER_RIGHT(2, "Upper Right"),
    LOWER_LEFT(3, "Lower Left"), LOWER_RIGHT(4, "Lower Right");

    private int quad;
    private String stringLocation;

    PUT_LOCATION(int quad, String stringLocation) {
        this.quad = quad;
        this.stringLocation = stringLocation;
    }

    public int getQuad() {
        return quad;
    }

    @Override
    public String toString() { return stringLocation; }

}
