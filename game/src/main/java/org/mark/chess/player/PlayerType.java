package org.mark.chess.player;

import lombok.Getter;

@Getter
public enum PlayerType {
    HUMAN,
    COMPUTER;

    static {
        HUMAN.setOpposite(COMPUTER);
        COMPUTER.setOpposite(HUMAN);
    }

    private PlayerType opposite;

    PlayerType() { }

    private void setOpposite(PlayerType opposite) {
        this.opposite = opposite;
    }
}
