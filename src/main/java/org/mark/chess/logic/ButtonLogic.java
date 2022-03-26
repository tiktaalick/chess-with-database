package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Piece;

import javax.swing.*;

public class ButtonLogic {
    private static final String IMAGES              = "src/main/resources/images/";
    private static final String UNDERSCORE          = "_";
    private static final String EXTENSION           = ".png";
    private static final double RELATIVE_IMAGE_SIZE = .8;

    public String getIconPath(Piece piece, Color color) {
        return IMAGES + color.getName() + UNDERSCORE + piece.pieceType().getName() + EXTENSION;
    }

    public int getIconWidth(JButton button) {
        return (int) (button.getWidth() * RELATIVE_IMAGE_SIZE);
    }
}
