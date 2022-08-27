package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Move;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Rook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mark.chess.enums.PieceType.PAWN;

@Component
public class MoveLogic {
    private FieldLogic        fieldLogic;
    private GridLogic         gridLogic;
    private KingLogic         kingLogic;
    private PieceLogicFactory pieceLogicFactory;

    @Autowired
    public MoveLogic(FieldLogic fieldLogic, GridLogic gridLogic, KingLogic kingLogic, PieceLogicFactory pieceLogicFactory) {
        this.fieldLogic = fieldLogic;
        this.gridLogic = gridLogic;
        this.kingLogic = kingLogic;
        this.pieceLogicFactory = pieceLogicFactory;
    }

    private static boolean duringAMove(Move move, Field field) {
        return move.getFrom() != null &&
               move.getTo() != null &&
               Arrays.asList(move.getFrom().getCode(), move.getTo().getCode()).contains(field.getCode());
    }

    private static boolean isCaptureEnPassant(Move move, Field to) {
        return move.getFrom().getPiece().getPieceType() == PAWN &&
               move.getFrom().getCoordinates().getX() != to.getCoordinates().getX() &&
               to.getPiece() == null;
    }

    void changeTurn(Game game) {
        game.setCurrentPlayerColor(game.getCurrentPlayerColor().getOpposite());
    }

    void enableValidMoves(Game game, Field from) {
        game.getGrid().getFields().forEach(field -> field.setValidFrom(false).setValidMove(false).setAttacking(false).setUnderAttack(false));

        List<Field> validMoves = getValidMoves(game, from);
        validMoves.forEach(to -> {
            from.setValidFrom(true);
            to.setValidMove(true);
        });
        gridLogic.setValidMoveColors(game.getGrid(), from, validMoves, validMoves);
    }

    boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.getPiece() != null &&
               fieldClick.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerColor().ordinal()).getColor();
    }

    void moveRookWhenCastling(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PieceType.KING &&
            kingLogic.isValidCastling(game.getGrid(), from, to, to.getCoordinates().getX(), false, true)) {

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

    void resetField(Field field) {
        field.setPiece(null);
        field.getButton().setText(field.getCode());
        field.getButton().setIcon(null);
    }

    List<Field> resetValidMoves(Game game, Move move) {
        Map<Field, List<Field>> allValidFromsAndMoves = new HashMap<>();
        List<Field> allValidMoves = new ArrayList<>();

        game.getGrid().getFields().forEach(from -> {
            from.setAttacking(false).setUnderAttack(false).setValidFrom(false);

            List<Field> validMoves = getValidMoves(game, from);
            from.setValidMove(!validMoves.isEmpty());
            from.setValidFrom(from.isValidMove());
            allValidMoves.addAll(validMoves);

            if (from.isValidFrom()) {
                allValidFromsAndMoves.put(from, validMoves);
            }

            if (duringAMove(move, from)) {
                return;
            }

            if (from.getPiece() != null && from.getPiece().getPieceType() == PAWN) {
                ((Pawn) from.getPiece()).setMayBeCapturedEnPassant(false);
            }
        });

        allValidFromsAndMoves.forEach((from, validMoves) -> gridLogic.setValidMoveColors(game.getGrid(), from, validMoves, allValidMoves));

        return allValidMoves;
    }

    void setChessPieceSpecificFields(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PAWN) {
            Pawn pawn = (Pawn) from.getPiece();
            PawnLogic pawnLogic = (PawnLogic) pieceLogicFactory.getLogic(PAWN);
            pawn.setMayBeCapturedEnPassant(pawnLogic.mayBeCapturedEnPassant(game.getGrid(), from, to));
            pawn.setPawnBeingPromoted(pawnLogic.isPawnBeingPromoted(from, to));

            if (pawn.isPawnBeingPromoted()) {
                fieldLogic.addChessPiece(to,
                        PieceFactory.getPiece(from.getPiece().getPieceType().getNextPawnPromotion()).setColor(from.getPiece().getColor()));
            }
        } else if (from.getPiece().getPieceType() == PieceType.ROOK) {
            ((Rook) from.getPiece()).setHasMovedAtLeastOnce(true);
        } else if (from.getPiece().getPieceType() == PieceType.KING) {
            ((King) from.getPiece()).setHasMovedAtLeastOnce(true);
        }
    }

    void setFrom(Move move, Field from) {
        move.setPiece(from.getPiece());
        move.setFrom(from);
        move.setTo(null);

        from.setValidFrom(true);
    }

    void setTo(Grid grid, Move move, Field to) {
        if (isCaptureEnPassant(move, to)) {
            captureEnPassant(grid, move.getFrom(), to);
        }
        move.setTo(to);
        move.getTo().setPiece(move.getFrom().getPiece());
        move.getTo().getButton().setText(null);
        move.getTo().getButton().setIcon(move.getFrom().getButton().getIcon());
    }

    private void captureEnPassant(Grid grid, Field from, Field to) {
        resetField(gridLogic.getField(grid, new Coordinates(to.getCoordinates().getX(), from.getCoordinates().getY())));
    }

    private List<Field> getValidMoves(Game game, Field from) {
        return fieldLogic.isActivePlayerField(game, from)
                ? pieceLogicFactory.getLogic(from.getPiece().getPieceType()).getValidMoves(game.getGrid(), from)
                : new ArrayList<>();
    }

    private void moveRock(Grid grid, Field from, Field to) {
        Move rookMove = new Move();
        setFrom(rookMove, from);
        setTo(grid, rookMove, to);
        resetField(rookMove.getFrom());
    }
}
