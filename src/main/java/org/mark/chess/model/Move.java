package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.PieceType;
import org.mark.chess.rulesengine.rule.isvalidmove.KingIsValidCastlingRule;

import java.util.Arrays;
import java.util.List;

import static org.mark.chess.enums.PieceType.PAWN;

@Data
@Accessors(chain = true)
public class Move {
    private Piece piece;
    private Field from;
    private Field to;

    public void changeTurn(Game game) {
        game.setCurrentPlayerColor(game.getCurrentPlayerColor().getOpposite());
    }

    public void enableValidMoves(Game game, Field from) {
        game.getGrid().getFields().forEach(field -> field.setValidFrom(false).setValidMove(false).setAttacking(false).setUnderAttack(false));

        List<Field> validMoves = game.getValidMoves(from);
        validMoves.forEach((Field to) -> {
            from.setValidFrom(true);
            to.setValidMove(true);
        });
        game.setValidMoveColors(game.getGrid(), from, validMoves, validMoves);
    }

    public boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.getPiece() != null &&
                fieldClick.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerColor().ordinal()).getColor();
    }

    public void moveRookWhenCastling(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PieceType.KING &&
                KingIsValidCastlingRule.isValidCastling(game.getGrid(), from, to, to.getCoordinates().getX(), false, true)) {

            var rookCoordinates = new Coordinates((to.getCoordinates().getX() == KingIsValidCastlingRule.KING_X_LEFT
                    ? KingIsValidCastlingRule.ROOK_X_LEFT_FROM
                    : KingIsValidCastlingRule.ROOK_X_RIGHT_FROM), from.getPiece().getColor().getBaseline());

            var rookFromField = game.getGrid().getField(rookCoordinates);
            var rookToField = game
                    .getGrid()
                    .getField(rookCoordinates.setX(to.getCoordinates().getX() == KingIsValidCastlingRule.KING_X_LEFT
                            ? KingIsValidCastlingRule.ROOK_X_LEFT_TO
                            : KingIsValidCastlingRule.ROOK_X_RIGHT_TO));

            moveRock(game.getGrid(), rookFromField, rookToField);
        }
    }

    public void resetField(Field field) {
        field.setPiece(null);
        field.getButton().setText(field.getCode());
        field.getButton().setIcon(null);
    }

    public void setChessPieceSpecificFields(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PAWN) {
            ((Pawn) from.getPiece()).setMayBeCapturedEnPassant(game.getGrid(), from, to).setPawnBeingPromoted(from, to);

            if ((from.getPiece()).isPawnBeingPromoted()) {
                to.addChessPiece(from.getPiece().getPieceType().getNextPawnPromotion().createPiece(from.getPiece().getColor()));
            }
        } else if (from.getPiece().getPieceType() == PieceType.ROOK) {
            ((Rook) from.getPiece()).setHasMovedAtLeastOnce(true);
        } else if (from.getPiece().getPieceType() == PieceType.KING) {
            ((King) from.getPiece()).setHasMovedAtLeastOnce(true);
        }
    }

    public void setFrom(Move move, Field from) {
        move.setPiece(from.getPiece());
        move.setFrom(from);
        move.setTo(null);

        from.setValidFrom(true);
    }

    public void setTo(Grid grid, Move move, Field to) {
        if (isCaptureEnPassant(move, to)) {
            captureEnPassant(grid, move.getFrom(), to);
        }
        move.setTo(to);
        move.getTo().setPiece(move.getFrom().getPiece());
        move.getTo().getButton().setText(null);
        move.getTo().getButton().setIcon(move.getFrom().getButton().getIcon());
    }

    boolean duringAMove(Field field) {
        return this.getFrom() != null &&
                this.getTo() != null &&
                Arrays.asList(this.getFrom().getCode(), this.getTo().getCode()).contains(field.getCode());
    }

    private static boolean isCaptureEnPassant(Move move, Field to) {
        return move.getFrom().getPiece().getPieceType() == PAWN &&
                move.getFrom().getCoordinates().getX() != to.getCoordinates().getX() &&
                to.getPiece() == null;
    }

    private void captureEnPassant(Grid grid, Field from, Field to) {
        resetField(grid.getField(new Coordinates(to.getCoordinates().getX(), from.getCoordinates().getY())));
    }

    private void moveRock(Grid grid, Field from, Field to) {
        var rookMove = new Move();
        setFrom(rookMove, from);
        setTo(grid, rookMove, to);
        resetField(rookMove.getFrom());
    }
}
