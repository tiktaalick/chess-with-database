package org.mark.chess.logic;

import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Move;
import org.mark.chess.swing.Button;
import org.springframework.stereotype.Service;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;

@Service
public class BoardLogic {
    private static final int        HEIGHT       = 870;
    private static final int        LEFT_CLICK   = 1;
    private static final int        RIGHT_CLICK  = 3;
    private static final int        SPLIT_IN_TWO = 2;
    private static final int        WIDTH        = 828;
    private final        ColorLogic colorLogic;
    private final        MoveLogic  moveLogic;

    public BoardLogic(MoveLogic moveLogic, ColorLogic colorLogic) {
        this.moveLogic = moveLogic;
        this.colorLogic = colorLogic;
    }

    public void handleButtonClick(Game game, Window board, Move move, int buttonClick, Button button) {
        var fieldClick = game.getGrid().getField(button);

        if (!game.isInProgress()) {
            board.dispose();
            ApplicationFactory.getInstance().startApplication(game.getHumanPlayerColor().getOpposite());
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setFrom(move, fieldClick);
            moveLogic.enableValidMoves(game, fieldClick);
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && !moveLogic.isFrom(game, fieldClick)) {
            moveLogic.setTo(game.getGrid(), move, fieldClick);
            moveLogic.setChessPieceSpecificFields(game, move.getFrom(), fieldClick);
            moveLogic.moveRookWhenCastling(game, move.getFrom(), fieldClick);
            moveLogic.changeTurn(game);
            moveLogic.resetField(move.getFrom());
            colorLogic.setKingFieldColors(game, moveLogic.resetValidMoves(game, move));
        } else if (buttonClick == RIGHT_CLICK) {
            colorLogic.setKingFieldColors(game, moveLogic.resetValidMoves(game, move));
        }
    }

    public void initializeBoard(Game game, Frame board, Move move) {
        board.setSize(WIDTH, HEIGHT);
        board.setLayout(Grid.createGridLayout());
        board.setVisible(true);
        board.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        board.setLocation(dim.width / SPLIT_IN_TWO - WIDTH / SPLIT_IN_TWO, dim.height / SPLIT_IN_TWO - HEIGHT / SPLIT_IN_TWO);
        moveLogic.resetValidMoves(game, move);
    }
}
