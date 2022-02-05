package org.mark.chess.enums;

public enum Color {
    BLACK("black", new java.awt.Color(0, 0, 0), 0, 7),
    WHITE("white", new java.awt.Color(255, 255, 255), 7, 0),
    DARK("dark", new java.awt.Color(150, 100, 0)),
    LIGHT("light", new java.awt.Color(250, 250, 200));

    private final String name;
    private final java.awt.Color awtColor;
    private int baselineY;
    private int oppositeBaselineY;

    Color(String name, java.awt.Color awtColor) {
        this.name = name;
        this.awtColor = awtColor;
    }

    Color(String name, java.awt.Color awtColor, int baselineY, int oppositeBaselineY) {
        this.name = name;
        this.awtColor = awtColor;
        this.baselineY = baselineY;
        this.oppositeBaselineY = oppositeBaselineY;
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

    public int getOppositeBaselineY() {
        return oppositeBaselineY;
    }
}
