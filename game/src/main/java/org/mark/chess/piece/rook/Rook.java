package org.mark.chess.piece.rook;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.bishop.Bishop;
import org.mark.chess.piece.general.PieceType;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.rook.isvalidmove.RookIsValidMoveRulesEngine;
import org.mark.chess.player.PlayerColor;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.mark.chess.board.Chessboard.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.horizontalAndVerticalMoves;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.skipFrom;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Rook extends PieceType {

    private static final int                        PIECE_VALUE                = 5;
    private static final RookIsValidMoveRulesEngine rookIsValidMoveRulesEngine = new RookIsValidMoveRulesEngine();

    private boolean hasMovedAtLeastOnce;

    public Rook(PlayerColor color) {
        super(color);
    }

    @Override
    public List<Coordinates> createCandidateCoordinates(Field from) {
        return IntStream
                .rangeClosed(1, NUMBER_OF_COLUMNS_AND_ROWS)
                .mapToObj(number -> horizontalAndVerticalMoves(from, number).toList())
                .flatMap(Collection::stream)
                .filter(skipFrom(from))
                .toList();
    }

    @Override
    public String getName() {
        return ROOK;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        return new Bishop(getColor());
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return rookIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, @NotNull Field from, Field to) {
        ((Rook) from.getPieceType()).setHasMovedAtLeastOnce(true);
    }
}
