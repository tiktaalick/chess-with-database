package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.board.Grid;
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

/**
 * Class for the front-end chessboard.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Board extends JFrame implements ActionListener, MouseListener {

    private static final int HEIGHT       = 870;
    private static final int SPLIT_IN_TWO = 2;
    private static final int WIDTH        = 828;

    private final transient Game game;

    private transient GameService gameService;

    /**
     * Constructor.
     *
     * @param gameService      A service class for the front-end.
     * @param humanPlayerColor The piece-type color used by the human player.
     */
    public Board(GameService gameService, PlayerColor humanPlayerColor) {
        this.gameService = gameService;
        this.game = gameService.createGame(this, humanPlayerColor);
        this.initialize(game);
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
    public void mousePressed(MouseEvent event) {
        gameService.handleButtonClick(game, this, event.getButton(), (Button) event.getSource());
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

    private void initialize(Game game) {
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(Grid.createGridLayout());
        this.setVisible(true);
        this.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / SPLIT_IN_TWO - WIDTH / SPLIT_IN_TWO, dim.height / SPLIT_IN_TWO - HEIGHT / SPLIT_IN_TWO);
        gameService.resetValidMoves(game);
    }
}
