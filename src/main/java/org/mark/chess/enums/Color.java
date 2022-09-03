package org.mark.chess.enums;

public enum Color {
    WHITE("white", new java.awt.Color(255, 255, 255), 1, 2),
    BLACK("black", new java.awt.Color(0, 0, 0), 8, 7),
    LIGHT(new java.awt.Color(250, 250, 200)),
    DARK(new java.awt.Color(150, 100, 0)),
    ATTACKING(new java.awt.Color(255, 150, 0)),
    CHECKMATE(new java.awt.Color(255, 0, 0)),
    STALEMATE(new java.awt.Color(100, 100, 100));

    static {
        BLACK.setOpposite(WHITE);
        WHITE.setOpposite(BLACK);
    }

    private final java.awt.Color awtColor;
    private       String         name;
    private       int            baseline;
    private       int            baselinePawn;
    private       Color          opposite;

    Color(java.awt.Color awtColor) {
        this.awtColor = awtColor;
    }

    Color(String name, java.awt.Color awtColor, int baseline, int baselinePawn) {
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

    public Color getOpposite() {
        return opposite;
    }

    private Color setOpposite(Color opposite) {
        this.opposite = opposite;
        return this;
    }
}
