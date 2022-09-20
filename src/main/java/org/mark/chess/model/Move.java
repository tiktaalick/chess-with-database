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

    public Move(Field from) {
        this.piece = from.getPiece();
        this.from = from;
    }

    public Move changeTurn(Game game) {
        game.setCurrentPlayerColor(game.getCurrentPlayerColor().getOpposite());
        return this;
    }

    public boolean duringAMove(Field field) {
        return from != null && to != null && Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode());
    }

    public void enableValidMoves(Game game, Field from) {
        game.getGrid().getFields().forEach(field -> field.setValidFrom(false).setValidMove(false).setAttacking(false).setUnderAttack(false));

        List<Field> validMoves = game.getValidMoves(from);
        validMoves.forEach((Field validMove) -> {
            from.setValidFrom(true);
            validMove.setValidMove(true);
        });
        game.setValidMoveColors(game.getGrid(), from, validMoves, validMoves);
    }

    public boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.getPiece() != null &&
                fieldClick.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerColor().ordinal()).getColor();
    }

    public Move moveRookWhenCastling(Game game, Field from, Field to) {
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

            moveRook(game.getGrid(), rookFromField, rookToField);
        }

        return this;
    }

    public Move resetField(Field field) {
        field.setPiece(null);
        field.getButton().setText(field.getCode());
        field.getButton().setIcon(null);

        return this;
    }

    public Move setChessPieceSpecificFields(Game game, Field from, Field to) {
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
        return this;
    }

    public Move setFrom(Field from) {
        this.piece = from.getPiece();
        this.from = from;
        this.to = null;

        from.setValidFrom(true);

        return this;
    }

    public Move setTo(Grid grid, Field to) {
        if (isCaptureEnPassant(this, to)) {
            captureEnPassant(grid, from, to);
        }

        setTo(to.setPiece(from.getPiece()));
        to.getButton().setText(null);
        to.getButton().setIcon(from.getButton().getIcon());

        return this;
    }

    private static boolean isCaptureEnPassant(Move move, Field to) {
        return move.getFrom().getPiece().getPieceType() == PAWN &&
                move.getFrom().getCoordinates().getX() != to.getCoordinates().getX() &&
                to.getPiece() == null;
    }

    private void captureEnPassant(Grid grid, Field from, Field to) {
        resetField(grid.getField(new Coordinates(to.getCoordinates().getX(), from.getCoordinates().getY())));
    }

    private void moveRook(Grid grid, Field from, Field to) {
        var rookMove = new Move(from).setTo(grid, to);
        resetField(rookMove.getFrom());
    }
}
