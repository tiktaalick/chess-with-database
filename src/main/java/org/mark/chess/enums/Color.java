package org.mark.chess.enums;

public enum Color {
    WHITE("white", new java.awt.Color(255, 255, 255), 7),
    BLACK("black", new java.awt.Color(0, 0, 0), 0),
    LIGHT("light", new java.awt.Color(250, 250, 200)),
    DARK("dark", new java.awt.Color(150, 100, 0));

    static {
        BLACK.opposite = WHITE;
        WHITE.opposite = BLACK;
    }

    private final String         name;
    private final java.awt.Color awtColor;
    private       int            baselineY;
    private       Color          opposite;

    Color(String name, java.awt.Color awtColor) {
        this.name = name;
        this.awtColor = awtColor;
    }

    Color(String name, java.awt.Color awtColor, int baselineY) {
        this.name = name;
        this.awtColor = awtColor;
        this.baselineY = baselineY;
    }

    public String getName() {
        return name;
    }

    public java.awt.Color getAwtColor() {
        return awtColor;
    }

    public int getBaselineY() {
        return baselineY;
    }

    public Color getOpposite() {
        return opposite;
    }
}
