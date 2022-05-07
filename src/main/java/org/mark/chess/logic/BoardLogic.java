package org.mark.chess.logic;

import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Toolkit;

public class BoardLogic {
    private static final int       HEIGHT      = 870;
    private static final int       LEFT_CLICK  = 1;
    private static final int       RIGHT_CLICK = 3;
    private static final int       WIDTH       = 828;
    @Autowired
    private              MoveLogic moveLogic;

    @Autowired
    private GridLogic gridLogic;

    @Autowired
    private ApplicationFactory applicationFactory;

    public void handleButtonClick(Game game, Board board, int buttonClick, JButton button) {
        Field fieldClick = gridLogic.getField(game.getGrid(), button);

        if (!game.isInProgress()) {
            board.dispose();
            applicationFactory.getInstance().startApplication(game.getHumanPlayerColor().getOpposite());
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setFrom(board.getMove(), fieldClick);
            moveLogic.enableValidMoves(game, fieldClick);
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && !moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setTo(board.getMove(), fieldClick);
            moveLogic.setChessPieceSpecificFields(game, board.getMove().getFrom(), fieldClick);
            moveLogic.moveRookWhenCastling(game, board.getMove().getFrom(), fieldClick);
            moveLogic.changeTurn(game);
            moveLogic.resetFrom(board.getMove());
            moveLogic.setFieldColors(game, moveLogic.resetValidMoves(game, board.getMove()));
        } else if (buttonClick == RIGHT_CLICK) {
            moveLogic.setFieldColors(game, moveLogic.resetValidMoves(game, board.getMove()));
        }
    }

    public void initializeBoard(Game game, Board board) {
        board.setSize(WIDTH, HEIGHT);
        board.setLayout(gridLogic.createGridLayout());
        board.setVisible(true);
        board.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        board.setLocation(dim.width / 2 - board.getSize().width / 2, dim.height / 2 - board.getSize().height / 2);
        moveLogic.resetValidMoves(game, board.getMove());
    }
}
