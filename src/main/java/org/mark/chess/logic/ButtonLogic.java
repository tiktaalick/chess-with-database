package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

import static org.mark.chess.swing.Button.FIELD_WIDTH_AND_HEIGHT;

public class ButtonLogic {
    private static final String EXTENSION  = ".png";
    private static final String UNDERSCORE = "_";

    public JButton initializeButton(Field field) {
        JButton button = field.getButton();

        if (field.getPiece() == null) {
            return button;
        }

        button.setText(null);
        button.setIcon(new ImageIcon(getResource(getIconPath(field.getPiece(), field.getPiece().getColor()))
                .getImage()
                .getScaledInstance(FIELD_WIDTH_AND_HEIGHT, FIELD_WIDTH_AND_HEIGHT, Image.SCALE_SMOOTH)));

        return button;
    }

    private String getIconPath(Piece piece, Color color) {
        return color.getName() + UNDERSCORE + piece.getPieceType().getName() + EXTENSION;
    }

    private ImageIcon getResource(String iconPath) {
        try {
            return new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(iconPath))));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon();
    }
}
