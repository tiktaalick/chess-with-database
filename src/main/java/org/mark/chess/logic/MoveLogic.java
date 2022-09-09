package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Move;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.PieceTypeLogic;
import org.mark.chess.model.Rook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mark.chess.enums.PieceType.PAWN;

@Service
public class MoveLogic {
    private final ColorLogic     colorLogic;
    private final FieldLogic     fieldLogic;
    private final GridLogic      gridLogic;
    private final KingLogic      kingLogic;
    private final PieceTypeLogic pieceTypeLogic;

    public MoveLogic(FieldLogic fieldLogic, GridLogic gridLogic, KingLogic kingLogic, PieceTypeLogic pieceTypeLogic, ColorLogic colorLogic) {
        this.fieldLogic = fieldLogic;
        this.gridLogic = gridLogic;
        this.kingLogic = kingLogic;
        this.pieceTypeLogic = pieceTypeLogic;
        this.colorLogic = colorLogic;
    }

    void changeTurn(Game game) {
        game.setCurrentPlayerColor(game.getCurrentPlayerColor().getOpposite());
    }

    void enableValidMoves(Game game, Field from) {
        game.getGrid().getFields().forEach(field -> field.setValidFrom(false).setValidMove(false).setAttacking(false).setUnderAttack(false));

        List<Field> validMoves = getValidMoves(game, from);
        validMoves.forEach((Field to) -> {
            from.setValidFrom(true);
            to.setValidMove(true);
        });
        colorLogic.setValidMoveColors(game.getGrid(), from, validMoves, validMoves);
    }

    boolean isFrom(Game game, Field fieldClick) {
        return fieldClick.getPiece() != null &&
                fieldClick.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerColor().ordinal()).getColor();
    }

    boolean isNotAbleToMove(Game game, Field field, Collection<Field> allValidMoves) {
        return game.getCurrentPlayerColor() == field.getPiece().getColor() && game.isInProgress() && allValidMoves.isEmpty();
    }

    void moveRookWhenCastling(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PieceType.KING &&
                kingLogic.isValidCastling(game.getGrid(), from, to, to.getCoordinates().getX(), false, true)) {

            var rookCoordinates = new Coordinates((to.getCoordinates().getX() == KingLogic.KING_X_LEFT
                    ? KingLogic.ROOK_X_LEFT_FROM
                    : KingLogic.ROOK_X_RIGHT_FROM), from.getPiece().getColor().getBaseline());

            var rookFromField = gridLogic.getField(game.getGrid(), rookCoordinates);
            var rookToField = gridLogic.getField(game.getGrid(),
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
        Map<Field, List<Field>> allValidFromsAndValidMoves = new HashMap<>();
        List<Field> allValidMoves = new ArrayList<>();

        game.getGrid().getFields().forEach((Field from) -> {
            from.setAttacking(false).setUnderAttack(false).setValidFrom(false);

            setValidMoves(game, allValidFromsAndValidMoves, allValidMoves, from);

            if (!duringAMove(move, from) && from.getPiece() != null && from.getPiece().getPieceType() == PAWN) {
                ((Pawn) from.getPiece()).setMayBeCapturedEnPassant(false);
            }
        });

        allValidFromsAndValidMoves.forEach((from, validMoves) -> colorLogic.setValidMoveColors(game.getGrid(), from, validMoves, allValidMoves));

        return allValidMoves;
    }

    void setChessPieceSpecificFields(Game game, Field from, Field to) {
        if (from.getPiece().getPieceType() == PAWN) {
            var pawn = (Pawn) from.getPiece();
            var pawnLogic = (PawnLogic) PAWN.getLogic(pieceTypeLogic);
            pawn.setMayBeCapturedEnPassant(pawnLogic.mayBeCapturedEnPassant(game.getGrid(), from, to));
            pawn.setPawnBeingPromoted(pawnLogic.isPawnBeingPromoted(from, to));

            if (pawn.isPawnBeingPromoted()) {
                fieldLogic.addChessPiece(to, from.getPiece().getPieceType().getNextPawnPromotion().createPiece(from.getPiece().getColor()));
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

    private void captureEnPassant(Grid grid, Field from, Field to) {
        resetField(gridLogic.getField(grid, new Coordinates(to.getCoordinates().getX(), from.getCoordinates().getY())));
    }

    private List<Field> getValidMoves(Game game, Field from) {
        return fieldLogic.isActivePlayerField(game, from)
                ? from.getPiece().getPieceType().getLogic(pieceTypeLogic).getValidMoves(game.getGrid(), from)
                : new ArrayList<>();
    }

    private void moveRock(Grid grid, Field from, Field to) {
        var rookMove = new Move();
        setFrom(rookMove, from);
        setTo(grid, rookMove, to);
        resetField(rookMove.getFrom());
    }

    private void setValidMoves(Game game, Map<Field, List<Field>> allValidFromsAndValidMoves, List<Field> allValidMoves, Field from) {
        List<Field> validMoves = getValidMoves(game, from);
        from.setValidMove(!validMoves.isEmpty());
        from.setValidFrom(from.isValidMove());
        allValidMoves.addAll(validMoves);

        if (from.isValidFrom()) {
            allValidFromsAndValidMoves.put(from, validMoves);
        }
    }
}
