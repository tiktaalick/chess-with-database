package org.mark.chess.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Human extends Player {

    private PlayerType playerType = PlayerType.HUMAN;
}
