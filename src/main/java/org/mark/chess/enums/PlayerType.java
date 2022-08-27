package org.mark.chess.enums;

public enum PlayerType {
    HUMAN,
    COMPUTER;

    static {
        HUMAN.setOpposite(COMPUTER);
        COMPUTER.setOpposite(HUMAN);
    }

    private PlayerType opposite;

    public PlayerType getOpposite() {
        return opposite;
    }

    private PlayerType setOpposite(PlayerType opposite) {
        this.opposite = opposite;
        return this;
    }
}
