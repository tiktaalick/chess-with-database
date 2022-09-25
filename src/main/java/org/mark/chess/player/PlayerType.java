package org.mark.chess.player;

public enum PlayerType {
    HUMAN,
    COMPUTER;

    static {
        HUMAN.setOpposite(COMPUTER);
        COMPUTER.setOpposite(HUMAN);
    }

    private PlayerType opposite;

    PlayerType() { }

    public PlayerType getOpposite() {
        return opposite;
    }

    private PlayerType setOpposite(PlayerType opposite) {
        this.opposite = opposite;
        return this;
    }
}
