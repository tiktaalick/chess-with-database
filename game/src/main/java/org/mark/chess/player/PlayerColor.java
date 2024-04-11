package org.mark.chess.player;

public enum PlayerColor {
    WHITE("white", 1, 2),
    BLACK("black", 8, 7);

    static {
        BLACK.setOpposite(WHITE);
        WHITE.setOpposite(BLACK);
    }

    private final String      name;
    private final int         baseline;
    private final int         baselinePawn;
    private       PlayerColor opposite;

    PlayerColor(String name, int baseline, int baselinePawn) {
        this.name = name;
        this.baseline = baseline;
        this.baselinePawn = baselinePawn;
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

    private void setOpposite(PlayerColor opposite) {
        this.opposite = opposite;
    }
}
