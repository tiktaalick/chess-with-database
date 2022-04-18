package org.mark.chess.enums;

public enum Color {
    WHITE("white", new java.awt.Color(255, 255, 255), 1, 2),
    BLACK("black", new java.awt.Color(0, 0, 0), 8, 7),
    LIGHT("light", new java.awt.Color(250, 250, 200)),
    DARK("dark", new java.awt.Color(150, 100, 0));

    static {
        BLACK.opposite = WHITE;
        WHITE.opposite = BLACK;
    }

    private final String         name;
    private final java.awt.Color awtColor;
    private       int            baseline;
    private       int            baselinePawn;
    private       Color          opposite;

    Color(String name, java.awt.Color awtColor) {
        this.name = name;
        this.awtColor = awtColor;
    }

    Color(String name, java.awt.Color awtColor, int baseline, int baselinePawn) {
        this.name = name;
        this.awtColor = awtColor;
        this.baseline = baseline;
        this.baselinePawn = baselinePawn;
    }

    public String getName() {
        return name;
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

    public Color getOpposite() {
        return opposite;
    }
}
