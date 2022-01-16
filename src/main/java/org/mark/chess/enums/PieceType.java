package org.mark.chess.enums;

public enum PieceType {
    KING("king"),
    QUEEN("queen"),
    ROOK("rook"),
    BISHOP("bishop"),
    KNIGHT("knight"),
    PAWN("pawn");

    private final String name;

    PieceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
