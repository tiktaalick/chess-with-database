package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.PlayerType;

@Data
@Accessors(fluent = true)
public class Human extends Player {
    private static final PlayerType playerType = PlayerType.HUMAN;
}
