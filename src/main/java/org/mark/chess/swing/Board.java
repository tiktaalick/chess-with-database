package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.Application;
import org.mark.chess.game.Game;
import org.mark.chess.game.GameService;
import org.mark.chess.player.PlayerColor;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * Class for the front-end chessboard.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class Board extends JFrame implements ActionListener, MouseListener {

    private transient Game         game;
    private transient GameService  gameService;
    private           List<Button> buttons;
    private           Dimension    dimension;

    /**
     * Creates a new chessboard for the front-end.
     *
     * @param gameService      A service class for the front-end.
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return The created chessboard.
     */
    public Board(GameService gameService, PlayerColor humanPlayerColor) {
        this.gameService = gameService;
        this.game = gameService.createGame(humanPlayerColor);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Ignored
    }

    public int getDimensionHeight() {
        return this.dimension.height;
    }

    public int getDimensionWidth() {
        return this.dimension.width;
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
    public void mousePressed(MouseEvent event) {
        this.game = gameService.handleButtonClick(game,
                event.getButton(),
                Button.createButtonId(this.game.getHumanPlayerColor(), ((Button) event.getSource()).getId()));

        Application.getBoardBuilder().setBoard(this).updateButtons();
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

    public void setDimension() {
        this.dimension = Toolkit.getDefaultToolkit().getScreenSize();
    }
}
