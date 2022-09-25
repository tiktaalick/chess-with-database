package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.piece.PieceType;
import org.mark.chess.player.PlayerColor;

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

    public static final  int                        FIELD_WIDTH_AND_HEIGHT     = 75;
    private static final String                     EXTENSION                  = ".png";
    private static final String                     UNDERSCORE                 = "_";
    private static final BackgroundColorRulesEngine backgroundColorRulesEngine = new BackgroundColorRulesEngine();

    public Button(Board board, Field field) {
        this.setText(String.valueOf(field.getCode()));
        this.setBounds(field.getCoordinates().getX() * FIELD_WIDTH_AND_HEIGHT,
                field.getCoordinates().getY() * FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT);
        this.addActionListener(board);
        this.addMouseListener(board);
        this.setBackground(backgroundColorRulesEngine.process(field));
    }

    public static Button initialize(Field field) {
        var button = field.getButton();

        if (field.getPieceType() == null) {
            return button;
        }

        try {
            button.setText(null);
            button.setIcon(new ImageIcon(getResource(getIconPath(field.getPieceType(), field.getPieceType().getColor()))
                    .getImage()
                    .getScaledInstance(FIELD_WIDTH_AND_HEIGHT, FIELD_WIDTH_AND_HEIGHT, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            button.setText("Error");
            button.setToolTipText(e.getMessage());
        }

        return button;
    }

    private static String getIconPath(PieceType pieceType, PlayerColor color) {
        return color.getName() + UNDERSCORE + pieceType.getName() + EXTENSION;
    }

    private static ImageIcon getResource(String iconPath) throws IOException {
        return new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(iconPath))));
    }
}
