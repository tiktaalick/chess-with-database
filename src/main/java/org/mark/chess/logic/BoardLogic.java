package org.mark.chess.logic;

import org.mark.chess.Application;
import org.mark.chess.enums.GameStatus;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Move;
import org.mark.chess.model.Pawn;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardLogic {
    private static final int WIDTH = 414;
    private static final int HEIGHT = 435;
    private static final int LEFT_CLICK = 1;
    private static final int RIGHT_CLICK = 3;

    private final Move move = new Move();

    @Autowired
    private GameLogic gameLogic;

    @Autowired
    private GridLogic gridLogic;

    @Autowired
    private PieceLogicFactory pieceLogicFactory;

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
        Field fieldClick = game.grid().stream()
                .filter(field -> field.button() == button)
                .findFirst()
                .orElse(new Field());

        if (game.gameStatus() != GameStatus.IN_PROGRESS) {
            board.dispose();
            Application.getInstance().startApplication();
        } else if (buttonClick == LEFT_CLICK && isFrom(fieldClick)) {
            enableValidMoves(game, fieldClick);
        } else if (buttonClick == LEFT_CLICK && !isFrom(fieldClick)) {
            movePieceTo(game, fieldClick);
        } else if (buttonClick == RIGHT_CLICK) {
            resetValidMoves(game);
        }
    }

    private boolean isFrom(Field fieldClick) {
        return fieldClick.piece() != null;
    }

    private void enableValidMoves(Game game, Field from) {
        this.move.piece(from.piece());
        this.move.from(from);
        this.move.to(null);
        PieceLogic pieceLogic = pieceLogicFactory.getLogic(from.piece().pieceType());
        List<Field> validMoves = pieceLogic.getValidMoves(game.grid(), from, pieceLogicFactory);
        game.grid().forEach(field -> field.button().setEnabled(false));
        validMoves.forEach(to -> to.button().setEnabled(true));
    }

    private void movePieceTo(Game game, Field to) {
        PawnLogic pawnLogic = (PawnLogic) pieceLogicFactory.getLogic(PieceType.PAWN);
        this.move.to(to);
        this.move.to().piece(this.move.from().piece());
        this.move.to().button().setText(null);
        this.move.to().button().setIcon(this.move.from().button().getIcon());
        resetValidMoves(game);
        if (this.move.from().piece().pieceType() == PieceType.PAWN) {
            ((Pawn) this.move.from().piece())
                    .mayBeCapturedEnPassant(pawnLogic.mayBeCapturedEnPassant(game.grid(), move.from(), to));
            this.move.to().button().setToolTipText(((Pawn) this.move.from().piece()).mayBeCapturedEnPassant()
                    ? "May be captured en passant"
                    : "No en passant applicable");
        }
        this.move.from().piece(null);
        this.move.from().button().setIcon(null);
    }

    private void resetValidMoves(Game game) {
        game.grid().forEach(field -> {
            field.button().setEnabled(field.piece() != null);
            if (field.piece() != null && field.piece().pieceType() == PieceType.PAWN) {
                ((Pawn) field.piece()).mayBeCapturedEnPassant(false);
                field.button().setToolTipText("No en passant applicable");
            }
        });
    }
}
