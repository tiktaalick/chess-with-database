package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.game.GameService;
import org.mark.chess.log.Logging;
import org.mark.chess.player.PlayerColor;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Class for the front-end chessboard.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class FrontendChessboard extends JFrame implements ActionListener, MouseListener {

    private static final GameService GAME_SERVICE = new GameService();
    private static final int         HEIGHT       = 870;
    private static final Logger      LOGGER       = Logger.getLogger(FrontendChessboard.class.getName());
    private static final int         SPLIT_IN_TWO = 2;
    private static final int         WIDTH        = 828;

    private transient Game                game;
    private           List<FrontendField> frontendFields;
    private           Dimension           dimension;

    /**
     * Creates a new chessboard for the front-end.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     */
    public FrontendChessboard(PlayerColor humanPlayerColor) {
        long start = System.nanoTime();
        this.game = GAME_SERVICE.createGame(humanPlayerColor);
        long beforeCreateFields = System.nanoTime();
        this.createFields();
        long afterCreateFields = System.nanoTime();
        this.initialize();
        this.updateFields();
        long stop = System.nanoTime();

        Logging.storeDuration("frontendChessboard.createFields()", beforeCreateFields, afterCreateFields);
        Logging.storeDuration("Application start", start, stop);

        Logging.logDurationAndReset();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Ignored
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        // Ignored
    }

    /**
     * Handles the user input.
     *
     * @param event The mouse event.
     */
    @Override
    public void mousePressed(@NotNull MouseEvent event) {
        long beforeCall = System.nanoTime();
        this.game = GAME_SERVICE.handleButtonClick(game,
                event.getButton(),
                FrontendField.createButtonId(this.game.getHumanPlayerColor(), ((FrontendField) event.getSource()).getId()));
        long afterCall = System.nanoTime();
        this.updateFields();

        Logging.storeDuration("gameService.handleButtonClick()", beforeCall, afterCall);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseExited(MouseEvent event) {
        // Ignored
    }

    private static @NotNull GridLayout createGrid() {
        return new GridLayout(Chessboard.NUMBER_OF_COLUMNS_AND_ROWS, Chessboard.NUMBER_OF_COLUMNS_AND_ROWS);
    }

    private void createFields() {
        this.setFrontendFields(new ArrayList<>());
        this.getGame().getChessboard().getFields().forEach((Field field) -> {
            var button = new FrontendField(this, field);
            this.getFrontendFields().add(field.getId(), button);
            this.add(button);
        });
    }

    private void initialize() {
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(FrontendChessboard.createGrid());
        this.setVisible(true);
        this.setResizable(false);
        this.dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(this.dimension.width / SPLIT_IN_TWO - WIDTH / SPLIT_IN_TWO, this.dimension.height / SPLIT_IN_TWO - HEIGHT / SPLIT_IN_TWO);
        long beforeCall = System.nanoTime();
        GAME_SERVICE.resetValidMoves(this.getGame());
        long afterCall = System.nanoTime();

        Logging.storeDuration("getGameService().resetValidMoves()", beforeCall, afterCall);
    }

    private void updateFields() {
        LOGGER.info(() -> "Main chessboard=" + this.game.getChessboard().hashCode());
        LOGGER.info(() -> "Main activePlayerColor=" + this.game.getActivePlayer().getColor().getName());
        LOGGER.info(() -> "Main childrenActivePlayerColor=" + this.game.getChessboard().getChildrenPlayerColor().getName());
        LOGGER.info(() -> "Main fromParentToChildMove=" + this.game.getChessboard().getFromParentToChildMove());
        LOGGER.info(() -> "Main kingField=" + this.game.getChessboard().getKingField());
        LOGGER.info(() -> "Main opponentKingField=" + this.game.getChessboard().getOpponentKingField());

        this.getGame().getChessboard().getFields().forEach(field -> {
            int buttonId = FrontendField.createButtonId(this.getGame().getHumanPlayerColor(), field.getId());

            FrontendField frontendField = Objects.isNull(field.getPieceType())
                                          ? this.getFrontendFields().get(buttonId).reset(field).setId(buttonId)
                                          : this.getFrontendFields().get(buttonId).updateGraphics(field).setId(buttonId);
            frontendField.setBackground(field.getBackgroundColor());
        });
    }
}
