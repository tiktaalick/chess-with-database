package org.mark.chess.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A human player.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Human extends Player {

    /**
     * Constructor for creating a human player based on the player color.
     *
     * @param color The player color.
     */
    public Human(PlayerColor color) {
        super(color, PlayerType.HUMAN);
    }
}
