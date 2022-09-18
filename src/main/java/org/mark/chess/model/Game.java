package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.logic.MoveLogic;
import org.mark.chess.swing.Board;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.enums.Color.WHITE;

@Data
@Accessors(chain = true)
public class Game {
    private static final int MAXIMUM_SQUARE_ID = 63;

    private boolean      inProgress;
    private Color        humanPlayerColor;
    private Color        currentPlayerColor;
    private Grid         grid;
    private List<Player> players;

    public Grid initializeGrid(Game game, Board board, CheckLogic checkLogic, MoveLogic moveLogic) {
        return new Grid(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .map(id -> (game.getHumanPlayerColor() == WHITE)
                        ? id
                        : (MAXIMUM_SQUARE_ID - id))
                .mapToObj(id -> new Field(null).initialize(board, id).addChessPiece(InitialPieceFactory.getInitialPiece(id)))
                .collect(Collectors.toList()), checkLogic, moveLogic);
    }
}
