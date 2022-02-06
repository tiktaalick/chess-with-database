package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

import javax.swing.*;
import java.awt.*;

public class FieldLogic {
    private static final double RELATIVE_IMAGE_SIZE = .8;
    private static final String IMAGES = "src/main/resources/images/";
    private static final String UNDERSCORE = "_";
    private static final String EXTENSION = ".png";

    public Field initializeField(Board board, int row, int column, int numberOfColumns) {
        int id = createId(row, column, numberOfColumns);

        Field field = new Field()
                .id(id)
                .coordinates(new Coordinates(column, row))
                .button(new Button(
                        column * Button.FIELD_WIDTH,
                        row * Button.FIELD_WIDTH,
                        (id + row) % 2 == 0 ? Color.LIGHT.getAwtColor() : Color.DARK.getAwtColor(),
                        String.valueOf(id),
                        board,
                        board));
        field.button().setEnabled(false);
        board.add(field.button());

        return field;
    }

    public JButton initializeButton(Game game, int index, Piece piece, Color color) {
        JButton button = game.grid().get(index).button();

        button.setEnabled(setEnabledButton(game, game.grid().get(index)));
        button.setToolTipText(color.getName() + " " + piece.pieceType().getName());
        button.setText(null);
        button.setBorder(null);
        button.setIcon(new ImageIcon(new ImageIcon(
                IMAGES + color.getName() + UNDERSCORE + piece.pieceType().getName() + EXTENSION)
                .getImage()
                .getScaledInstance(
                        (int) (button.getWidth() * RELATIVE_IMAGE_SIZE),
                        (int) (button.getHeight() * RELATIVE_IMAGE_SIZE),
                        Image.SCALE_SMOOTH)));

        return button;
    }

    private int createId(int row, int column, int numberOfColumns) {
        return row * numberOfColumns + column;
    }

    public boolean setEnabledButton(Game game, Field field) {
        return field.piece() != null &&
                field.piece().color() == game.players().get(game.currentPlayerIndex()).color();
    }

}