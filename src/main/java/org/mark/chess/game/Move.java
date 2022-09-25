package org.mark.chess.game;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.PieceType;
import org.mark.chess.piece.isvalidmove.KingIsValidCastlingRule;

import java.util.Arrays;
import java.util.List;

import static org.mark.chess.piece.PieceType.KING;
import static org.mark.chess.piece.PieceType.PAWN;

@Data
@Accessors(chain = true)
public class Move {

    private PieceType pieceType;
    private Field     from;
    private Field     to;
    private Move      rookMove;

    public Move(Field from) {
        this.pieceType = from.getPieceType();
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

        List<Field> validMoves = game.createValidMoves(from);
        validMoves.forEach((Field validMove) -> {
            from.setValidFrom(true);
            validMove.setValidMove(true);
        });
        game.setValidMoveColors(game.getGrid(), from, validMoves, validMoves);
    }

    public boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.getPieceType() != null &&
                fieldClick.getPieceType().getColor() == game.getPlayers().get(game.getCurrentPlayerColor().ordinal()).getColor();
    }

    public Move moveRookWhenCastling(Game game, Field from, Field to) {
        if (from.getPieceType().getName().equals(KING) &&
                KingIsValidCastlingRule.isValidCastling(game.getGrid(), from, to, to.getCoordinates().getX(), false, true)) {

            var rookCoordinates = new Coordinates((to.getCoordinates().getX() == KingIsValidCastlingRule.KING_CASTLING_TO_THE_LEFT
                    ? KingIsValidCastlingRule.ROOK_CASTLING_FROM_THE_LEFT
                    : KingIsValidCastlingRule.ROOK_CASTLING_FROM_THE_RIGHT), from.getPieceType().getColor().getBaseline());

            var rookFromField = game.getGrid().getField(rookCoordinates);
            var rookToField = game
                    .getGrid()
                    .getField(rookCoordinates.setX(to.getCoordinates().getX() == KingIsValidCastlingRule.KING_CASTLING_TO_THE_LEFT
                            ? KingIsValidCastlingRule.ROOK_CASTLING_TO_THE_RIGHT
                            : KingIsValidCastlingRule.ROOK_CASTLING_TO_THE_LEFT));

            moveRook(game.getGrid(), rookFromField, rookToField);
        }

        return this;
    }

    public void resetField(Field field) {
        field.setPieceType(null);
        field.getButton().setText(field.getCode());
        field.getButton().setIcon(null);
    }

    public Move setFrom(Field from) {
        this.pieceType = from.getPieceType();
        this.from = from;
        this.to = null;

        from.setValidFrom(true);

        return this;
    }

    public Move setPieceTypeSpecificFields(Game game, Field from, Field to) {
        from.getPieceType().setPieceTypeSpecificFields(game, from, to);

        return this;
    }

    public Move setTo(Grid grid, Field to) {
        if (isCaptureEnPassant(this, to)) {
            captureEnPassant(grid, from, to);
        }

        setTo(to.setPieceType(from.getPieceType()));
        to.getButton().setText(null);
        to.getButton().setIcon(from.getButton().getIcon());

        return this;
    }

    private static boolean isCaptureEnPassant(Move move, Field to) {
        return move.getFrom().getPieceType().getName().equals(PAWN) &&
                move.getFrom().getCoordinates().getX() != to.getCoordinates().getX() &&
                to.getPieceType() == null;
    }

    private void captureEnPassant(Grid grid, Field from, Field to) {
        resetField(grid.getField(new Coordinates(to.getCoordinates().getX(), from.getCoordinates().getY())));
    }

    private void moveRook(Grid grid, Field from, Field to) {
        this.rookMove = new Move(from).setTo(grid, to);
        resetField(rookMove.getFrom());
    }
}
