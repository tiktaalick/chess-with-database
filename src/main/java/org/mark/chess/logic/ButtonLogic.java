package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;

import static org.mark.chess.swing.Button.FIELD_WIDTH;

public class ButtonLogic {
    private static final String EXTENSION  = ".png";
    private static final String IMAGES     = "src/main/resources/images/";
    private static final String UNDERSCORE = "_";

    public String getIconPath(Piece piece, Color color) {
        return IMAGES + color.getName() + UNDERSCORE + piece.getPieceType().getName() + EXTENSION;
    }

    public JButton initializeButton(Field field) {
        JButton button = field.getButton();

        if (field.getPiece() == null) {
            return button;
        }

        button.setText(null);
        button.setIcon(new ImageIcon(new ImageIcon(getIconPath(field.getPiece(), field.getPiece().getColor()))
                .getImage()
                .getScaledInstance(FIELD_WIDTH, FIELD_WIDTH, Image.SCALE_SMOOTH)));

        return button;
    }
}
