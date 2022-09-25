package org.mark.chess.player;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Computer extends Player {

    private PlayerType playerType = PlayerType.COMPUTER;
}
