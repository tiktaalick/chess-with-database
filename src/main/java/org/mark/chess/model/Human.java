package org.mark.chess.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.enums.PlayerType;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Human extends Player {
    private PlayerType playerType = PlayerType.HUMAN;
}
