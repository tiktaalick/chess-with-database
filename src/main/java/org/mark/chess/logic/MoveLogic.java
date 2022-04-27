package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.BackgroundColorFactory;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveLogic {
    @Autowired
    private BackgroundColorFactory backgroundColorFactory;

    @Autowired
    private ButtonLogic buttonLogic;

    @Autowired
    private FieldLogic fieldLogic;

    @Autowired
    private GridLogic gridLogic;

    @Autowired
    private KingLogic kingLogic;

    @Autowired
    private PieceFactory pieceFactory;

    @Autowired
    private PieceLogicFactory pieceLogicFactory;

    public void changeTurn(Game game) {
        game.setCurrentPlayerId(1 - game.getCurrentPlayerId());
    }

    public void enableValidMoves(Game game, Field from) {
        game.getGrid().forEach(field -> field.setValidMove(false));

        List<Field> validMoves = getValidMoves(game, from);
        validMoves.forEach(to -> {
            to.setValidMove(true);
            to.getButton().setBackground(backgroundColorFactory.getBackgroundColor(to));
        });
    }

    public boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.getPiece() != null && fieldClick.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerId()).getColor();
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

    public void resetFrom(Move move) {
        move.getFrom().setPiece(null);
        move.getFrom().getButton().setText(move.getFrom().getCode());
        move.getFrom().getButton().setIcon(null);
    }

    public void resetValidMoves(Game game, Move move) {
        List<Field> allValidMoves = new ArrayList<>();
        game.getGrid().forEach(field -> {
            field.setAttacking(false);
            field.setValidFrom(false);
            List<Field> validMoves = getValidMoves(game, field);
            field.setValidMove(!validMoves.isEmpty());
            allValidMoves.addAll(validMoves);

            if (duringAMove(move, field)) {
                return;
            }

            if (field.getPiece() != null && field.getPiece().getPieceType() == PieceType.PAWN) {
                ((Pawn) field.getPiece()).setMayBeCapturedEnPassant(false);
            }
        });

        game
                .getGrid()
                .stream()
                .filter(field -> field.getPiece() != null)
                .filter(field -> field.getPiece().getPieceType() == PieceType.KING)
                .forEach(field -> {
                    field
                            .setCheckMate(kingLogic.isInCheckNow(game.getGrid(), field, field, pieceLogicFactory, false) && allValidMoves.isEmpty())
                            .getButton()
                            .setBackground(backgroundColorFactory.getBackgroundColor(field));
                });
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

    public void setFrom(Move move, Field from) {
        move.setPiece(from.getPiece());
        move.setFrom(from);
        move.setTo(null);

        from.setValidFrom(true);
    }

    public void setTo(Move move, Field to) {
        move.setTo(to);
        move.getTo().setPiece(move.getFrom().getPiece());
        move.getTo().getButton().setText(null);
        move.getTo().getButton().setIcon(move.getFrom().getButton().getIcon());
    }

    private boolean duringAMove(Move move, Field field) {
        return move.getFrom() != null &&
               move.getTo() != null &&
               Arrays.asList(move.getFrom().getCode(), move.getTo().getCode()).contains(field.getCode());
    }

    private List<Field> getValidMoves(Game game, Field from) {
        return fieldLogic.isActivePlayerField(game, from)
                ? pieceLogicFactory.getLogic(from.getPiece().getPieceType()).getValidMoves(game.getGrid(), from, pieceLogicFactory)
                : new ArrayList<>();
    }

    private void moveRock(Field from, Field to) {
        Move rookMove = new Move();
        setFrom(rookMove, from);
        setTo(rookMove, to);
        resetFrom(rookMove);
    }
}
