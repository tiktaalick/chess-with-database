package org.mark.chess.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.enums.PieceType;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class King extends Piece {
    private PieceType pieceType = PieceType.KING;
    private boolean   hasMovedAtLeastOnce;
}
