package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.PlayerType;

@Data
@Accessors(fluent = true)
public class Computer extends Player {
    private PlayerType playerType = PlayerType.COMPUTER;
}
