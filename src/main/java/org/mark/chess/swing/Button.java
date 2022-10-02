package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
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

import static org.mark.chess.player.PlayerColor.WHITE;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public final class Button extends JButton {

    public static final  int                        FIELD_WIDTH_AND_HEIGHT     = 75;
    private static final String                     EXTENSION                  = ".png";
    private static final int                        MAXIMUM_FIELD_ID           = 63;
    private static final String                     UNDERSCORE                 = "_";
    private static final BackgroundColorRulesEngine backgroundColorRulesEngine = new BackgroundColorRulesEngine();

    private int    id;
    private String iconPath;

    public Button(Board board, @NotNull Field field) {
        this.setText(String.valueOf(field.getCode()));
        this.setBounds(field.getCoordinates().getX() * FIELD_WIDTH_AND_HEIGHT,
                field.getCoordinates().getY() * FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT);
        this.addActionListener(board);
        this.addMouseListener(board);
        this.setBackground(backgroundColorRulesEngine.process(field));
        this.initialize(field);
    }

    public static int createButtonId(PlayerColor humanPlayerColor, int fieldId) {
        return humanPlayerColor == WHITE
                ? fieldId
                : (MAXIMUM_FIELD_ID - fieldId);
    }

    public Button initialize(@NotNull Field field) {
        this.id = field.getId();

        if (field.getPieceType() == null) {
            return this;
        }

        try {
            this.setText(null);
            this.setIcon(new ImageIcon(getResource(createIconPath(field.getPieceType(), field.getPieceType().getColor()))
                    .getImage()
                    .getScaledInstance(FIELD_WIDTH_AND_HEIGHT, FIELD_WIDTH_AND_HEIGHT, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return this;
    }

    public Button reset(Field field) {
        this.setText(field.getCode());
        this.setIcon(null);

        return this;
    }

    private static ImageIcon getResource(String iconPath) throws IOException {
        return new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(iconPath))));
    }

    private String createIconPath(PieceType pieceType, PlayerColor color) {
        this.iconPath = color.getName() + UNDERSCORE + pieceType.getName() + EXTENSION;

        return this.iconPath;
    }
}
