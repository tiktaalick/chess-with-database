package org.mark.chess.logic;

import org.mark.chess.Application;
import org.mark.chess.enums.GameStatus;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Move;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

public class BoardLogic {
    private static final int WIDTH = 414;
    private static final int HEIGHT = 435;
    private static final int LEFT_CLICK = 1;
    private static final int RIGHT_CLICK = 3;
    private final Move move = new Move();

    @Autowired
    private MoveLogic moveLogic;

    @Autowired
    private GridLogic gridLogic;

    public void initializeBoard(Board board) {
        board.setSize(WIDTH, HEIGHT);
        board.setLayout(gridLogic.createGridLayout());
        board.setVisible(true);
        board.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        board.setLocation(dim.width / 2 - board.getSize().width / 2, dim.height / 2 - board.getSize().height / 2);
    }

    public void handleButtonClick(Game game, Board board, int buttonClick, JButton button) {
        Field fieldClick = gridLogic.getField(game.grid(), button);

        if (game.gameStatus() == GameStatus.IN_PROGRESS && buttonClick == LEFT_CLICK && !button.isEnabled()) {
            return;
        }

        if (game.gameStatus() != GameStatus.IN_PROGRESS) {
            board.dispose();
            Application.getInstance().startApplication();
        } else if (buttonClick == LEFT_CLICK && moveLogic.isFrom(fieldClick)) {
            moveLogic.setFrom(move, fieldClick);
            moveLogic.enableValidMoves(game, fieldClick);
        } else if (buttonClick == LEFT_CLICK && !moveLogic.isFrom(fieldClick)) {
            moveLogic.setTo(move, fieldClick);
            moveLogic.setChessPieceSpecificFields(game.grid(), move.from(), fieldClick);
            moveLogic.moveRookWhenCastling(game.grid(), move.from(), fieldClick);
            moveLogic.resetValidMoves(game);
            moveLogic.resetFrom(move);
        } else if (buttonClick == RIGHT_CLICK) {
            moveLogic.resetValidMoves(game);
        }
    }
}
