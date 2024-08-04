package org.mark.chess.piece.king;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.general.IllegalPawnPromotionException;
import org.mark.chess.piece.general.PieceType;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.piece.king.isvalidmove.KingIsValidMoveRulesEngine;
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

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class King extends PieceType {

    private static final int                        PIECE_VALUE                = 0;
    private static final KingIsValidMoveRulesEngine kingIsValidMoveRulesEngine = new KingIsValidMoveRulesEngine();

    private boolean hasMovedAtLeastOnce;

    public King(PlayerColor color) {
        super(color);
    }

    @Override
    public List<Coordinates> createCandidateToFieldCoordinates(Field from) {
        return IntStream
                .rangeClosed(1, NUMBER_OF_COLUMNS_AND_ROWS)
                .mapToObj(number -> Stream.concat(diagonalMoves(from, number), horizontalAndVerticalMoves(from, number)).toList())
                .flatMap(Collection::stream)
                .filter(PieceTypeSharedRules.maxStepsVertically(from, 1))
                .filter(PieceTypeSharedRules.maxStepsHorizontally(from, 2))
                .filter(withinChessboardBoundaries())
                .filter(skipFrom(from))
                .toList();
    }

    @Override
    public String getName() {
        return KING;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        throw new IllegalPawnPromotionException();
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return kingIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, @NotNull Field from, Field to) {
        ((King) from.getPieceType()).setHasMovedAtLeastOnce(true);
    }
}
