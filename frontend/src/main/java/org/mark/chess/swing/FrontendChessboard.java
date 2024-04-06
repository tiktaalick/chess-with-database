package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.game.GameService;
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

/**
 * Class for the front-end chessboard.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class FrontendChessboard extends JFrame implements ActionListener, MouseListener {

    private static final int HEIGHT       = 870;
    private static final int SPLIT_IN_TWO = 2;
    private static final int WIDTH        = 828;

    private transient Game        game;
    private transient GameService gameService;

    private List<FrontendField> frontendFields;
    private Dimension           dimension;

    /**
     * Creates a new chessboard for the front-end.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     */
    public FrontendChessboard(PlayerColor humanPlayerColor) {
        this.gameService = new GameService();
        this.game = gameService.createGame(humanPlayerColor);
        this.createFields();
        this.initialize();
        this.updateFields();
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
        this.game = gameService.handleButtonClick(game,
                event.getButton(),
                FrontendField.createButtonId(this.game.getHumanPlayerColor(), ((FrontendField) event.getSource()).getId()));

        this.updateFields();
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
        this.getGameService().resetValidMoves(this.getGame());
    }

    private void updateFields() {
        this.getGame().getChessboard().getFields().forEach((Field field) -> {
            int buttonId = FrontendField.createButtonId(this.getGame().getHumanPlayerColor(), field.getId());

            FrontendField frontendField = Objects.isNull(field.getPieceType())
                    ? this.getFrontendFields().get(buttonId).reset(field).setId(buttonId)
                    : this.getFrontendFields().get(buttonId).updateGraphics(field).setId(buttonId);
            frontendField.setBackground(field.getBackgroundColor());
        });
    }
}
