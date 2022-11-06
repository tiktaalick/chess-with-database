package org.mark.chess.player;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * An artificial intelligent chess game player.
 */
@Getter
@Setter
@Accessors(chain = true)
public class Computer extends Player {

    /**
     * Constructor for creating a computer player based on the player color.
     *
     * @param color The player color.
     */
    public Computer(PlayerColor color) {
        super(color, PlayerType.COMPUTER);
    }
}
