package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;

@Data
@Accessors(fluent = true)
public abstract class Piece {
    private PieceType pieceType;
    private Color color;
    private boolean kickedOff;
}
