package org.mark.chess.application;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.piece.general.PieceType;
import org.mark.chess.player.PlayerColor;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mark.chess.swing.FrontendField.FIELD_WIDTH_AND_HEIGHT;

public final class IconFactory {

    private static final String                 EXTENSION  = ".png";
    private static final Map<String, ImageIcon> ICONS      = new HashMap<>();
    private static final String                 UNDERSCORE = "_";

    static {
        for (PlayerColor playerColor : PlayerColor.values()) {
            for (String pieceType : List.of(PieceType.PAWN, PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING)) {
                ICONS.put(playerColor.getName() + " " + pieceType, createImageIcon(pieceType, playerColor));
            }
        }
    }

    private IconFactory() {
    }

    public static Icon getIcon(Field field) {
        if (field.getPieceType() == null) {
            return null;
        }

        return ICONS.get(field.getPieceType().getColor().getName() + " " + field.getPieceType().getName());
    }

    private static String createIconPath(@NotNull String pieceType, @NotNull PlayerColor color) {
        return color.getName() + UNDERSCORE + pieceType + EXTENSION;
    }

    private static @NotNull ImageIcon createImageIcon(String pieceType, PlayerColor playerColor) {
        return new ImageIcon(getResource(createIconPath(pieceType, playerColor))
                .getImage()
                .getScaledInstance(FIELD_WIDTH_AND_HEIGHT, FIELD_WIDTH_AND_HEIGHT, Image.SCALE_SMOOTH));
    }

    private static @NotNull ImageIcon getResource(String iconPath) {
        ImageIcon icon;

        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(iconPath))));
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return icon;
    }
}
