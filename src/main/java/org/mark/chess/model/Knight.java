package org.mark.chess.model;

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
public class Knight extends Piece {
    public Knight(Color color) {
        super(PieceType.KNIGHT, color);
    }
}
