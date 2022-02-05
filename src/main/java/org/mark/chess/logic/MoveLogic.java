package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MoveLogic {
    @Autowired
    private PieceLogicFactory pieceLogicFactory;

    @Autowired
    private KingLogic kingLogic;

    @Autowired
    private GridLogic gridLogic;

    public boolean isFrom(Field fieldClick) {
        return fieldClick.piece() != null;
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

    public void enableValidMoves(Game game, Field from) {
        game.grid().forEach(field -> field.button().setEnabled(false));
        pieceLogicFactory.getLogic(from.piece().pieceType())
                .getValidMoves(game.grid(), from, pieceLogicFactory)
                .forEach(to -> to.button().setEnabled(true));
    }

    public void resetValidMoves(Game game) {
        game.grid().forEach(field -> {
            field.button().setEnabled(field.piece() != null);
            if (field.piece() != null && field.piece().pieceType() == PieceType.PAWN) {
                ((Pawn) field.piece()).mayBeCapturedEnPassant(false);
            } else if (field.piece() != null && field.piece().pieceType() == PieceType.ROOK) {
                ((Rook) field.piece()).hasMovedAtLeastOnce(false);
            } else if (field.piece() != null && field.piece().pieceType() == PieceType.KING) {
                ((King) field.piece()).hasMovedAtLeastOnce(false);
            }
        });
    }

    public void setChessPieceSpecificFields(List<Field> grid, Field from, Field to) {
        if (from.piece().pieceType() == PieceType.PAWN) {
            ((Pawn) from.piece()).mayBeCapturedEnPassant(
                    ((PawnLogic) pieceLogicFactory.getLogic(PieceType.PAWN))
                            .mayBeCapturedEnPassant(grid, from, to));
        } else if (from.piece().pieceType() == PieceType.ROOK) {
            ((Rook) from.piece()).hasMovedAtLeastOnce(true);
        } else if (from.piece().pieceType() == PieceType.KING) {
            ((King) from.piece()).hasMovedAtLeastOnce(true);
        }
    }

    public void moveRookWhenCastling(List<Field> grid, Field from, Field to) {
        if (from.piece().pieceType() == PieceType.KING &&
                kingLogic.isValidCastling(grid, from, to, to.coordinates().x(), pieceLogicFactory, true)) {

            Coordinates rookCoordinates = new Coordinates((to.coordinates().x() == KingLogic.LEFT ?
                    KingLogic.ROOK_X_LEFT_FROM : KingLogic.ROOK_X_RIGHT_FROM), from.piece().color().getBaselineY());

            Field rookFromField = gridLogic.getField(grid, rookCoordinates);
            Field rookToField = gridLogic.getField(grid, rookCoordinates.x(
                    to.coordinates().x() == KingLogic.LEFT
                            ? KingLogic.ROOK_X_LEFT_TO
                            : KingLogic.ROOK_X_RIGHT_TO));

            moveRock(rookFromField, rookToField);
        }
    }

    private void moveRock(Field from, Field to) {
        Move rookMove = new Move();
        setFrom(rookMove, from);
        setTo(rookMove, to);
        resetFrom(rookMove);
    }
}
