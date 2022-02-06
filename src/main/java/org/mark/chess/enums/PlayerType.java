package org.mark.chess.enums;

public enum PlayerType {
    HUMAN,
    COMPUTER;

    static {
        HUMAN.opposite = COMPUTER;
        COMPUTER.opposite = HUMAN;
    }

    private PlayerType opposite;

    public PlayerType getOpposite() {
        return opposite;
    }
}
