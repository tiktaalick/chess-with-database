package org.mark.chess.player;

public enum PlayerColor {
    WHITE("white", new java.awt.Color(255, 255, 255), 1, 2),
    BLACK("black", new java.awt.Color(0, 0, 0), 8, 7);

    static {
        BLACK.setOpposite(WHITE);
        WHITE.setOpposite(BLACK);
    }

    private final java.awt.Color awtColor;
    private final String         name;
    private final int            baseline;
    private final int            baselinePawn;
    private       PlayerColor    opposite;

    PlayerColor(String name, java.awt.Color awtColor, int baseline, int baselinePawn) {
        this.name = name;
        this.awtColor = awtColor;
        this.baseline = baseline;
        this.baselinePawn = baselinePawn;
    }

    public java.awt.Color getAwtColor() {
        return awtColor;
    }

    public int getBaseline() {
        return baseline;
    }

    public int getBaselinePawn() {
        return baselinePawn;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getOpposite() {
        return opposite;
    }

    private PlayerColor setOpposite(PlayerColor opposite) {
        this.opposite = opposite;
        return this;
    }
}
