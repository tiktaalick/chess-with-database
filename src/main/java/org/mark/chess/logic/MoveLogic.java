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

    @Autowired
    private ButtonLogic buttonLogic;

    public boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.getPiece() != null && fieldClick.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerId()).getColor();
    }

    public void enableValidMoves(Game game, Field from) {
        game.getGrid().forEach(field -> field.getButton().setEnabled(false));
        pieceLogicFactory
                .getLogic(from.getPiece().getPieceType())
                .getValidMoves(game.getGrid(), from, pieceLogicFactory)
                .forEach(to -> to.getButton().setEnabled(true));
    }

    public void resetValidMoves(Game game, Move move) {
        game.getGrid().forEach(field -> {
            field.getButton().setEnabled(buttonLogic.setEnabledButton(game, field));

            if (duringAMove(move, field)) {
                return;
            }

            if (field.getPiece() != null && field.getPiece().getPieceType() == PieceType.PAWN) {
                ((Pawn) field.getPiece()).setMayBeCapturedEnPassant(false);
            } else if (field.getPiece() != null && field.getPiece().getPieceType() == PieceType.ROOK) {
                ((Rook) field.getPiece()).setHasMovedAtLeastOnce(false);
            } else if (field.getPiece() != null && field.getPiece().getPieceType() == PieceType.KING) {
                ((King) field.getPiece()).setHasMovedAtLeastOnce(false);
            }
        });
    }

    private boolean duringAMove(Move move, Field field) {
        return move.getFrom() != null &&
               move.getTo() != null &&
               Arrays.asList(move.getFrom().getCode(), move.getTo().getCode()).contains(field.getCode());
    }

    public void setChessPieceSpecificFields(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PieceType.PAWN) {
            Pawn pawn = (Pawn) from.getPiece();
            PawnLogic pawnLogic = (PawnLogic) pieceLogicFactory.getLogic(PieceType.PAWN);
            pawn.setMayBeCapturedEnPassant(pawnLogic.mayBeCapturedEnPassant(game.getGrid(), from, to));
            pawn.setPawnBeingPromoted(pawnLogic.isPawnBeingPromoted(from, to));

            if (pawn.isPawnBeingPromoted()) {
                fieldLogic.addChessPiece(game,
                        to,
                        pieceFactory.getPiece(from.getPiece().getPieceType().getNextPawnPromotion()).setColor(from.getPiece().getColor()));
            }
        } else if (from.getPiece().getPieceType() == PieceType.ROOK) {
            ((Rook) from.getPiece()).setHasMovedAtLeastOnce(true);
        } else if (from.getPiece().getPieceType() == PieceType.KING) {
            ((King) from.getPiece()).setHasMovedAtLeastOnce(true);
        }
    }

    public void moveRookWhenCastling(List<Field> grid, Field from, Field to) {
        if (from.getPiece().getPieceType() == PieceType.KING &&
            kingLogic.isValidCastling(grid, from, to, to.getCoordinates().getX(), pieceLogicFactory, false, true)) {

            Coordinates rookCoordinates = new Coordinates((to.getCoordinates().getX() == KingLogic.KING_X_LEFT
                    ? KingLogic.ROOK_X_LEFT_FROM
                    : KingLogic.ROOK_X_RIGHT_FROM), from.getPiece().getColor().getBaseline());

            Field rookFromField = gridLogic.getField(grid, rookCoordinates);
            Field rookToField = gridLogic.getField(grid,
                    rookCoordinates.setX(to.getCoordinates().getX() == KingLogic.KING_X_LEFT
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

    public void setFrom(Move move, Field from) {
        move.setPiece(from.getPiece());
        move.setFrom(from);
        move.setTo(null);
    }

    public void setTo(Move move, Field to) {
        move.setTo(to);
        move.getTo().setPiece(move.getFrom().getPiece());
        move.getTo().getButton().setText(null);
        move.getTo().getButton().setIcon(move.getFrom().getButton().getIcon());
    }

    public void resetFrom(Move move) {
        move.getFrom().setPiece(null);
        move.getFrom().getButton().setText(move.getFrom().getCode());
        move.getFrom().getButton().setIcon(null);
    }

    public void changeTurn(Game game) {
        game.setCurrentPlayerId(1 - game.getCurrentPlayerId());
    }
}
