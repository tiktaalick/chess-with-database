package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PlayerType;

@Data
@Accessors(fluent = true)
public abstract class Player {
    private Color color;
    private PlayerType playerType;
}
