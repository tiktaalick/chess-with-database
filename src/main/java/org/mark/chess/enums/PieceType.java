package org.mark.chess.enums;

public enum PieceType {
    KING("king"),
    PAWN("pawn"),
    QUEEN("queen"),
    ROOK("rook"),
    BISHOP("bishop"),
    KNIGHT("knight");

    private final String name;

    PieceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PieceType getNextPawnPromotion() {
        return this != KNIGHT ? PieceType.values()[ordinal() + 1] : QUEEN;
    }
}
