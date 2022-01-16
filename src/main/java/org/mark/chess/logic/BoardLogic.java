package org.mark.chess.logic;

import org.mark.chess.Application;
import org.mark.chess.enums.GameStatus;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardLogic {
    private static final int WIDTH = 414;
    private static final int HEIGHT = 435;

    @Autowired
    private GameLogic gameLogic;

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

    public void addButtons(Board board, List<Field> fields) {
        for (Field field : fields) {
            board.add(field.button());
        }
    }

    public void handleButtonClick(Game game, Board board, int buttonClick, JButton button) {
        if (game.gameStatus() != GameStatus.IN_PROGRESS) {
            board.dispose();
            Application.getInstance().startApplication();
        }
    }
}
