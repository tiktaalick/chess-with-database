package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Button;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

import static org.mark.chess.swing.Button.FIELD_WIDTH_AND_HEIGHT;

@Service
public class ButtonLogic {
    private static final String EXTENSION  = ".png";
    private static final String UNDERSCORE = "_";

    Button initializeButton(Field field) {
        var button = field.getButton();

        if (field.getPiece() == null) {
            return button;
        }

        try {
            button.setText(null);
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

    private static String getIconPath(Piece piece, Color color) {
        return new StringBuilder(color.getName()).append(UNDERSCORE).append(piece.getPieceType().getName()).append(EXTENSION).toString();
    }

    private static ImageIcon getResource(String iconPath) throws IOException {
        return new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(iconPath))));
    }
}
