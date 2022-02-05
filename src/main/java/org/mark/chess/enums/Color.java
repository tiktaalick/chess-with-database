package org.mark.chess.enums;

public enum Color {
    BLACK("black", new java.awt.Color(0, 0, 0), 0),
    WHITE("white", new java.awt.Color(255, 255, 255), 7),
    DARK("dark", new java.awt.Color(150, 100, 0), 0),
    LIGHT("light", new java.awt.Color(250, 250, 200), 0);

    private final String name;
    private final java.awt.Color awtColor;
    private int baselineY;

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
}
