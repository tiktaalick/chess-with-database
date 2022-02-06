package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Move {
    private Piece piece;
    private Field from;
    private Field to;
}
