package org.mark.chess.swing;

import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.game.GameService;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.Objects;

public class BoardBuilder {

    private static final int HEIGHT       = 870;
    private static final int SPLIT_IN_TWO = 2;
    private static final int WIDTH        = 828;

    private Board board;

    public Board build() {
        return this.board;
    }

    public BoardBuilder createButtons() {
        this.board.setButtons(new ArrayList<>());
        this.board.getGame().getGrid().getFields().forEach((Field field) -> {
            var button = new Button(this.board, field);
            this.board.getButtons().add(field.getId(), button);
            this.board.add(button);
        });

        return this;
    }

    public BoardBuilder initialize() {
        this.board.setSize(WIDTH, HEIGHT);
        this.board.setLayout(Grid.createGridLayout());
        this.board.setVisible(true);
        this.board.setResizable(false);
        this.board.setDimension();
        this.board.setLocation(this.board.getDimensionWidth() / SPLIT_IN_TWO - WIDTH / SPLIT_IN_TWO,
                this.board.getDimensionHeight() / SPLIT_IN_TWO - HEIGHT / SPLIT_IN_TWO);
        this.board.getGameService().resetValidMoves(this.board.getGame());

        return this;
    }

    public BoardBuilder setBoard(GameService gameService, PlayerColor humanPlayerColor) {
        this.board = new Board(gameService, humanPlayerColor);

        return this;
    }

    public BoardBuilder setBoard(Board board) {
        this.board = board;

        return this;
    }

    public BoardBuilder updateButtons() {
        this.board.getGame().getGrid().getFields().forEach((Field field) -> {
            int buttonId = Button.createButtonId(this.board.getGame().getHumanPlayerColor(), field.getId());

            Button button = Objects.isNull(field.getPieceType())
                    ? this.board.getButtons().get(buttonId).reset(field).setId(buttonId)
                    : this.board.getButtons().get(buttonId).initialize(field).setId(buttonId);
            button.setBackground(field.getBackgroundColor());
        });

        return this;
    }
}
