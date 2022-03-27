package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.King;
import org.mark.chess.model.Move;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Rook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class MoveLogic {
    @Autowired
    private PieceLogicFactory pieceLogicFactory;

    @Autowired
    private PieceFactory pieceFactory;

    @Autowired
    private KingLogic kingLogic;

    @Autowired
    private GridLogic gridLogic;

    @Autowired
    private FieldLogic fieldLogic;

    public boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.piece() != null && fieldClick.piece().color() == game.players().get(game.currentPlayerIndex()).color();
    }

    public void enableValidMoves(Game game, Field from) {
        game.grid().forEach(field -> field.button().setEnabled(false));
        pieceLogicFactory
                .getLogic(from.piece().pieceType())
                .getValidMoves(game.grid(), from, pieceLogicFactory)
                .forEach(to -> to.button().setEnabled(true));
    }

    public void resetValidMoves(Game game, Move move) {
        game.grid().forEach(field -> {
            field.button().setEnabled(fieldLogic.setEnabledButton(game, field));

            if (duringAMove(move, field)) {
                return;
            }

            if (field.piece() != null && field.piece().pieceType() == PieceType.PAWN) {
                ((Pawn) field.piece()).mayBeCapturedEnPassant(false);
            } else if (field.piece() != null && field.piece().pieceType() == PieceType.ROOK) {
                ((Rook) field.piece()).hasMovedAtLeastOnce(false);
            } else if (field.piece() != null && field.piece().pieceType() == PieceType.KING) {
                ((King) field.piece()).hasMovedAtLeastOnce(false);
            }
        });
    }

    private boolean duringAMove(Move move, Field field) {
        return move.from() != null && move.to() != null && Arrays.asList(move.from().id(), move.to().id()).contains(field.id());
    }

    public void setChessPieceSpecificFields(Game game, Field from, Field to) {
        if (from.piece().pieceType() == PieceType.PAWN) {
            Pawn pawn = (Pawn) from.piece();
            PawnLogic pawnLogic = (PawnLogic) pieceLogicFactory.getLogic(PieceType.PAWN);
            pawn.mayBeCapturedEnPassant(pawnLogic.mayBeCapturedEnPassant(game.grid(), from, to));
            pawn.isPawnBeingPromoted(pawnLogic.isPawnBeingPromoted(from, to));

            if (pawn.isPawnBeingPromoted()) {
                gridLogic.addChessPiece(game, to.id(), pieceFactory.getPiece(from.piece().pieceType().getNextPawnPromotion()), from.piece().color());
            }
        } else if (from.piece().pieceType() == PieceType.ROOK) {
            ((Rook) from.piece()).hasMovedAtLeastOnce(true);
        } else if (from.piece().pieceType() == PieceType.KING) {
            ((King) from.piece()).hasMovedAtLeastOnce(true);
        }
    }

    public void moveRookWhenCastling(List<Field> grid, Field from, Field to) {
        if (from.piece().pieceType() == PieceType.KING &&
            kingLogic.isValidCastling(grid, from, to, to.coordinates().x(), pieceLogicFactory, false, true)) {

            Coordinates rookCoordinates = new Coordinates(
                    (to.coordinates().x() == KingLogic.LEFT ? KingLogic.ROOK_X_LEFT_FROM : KingLogic.ROOK_X_RIGHT_FROM),
                    from.piece().color().getBaselineY());

            Field rookFromField = gridLogic.getField(grid, rookCoordinates);
            Field rookToField = gridLogic.getField(grid,
                    rookCoordinates.x(to.coordinates().x() == KingLogic.LEFT ? KingLogic.ROOK_X_LEFT_TO : KingLogic.ROOK_X_RIGHT_TO));

            moveRock(rookFromField, rookToField);
        }
    }

    private void moveRock(Field from, Field to) {
        Move rookMove = new Move();
        setFrom(rookMove, from);
        setTo(rookMove, to);
        resetFrom(rookMove);
    }

    public void setFrom(Move move, Field from) {
        move.piece(from.piece());
        move.from(from);
        move.to(null);
    }

    public void setTo(Move move, Field to) {
        move.to(to);
        move.to().piece(move.from().piece());
        move.to().button().setText(null);
        move.to().button().setIcon(move.from().button().getIcon());
    }

    public void resetFrom(Move move) {
        move.from().piece(null);
        move.from().button().setIcon(null);
    }

    public void changeTurn(Game game) {
        game.currentPlayerIndex(1 - game.currentPlayerIndex());
    }
}
