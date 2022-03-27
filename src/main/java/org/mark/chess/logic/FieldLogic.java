package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;

import static org.mark.chess.logic.GridLogic.NUMBER_OF_COLUMNS_AND_ROWS;

public class FieldLogic {
    @Autowired
    private ButtonLogic buttonLogic;

    public Field initializeField(Board board, int row, int column) {
        int id = createId(row, column, NUMBER_OF_COLUMNS_AND_ROWS);

        Field field = new Field().id(id).coordinates(new Coordinates(column, row));
        field.button(new Button(board, field));
        field.button().setEnabled(false);
        board.add(field.button());

        return field;
    }

    public int createId(int row, int column, int numberOfColumns) {
        return row * numberOfColumns + column;
    }

    public JButton initializeButton(Game game, int index) {
        Field field = game.grid().get(index);
        Piece piece = field.piece();
        Color color = piece.color();
        JButton button = field.button();

        button.setEnabled(setEnabledButton(game, field));
        button.setToolTipText(color.getName() + " " + piece.pieceType().getName());
        button.setText(null);
        button.setIcon(new ImageIcon(new ImageIcon(buttonLogic.getIconPath(piece, color))
                .getImage()
                .getScaledInstance(buttonLogic.getIconWidth(button), buttonLogic.getIconWidth(button), Image.SCALE_SMOOTH)));

        return button;
    }

    public boolean setEnabledButton(Game game, Field field) {
        return field.piece() != null && field.piece().color() == game.players().get(game.currentPlayerIndex()).color();
    }
}