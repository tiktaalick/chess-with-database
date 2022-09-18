package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

import java.util.List;
import java.util.stream.Collectors;

import static org.mark.chess.enums.PieceType.PAWN;

public class PawnIsValidEnPassantMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidEnPassantMove(getGrid(), getFrom(), getTo());
    }

    private static List<Field> neighbourFieldsWithOpponentPawns(Grid grid, Field playerField, Color color) {
        return grid
                .getFields()
                .stream()
                .filter(opponentField -> (opponentField.getCoordinates().getX() - 1 == playerField.getCoordinates().getX() ||
                        opponentField.getCoordinates().getX() + 1 == playerField.getCoordinates().getX()) &&
                        opponentField.getCoordinates().getY() == playerField.getCoordinates().getY())
                .filter(opponentField -> opponentField.getPiece() != null && opponentField.getPiece().getColor() != color)
                .filter(opponentField -> opponentField.getPiece().getPieceType() == PAWN)
                .collect(Collectors.toList());
    }

    private boolean isValidEnPassantMove(Grid grid, Field from, Field to) {
        return neighbourFieldsWithOpponentPawns(grid, from, from.getPiece().getColor())
                .stream()
                .filter(field -> ((Pawn) field.getPiece()).isMayBeCapturedEnPassant())
                .filter(field -> field.getCoordinates().getX() == to.getCoordinates().getX())
                .anyMatch(field -> getAbsoluteVerticalMove(field, to) == 1);
    }
}
