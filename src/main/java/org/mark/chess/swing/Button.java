package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public final class Button extends JButton {
    public static final  int    FIELD_WIDTH_AND_HEIGHT = 75;
    private static final String EXTENSION              = ".png";
    private static final String UNDERSCORE             = "_";

    public Button(Board board, Field field) {
        this.setText(String.valueOf(field.getCode()));
        this.setBounds(field.getCoordinates().getX() * FIELD_WIDTH_AND_HEIGHT,
                field.getCoordinates().getY() * FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT);
        this.addActionListener(board);
        this.addMouseListener(board);
        this.setBackground(BackgroundColorFactory.getBackgroundColor(field));
    }

    public static Button initialize(Field field) {
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
