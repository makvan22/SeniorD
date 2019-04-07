package leadgames.cis400.leadgames;

public enum PUT_LOCATION {
    UPPER_LEFT(1), UPPER_RIGHT(2), LOWER_LEFT(3), LOWER_RIGHT(4);

    private int quad;

    PUT_LOCATION(int quad) {
        this.quad = quad;
    }

    public int getQuad() {
        return quad;
    }

}
