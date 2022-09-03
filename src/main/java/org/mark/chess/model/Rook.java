package org.mark.chess.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Rook extends Piece {
    private boolean   hasMovedAtLeastOnce;

    public Rook(Color color) {
        super(PieceType.ROOK, color);
    }
}
