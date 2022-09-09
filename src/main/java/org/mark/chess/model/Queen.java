package org.mark.chess.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;

import static org.mark.chess.enums.PieceType.QUEEN;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Queen extends Piece {

    public Queen(Color color) {
        super(QUEEN, color);
    }
}
