package org.mark.chess.swing;

import org.mark.chess.board.Field;
import org.mark.chess.game.GameService;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.Objects;

public class FrontendChessboardBuilder {

    private static final int HEIGHT       = 870;
    private static final int SPLIT_IN_TWO = 2;
    private static final int WIDTH        = 828;

    private FrontendChessboard frontendChessboard;

    public FrontendChessboard build() {
        return this.frontendChessboard;
    }

    public FrontendChessboardBuilder createButtons() {
        this.frontendChessboard.setFrontendFields(new ArrayList<>());
        this.frontendChessboard.getGame().getChessboard().getFields().forEach((Field field) -> {
            var button = new FrontendField(this.frontendChessboard, field);
            this.frontendChessboard.getFrontendFields().add(field.getId(), button);
            this.frontendChessboard.add(button);
        });

        return this;
    }

    public FrontendChessboardBuilder initialize() {
        this.frontendChessboard.setSize(WIDTH, HEIGHT);
        this.frontendChessboard.setLayout(FrontendChessboard.createGridLayout());
        this.frontendChessboard.setVisible(true);
        this.frontendChessboard.setResizable(false);
        this.frontendChessboard.setDimension();
        this.frontendChessboard.setLocation(this.frontendChessboard.getDimensionWidth() / SPLIT_IN_TWO - WIDTH / SPLIT_IN_TWO,
                this.frontendChessboard.getDimensionHeight() / SPLIT_IN_TWO - HEIGHT / SPLIT_IN_TWO);
        this.frontendChessboard.getGameService().resetValidMoves(this.frontendChessboard.getGame());

        return this;
    }

    public FrontendChessboardBuilder setBoard(GameService gameService, PlayerColor humanPlayerColor) {
        this.frontendChessboard = new FrontendChessboard(gameService, humanPlayerColor);

        return this;
    }

    public FrontendChessboardBuilder setBoard(FrontendChessboard frontendChessboard) {
        this.frontendChessboard = frontendChessboard;

        return this;
    }

    public FrontendChessboardBuilder updateButtons() {
        this.frontendChessboard.getGame().getChessboard().getFields().forEach((Field field) -> {
            int buttonId = FrontendField.createButtonId(this.frontendChessboard.getGame().getHumanPlayerColor(), field.getId());

            FrontendField frontendField = Objects.isNull(field.getPieceType())
                    ? this.frontendChessboard.getFrontendFields().get(buttonId).reset(field).setId(buttonId)
                    : this.frontendChessboard.getFrontendFields().get(buttonId).initialize(field).setId(buttonId);
            frontendField.setBackground(field.getBackgroundColor());
        });

        return this;
    }
}
