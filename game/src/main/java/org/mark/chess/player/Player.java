package org.mark.chess.player;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Player {

    private PlayerColor color;
    private PlayerType  playerType;
}
