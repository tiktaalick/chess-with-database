package org.mark.chess.game;

import org.mark.chess.board.ChessboardDirector;
import org.mark.chess.board.Field;
import org.mark.chess.player.PlayerColor;

import java.util.List;

public class GameBuilder {

    private static ChessboardDirector chessboardDirector = new ChessboardDirector();

    private Game        game;
    private List<Field> allValidToFields;

    public Game build() {
        return this.game;
    }

    public GameBuilder createGame(PlayerColor humanPlayerColor) {
        this.game = new Game(humanPlayerColor, chessboardDirector.createChessboard());

        return this;
    }

    public GameBuilder resetValidMoves() {
        this.allValidToFields = this.game.getChessboard().resetValidMoves(this.game.getMove(), this.game.getHumanPlayerColor());

        return this;
    }

    public GameBuilder setValidMoveColors() {
        this.game
                .getChessboard()
                .getAllValidFromToCombinations()
                .forEach((from, validToFields) -> this.game
                        .getChessboard()
                        .setValidMoveColors(from, validToFields, this.allValidToFields, game.getActivePlayer().getColor()));

        return this;
    }
}
