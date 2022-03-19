package org.mark.chess.logic;

import org.mark.chess.enums.GameStatus;
import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

public class BoardLogic {
    private static final int WIDTH = 414;
    private static final int HEIGHT = 435;
    private static final int LEFT_CLICK = 1;
    private static final int RIGHT_CLICK = 3;

    @Autowired
    private MoveLogic moveLogic;

    @Autowired
    private GridLogic gridLogic;

    @Autowired
    private ApplicationFactory applicationFactory;

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
            applicationFactory.getInstance().startApplication();
        }
        else if (buttonClick == LEFT_CLICK && moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setFrom(board.move(), fieldClick);
            moveLogic.enableValidMoves(game, fieldClick);
        }
        else if (buttonClick == LEFT_CLICK && !moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setTo(board.move(), fieldClick);
            moveLogic.setChessPieceSpecificFields(game, board.move().from(), fieldClick);
            moveLogic.moveRookWhenCastling(game.grid(), board.move().from(), fieldClick);
            moveLogic.changeTurn(game);
            moveLogic.resetValidMoves(game, board.move());
            moveLogic.resetFrom(board.move());
        }
        else if (buttonClick == RIGHT_CLICK) {
            moveLogic.resetValidMoves(game, board.move());
        }
    }
}
