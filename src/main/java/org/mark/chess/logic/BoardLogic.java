package org.mark.chess.logic;

import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Toolkit;

@Component
public class BoardLogic {
    private static final int HEIGHT      = 870;
    private static final int LEFT_CLICK  = 1;
    private static final int RIGHT_CLICK = 3;
    private static final int WIDTH       = 828;

    private MoveLogic moveLogic;
    private GridLogic gridLogic;

    @Autowired
    public BoardLogic(MoveLogic moveLogic, GridLogic gridLogic) {
        this.moveLogic = moveLogic;
        this.gridLogic = gridLogic;
    }

    public void handleButtonClick(Game game, Board board, int buttonClick, JButton button) {
        Field fieldClick = gridLogic.getField(game.getGrid(), button);

        if (!game.isInProgress()) {
            board.dispose();
            ApplicationFactory.getInstance().startApplication(game.getHumanPlayerColor().getOpposite());
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setFrom(board.getMove(), fieldClick);
            moveLogic.enableValidMoves(game, fieldClick);
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && !moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setTo(game.getGrid(), board.getMove(), fieldClick);
            moveLogic.setChessPieceSpecificFields(game, board.getMove().getFrom(), fieldClick);
            moveLogic.moveRookWhenCastling(game, board.getMove().getFrom(), fieldClick);
            moveLogic.changeTurn(game);
            moveLogic.resetField(board.getMove().getFrom());
            gridLogic.setKingFieldColors(game, moveLogic.resetValidMoves(game, board.getMove()));
        } else if (buttonClick == RIGHT_CLICK) {
            gridLogic.setKingFieldColors(game, moveLogic.resetValidMoves(game, board.getMove()));
        }
    }

    public void initializeBoard(Game game, Board board) {
        board.setSize(WIDTH, HEIGHT);
        board.setLayout(GridLogic.createGridLayout());
        board.setVisible(true);
        board.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        board.setLocation(dim.width / 2 - board.getSize().width / 2, dim.height / 2 - board.getSize().height / 2);
        moveLogic.resetValidMoves(game, board.getMove());
    }
}
