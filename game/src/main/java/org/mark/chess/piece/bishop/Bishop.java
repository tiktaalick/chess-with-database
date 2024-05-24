package org.mark.chess.piece.bishop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.bishop.isvalidmove.BishopIsValidMoveRulesEngine;
import org.mark.chess.piece.general.PieceType;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.knight.Knight;
import org.mark.chess.player.PlayerColor;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.mark.chess.board.Chessboard.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.diagonalMoves;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.skipFrom;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.withinChessboardBoundaries;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Bishop extends PieceType {

    private static final int                          PIECE_VALUE                  = 3;
    private static final BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine = new BishopIsValidMoveRulesEngine();

    public Bishop(PlayerColor color) {
        super(color);
    }

    @Override
    public List<Coordinates> createCandidateToFieldCoordinates(Field from) {
        return IntStream
                .rangeClosed(1, NUMBER_OF_COLUMNS_AND_ROWS)
                .mapToObj(number -> diagonalMoves(from, number).toList())
                .flatMap(Collection::stream)
                .filter(withinChessboardBoundaries())
                .filter(skipFrom(from))
                .toList();
    }

    @Override
    public String getName() {
        return BISHOP;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        return new Knight(getColor());
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return bishopIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, Field from, Field to) {
        // No specific fields for bishop need to be set.
    }
}
