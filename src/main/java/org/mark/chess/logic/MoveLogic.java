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

import static org.mark.chess.enums.PieceType.PAWN;

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
        game.setCurrentPlayerColor(game.getCurrentPlayerColor().getOpposite());
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
        return fieldClick.getPiece() != null &&
               fieldClick.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerColor().ordinal()).getColor();
    }

    public void moveRookWhenCastling(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PieceType.KING &&
            kingLogic.isValidCastling(game.getGrid(), from, to, to.getCoordinates().getX(), pieceLogicFactory, false, true)) {

            Coordinates rookCoordinates = new Coordinates((to.getCoordinates().getX() == KingLogic.KING_X_LEFT
                    ? KingLogic.ROOK_X_LEFT_FROM
                    : KingLogic.ROOK_X_RIGHT_FROM), from.getPiece().getColor().getBaseline());

            Field rookFromField = gridLogic.getField(game.getGrid(), rookCoordinates);
            Field rookToField = gridLogic.getField(game.getGrid(),
                    rookCoordinates.setX(to.getCoordinates().getX() == KingLogic.KING_X_LEFT
                            ? KingLogic.ROOK_X_LEFT_TO
                            : KingLogic.ROOK_X_RIGHT_TO));

            moveRock(game.getGrid(), rookFromField, rookToField);
        }
    }

    public void resetField(Field field) {
        field.setPiece(null);
        field.getButton().setText(field.getCode());
        field.getButton().setIcon(null);
    }

    public List<Field> resetValidMoves(Game game, Move move) {
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

            if (field.getPiece() != null && field.getPiece().getPieceType() == PAWN) {
                ((Pawn) field.getPiece()).setMayBeCapturedEnPassant(false);
            }
        });

        return allValidMoves;
    }

    public void setChessPieceSpecificFields(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PAWN) {
            Pawn pawn = (Pawn) from.getPiece();
            PawnLogic pawnLogic = (PawnLogic) pieceLogicFactory.getLogic(PAWN);
            pawn.setMayBeCapturedEnPassant(pawnLogic.mayBeCapturedEnPassant(game.getGrid(), from, to));
            pawn.setPawnBeingPromoted(pawnLogic.isPawnBeingPromoted(from, to));

            if (pawn.isPawnBeingPromoted()) {
                fieldLogic.addChessPiece(to,
                        pieceFactory.getPiece(from.getPiece().getPieceType().getNextPawnPromotion()).setColor(from.getPiece().getColor()));
            }
        } else if (from.getPiece().getPieceType() == PieceType.ROOK) {
            ((Rook) from.getPiece()).setHasMovedAtLeastOnce(true);
        } else if (from.getPiece().getPieceType() == PieceType.KING) {
            ((King) from.getPiece()).setHasMovedAtLeastOnce(true);
        }
    }

    public void setFieldColors(Game game, List<Field> allValidMoves) {
        game.getGrid().stream().filter(field -> field.getPiece() != null).forEach(field -> {
            if (field.getPiece().getPieceType() == PieceType.KING) {
                boolean isInCheckNow = kingLogic.isInCheckNow(game.getGrid(), field, field, pieceLogicFactory, false);
                field
                        .setCheckMate(isNotAbleToMove(game, field, allValidMoves) && isInCheckNow)
                        .setStaleMate(isNotAbleToMove(game, field, allValidMoves) && !isInCheckNow)
                        .getButton()
                        .setBackground(backgroundColorFactory.getBackgroundColor(field));
            } else {
                fieldLogic.setValue(game, field);
            }

            game.setInProgress(game.isInProgress()
                    ? !field.isCheckMate() && !field.isStaleMate()
                    : game.isInProgress());
        });
    }

    public void setFrom(Move move, Field from) {
        move.setPiece(from.getPiece());
        move.setFrom(from);
        move.setTo(null);

        from.setValidFrom(true);
    }

    public void setTo(List<Field> grid, Move move, Field to) {
        if (isCaptureEnPassant(move, to)) {
            captureEnPassant(grid, move.getFrom(), to);
        }
        move.setTo(to);
        move.getTo().setPiece(move.getFrom().getPiece());
        move.getTo().getButton().setText(null);
        move.getTo().getButton().setIcon(move.getFrom().getButton().getIcon());
    }

    private void captureEnPassant(List<Field> grid, Field from, Field to) {
        resetField(gridLogic.getField(grid, new Coordinates(to.getCoordinates().getX(), from.getCoordinates().getY())));
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

    private boolean isCaptureEnPassant(Move move, Field to) {
        return move.getFrom().getPiece().getPieceType() == PAWN &&
               move.getFrom().getCoordinates().getX() != to.getCoordinates().getX() &&
               to.getPiece() == null;
    }

    private boolean isNotAbleToMove(Game game, Field field, List<Field> allValidMoves) {
        return game.getCurrentPlayerColor() == field.getPiece().getColor() && game.isInProgress() && allValidMoves.isEmpty();
    }

    private void moveRock(List<Field> grid, Field from, Field to) {
        Move rookMove = new Move();
        setFrom(rookMove, from);
        setTo(grid, rookMove, to);
        resetField(rookMove.getFrom());
    }
}
