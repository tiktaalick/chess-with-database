package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

import static org.mark.chess.swing.Button.FIELD_WIDTH_AND_HEIGHT;

@Component
public class ButtonLogic {
    private static final String EXTENSION  = ".png";
    private static final String UNDERSCORE = "_";

    private static String getIconPath(Piece piece, Color color) {
        return new StringBuilder(color.getName()).append(UNDERSCORE).append(piece.getPieceType().getName()).append(EXTENSION).toString();
    }

    JButton initializeButton(Field field) {
        JButton button = field.getButton();

        if (field.getPiece() == null) {
            return button;
        }

        button.setText(null);
        try {
            button.setIcon(new ImageIcon(getResource(getIconPath(field.getPiece(), field.getPiece().getColor()))
                    .getImage()
                    .getScaledInstance(FIELD_WIDTH_AND_HEIGHT, FIELD_WIDTH_AND_HEIGHT, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            button.setText("Error");
            button.setToolTipText(e.getMessage());
        }

        return button;
    }

    private ImageIcon getResource(String iconPath) throws IOException {
        return new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(iconPath))));
    }
}
