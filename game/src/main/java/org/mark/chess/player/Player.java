package org.mark.chess.player;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Player {

    private final PlayerColor color;
    private final PlayerType  playerType;
}
