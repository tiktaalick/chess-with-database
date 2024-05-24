package org.mark.chess.piece.pawn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.general.PieceType;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidMoveRulesEngine;
import org.mark.chess.piece.pawn.maybecapturedenpassant.PawnMayBeCapturedEnPassantRulesEngine;
import org.mark.chess.piece.queen.Queen;
import org.mark.chess.player.PlayerColor;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mark.chess.board.Chessboard.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.diagonalMoves;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.horizontalAndVerticalMoves;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.skipFrom;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.withinChessboardBoundaries;

/**
 * Contains pawn related methods.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Pawn extends PieceType {

    private static final int                        PIECE_VALUE                = 1;
    private static final PawnIsValidMoveRulesEngine pawnIsValidMoveRulesEngine = new PawnIsValidMoveRulesEngine();

    private PawnMayBeCapturedEnPassantRulesEngine pawnMayBeCapturedEnPassantRulesEngine = new PawnMayBeCapturedEnPassantRulesEngine();
    private boolean                               mayBeCapturedEnPassant;

    public Pawn(PlayerColor color) {
        super(color);
    }

    @Override
    public List<Coordinates> createCandidateToFieldCoordinates(Field from) {
        return IntStream
                .rangeClosed(1, NUMBER_OF_COLUMNS_AND_ROWS)
                .mapToObj(number -> Stream.concat(diagonalMoves(from, number), horizontalAndVerticalMoves(from, number)).toList())
                .flatMap(Collection::stream)
                .filter(PieceTypeSharedRules.minStepsVertically(from))
                .filter(PieceTypeSharedRules.maxStepsVertically(from, 2))
                .filter(PieceTypeSharedRules.maxStepsHorizontally(from, 1))
                .filter(withinChessboardBoundaries())
                .filter(skipFrom(from))
                .toList();
    }

    @Override
    public String getName() {
        return PAWN;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        return new Queen(getColor());
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return pawnIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(@NotNull Game game, @NotNull Field from, Field to) {
        ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(game.getChessboard(), from, to).setBeingPromoted(from, to);

        if ((from.getPieceType()).isBeingPromoted()) {
            to.setPieceType(getNextPawnPromotion());
        }
    }

    /**
     * Sets whether the pawn is being promoted or not.
     *
     * @param from The field from which the pawn is moving.
     * @param to   The field to which the pawn is moving.
     * @return The pawn.
     */
    public Pawn setBeingPromoted(@NotNull Field from, Field to) {
        setBeingPromoted(from.getPieceType().isBeingPromoted() ||
                from.getCoordinates().getY() == from.getPieceType().getColor().getOpposite().getBaseline() ||
                to.getCoordinates().getY() == from.getPieceType().getColor().getOpposite().getBaseline());

        return this;
    }

    /**
     * Sets whether the pawn may be captured en passant or not.
     *
     * @param chessboard The backend representation of a chessboard.
     * @param from       The field from which the pawn is moving.
     * @param to         The field to which the pawn is moving.
     * @return The pawn.
     */
    public Pawn setMayBeCapturedEnPassant(Chessboard chessboard, Field from, Field to) {
        setMayBeCapturedEnPassant(pawnMayBeCapturedEnPassantRulesEngine.process(new IsValidMoveParameter(chessboard, from, to, false)));

        return this;
    }
}
