package org.mark.chess.piece.knight;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.general.PieceType;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.knight.isvalidmove.KnightIsValidMoveRulesEngine;
import org.mark.chess.piece.queen.Queen;
import org.mark.chess.player.PlayerColor;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static org.mark.chess.board.Chessboard.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.withinChessboardBoundaries;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Knight extends PieceType {

    private static final int                          PIECE_VALUE                  = 3;
    private static final KnightIsValidMoveRulesEngine knightIsValidMoveRulesEngine = new KnightIsValidMoveRulesEngine();

    public Knight(PlayerColor color) {
        super(color);
    }

    @Override
    public List<Coordinates> createCandidateCoordinates(Field from) {
        return IntStream
                .rangeClosed(1, NUMBER_OF_COLUMNS_AND_ROWS)
                .filter(number -> abs(from.getCoordinates().getX() - number) >= 1 && abs(from.getCoordinates().getX() - number) <= 2)
                .mapToObj(number -> List.of(new Coordinates(number, from.getCoordinates().getY() - 3 + abs(from.getCoordinates().getX() - number)),
                        new Coordinates(number, from.getCoordinates().getY() + 3 - abs(from.getCoordinates().getX() - number))))
                .flatMap(Collection::stream)
                .filter(withinChessboardBoundaries())
                .toList();
    }

    @Override
    public String getName() {
        return KNIGHT;
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
        return knightIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, Field from, Field to) {
        // No specific fields for knight need to be set.
    }
}
