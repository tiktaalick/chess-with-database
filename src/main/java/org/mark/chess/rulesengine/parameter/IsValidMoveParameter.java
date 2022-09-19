package org.mark.chess.rulesengine.parameter;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;

@Data
@Accessors(chain = true)
public class IsValidMoveParameter {
    private final Grid    grid;
    private final Field   from;
    private final Field   to;
    private final boolean isOpponent;
}
