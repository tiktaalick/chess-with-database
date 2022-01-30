package org.mark.chess.enums;

public enum Color {
    BLACK("black", new java.awt.Color(0, 0, 0)),
    WHITE("white", new java.awt.Color(255, 255, 255)),
    DARK("dark", new java.awt.Color(150, 100, 0)),
    LIGHT("light", new java.awt.Color(250, 250, 200));

    private final String name;
    private final java.awt.Color awtColor;

    Color(String name, java.awt.Color awtColor) {
        this.name = name;
        this.awtColor = awtColor;
    }

    public String getName() {
        return name;
    }

    public java.awt.Color getAwtColor() {
        return awtColor;
    }
}
