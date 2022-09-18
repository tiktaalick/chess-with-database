package org.mark.chess.model;

import lombok.Data;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class PieceTypeLogic {
    private final CheckLogic checkLogic;

    @Lazy
    public PieceTypeLogic(CheckLogic checkLogic) {
        this.checkLogic = checkLogic;
    }

    public List<Field> getValidMoves(Grid grid, Field from) {
        return grid
                .getFields()
                .stream()
                .filter(to -> from.getPiece().getPieceType().isValidMove(new IsValidMoveParameter(grid, from, to, checkLogic, false)))
                .collect(Collectors.toList());
    }
}
