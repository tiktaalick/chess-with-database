package org.mark.chess.board.backgroundcolor;

public enum BackgroundColor {
    LIGHT(new java.awt.Color(250, 250, 200)),
    DARK(new java.awt.Color(150, 100, 0)),
    ATTACKING(new java.awt.Color(255, 150, 0)),
    CHECKMATE(new java.awt.Color(255, 0, 0)),
    STALEMATE(new java.awt.Color(100, 100, 100));

    private final java.awt.Color awtColor;

    BackgroundColor(java.awt.Color awtColor) {
        this.awtColor = awtColor;
    }

    public java.awt.Color getAwtColor() {
        return awtColor;
    }
}
