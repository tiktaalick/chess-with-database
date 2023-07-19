package org.mark.chess.swing;

import org.mark.chess.board.Field;
import org.mark.chess.game.GameService;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A builder to build the front-end chessboard.
 */
public class FrontendChessboardBuilder {

    private static final int HEIGHT       = 870;
    private static final int SPLIT_IN_TWO = 2;
    private static final int WIDTH        = 828;

    private FrontendChessboard frontendChessboard;

    /**
     * Builds the front-end chessboard.
     *
     * @return The front-end chessboard.
     */
    public FrontendChessboard build() {
        return this.frontendChessboard;
    }

    /**
     * Creates the buttons on the front-end chessboard.
     *
     * @return The front-end chessboard builder.
     */
    public FrontendChessboardBuilder createFields() {
        this.frontendChessboard.setFrontendFields(new ArrayList<>());
        this.frontendChessboard.getGame().getChessboard().getFields().forEach((Field field) -> {
            var button = new FrontendField(this.frontendChessboard, field);
            this.frontendChessboard.getFrontendFields().add(field.getId(), button);
            this.frontendChessboard.add(button);
        });

        return this;
    }

    /**
     * Initializes the front-end chessboard.
     *
     * @return The front-end chessboard builder.
     */
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

    /**
     * Creates a basic front-end chessboard.
     *
     * @param gameService      The game service.
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return The front-end chessboard builder.
     */
    public FrontendChessboardBuilder setBoard(GameService gameService, PlayerColor humanPlayerColor) {
        this.frontendChessboard = new FrontendChessboard(gameService, humanPlayerColor);

        return this;
    }

    /**
     * Adds a basic front-end chessboard.
     *
     * @return The front-end chessboard builder.
     */
    public FrontendChessboardBuilder setBoard(FrontendChessboard frontendChessboard) {
        this.frontendChessboard = frontendChessboard;

        return this;
    }

    /**
     * Updates the buttons on the front-end chessboard.
     *
     * @return The front-end chessboard builder.
     */
    public FrontendChessboardBuilder updateFields() {
        this.frontendChessboard.getGame().getChessboard().getFields().forEach((Field field) -> {
            int buttonId = FrontendField.createButtonId(this.frontendChessboard.getGame().getHumanPlayerColor(), field.getId());

            FrontendField frontendField = Objects.isNull(field.getPieceType()) ? this.frontendChessboard
                    .getFrontendFields()
                    .get(buttonId)
                    .reset(field)
                    .setId(buttonId) : this.frontendChessboard.getFrontendFields().get(buttonId).update(field).setId(buttonId);
            frontendField.setBackground(field.getBackgroundColor());
        });

        return this;
    }
}
