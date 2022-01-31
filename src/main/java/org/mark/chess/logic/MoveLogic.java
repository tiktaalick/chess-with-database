package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.*;
import org.springframework.beans.factory.annotation.Autowired;

public class MoveLogic {
    @Autowired
    private PieceLogicFactory pieceLogicFactory;

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

    public void setChessPieceSpecificFields(Game game, Move move, Field to) {
        if (move.from().piece().pieceType() == PieceType.PAWN) {
            ((Pawn) move.from().piece()).mayBeCapturedEnPassant(
                    ((PawnLogic) pieceLogicFactory.getLogic(PieceType.PAWN))
                            .mayBeCapturedEnPassant(game.grid(), move.from(), to));
        } else if (move.from().piece().pieceType() == PieceType.ROOK) {
            ((Rook) move.from().piece()).hasMovedAtLeastOnce(true);
        } else if (move.from().piece().pieceType() == PieceType.KING) {
            ((King) move.from().piece()).hasMovedAtLeastOnce(true);
        }
    }
}
