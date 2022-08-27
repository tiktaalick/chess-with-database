package org.mark.chess.enums;

public enum PieceType {
    KING("king", 0),
    PAWN("pawn", 1),
    QUEEN("queen", 9),
    ROOK("rook", 5),
    BISHOP("bishop", 3),
    KNIGHT("knight", 3);

    private final String name;
    private final int    value;

    PieceType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public PieceType getNextPawnPromotion() {
        return this == KNIGHT
                ? QUEEN
                : values()[ordinal() + 1];
    }

    public int getValue() {
        return value;
    }
}
