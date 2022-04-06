package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;

public class ButtonLogic {
    private static final String IMAGES              = "src/main/resources/images/";
    private static final String UNDERSCORE          = "_";
    private static final String EXTENSION           = ".png";
    private static final double RELATIVE_IMAGE_SIZE = .8;

    public String getIconPath(Piece piece, Color color) {
        return IMAGES + color.getName() + UNDERSCORE + piece.getPieceType().getName() + EXTENSION;
    }

    public int getIconWidth(JButton button) {
        return (int) (button.getWidth() * RELATIVE_IMAGE_SIZE);
    }

    public JButton initializeButton(Game game, int id) {
        Field field = game.getGrid().get(id);
        Piece piece = field.getPiece();
        Color color = piece.getColor();
        JButton button = field.getButton();

        button.setEnabled(setEnabledButton(game, field));
        button.setToolTipText(color.getName() + " " + piece.getPieceType().getName());
        button.setText(null);
        button.setIcon(new ImageIcon(new ImageIcon(getIconPath(piece, color))
                .getImage()
                .getScaledInstance(getIconWidth(button), getIconWidth(button), Image.SCALE_SMOOTH)));

        return button;
    }

    public boolean setEnabledButton(Game game, Field field) {
        return field.getPiece() != null && field.getPiece().getColor() == game.getPlayers().get(game.getCurrentPlayerId()).getColor();
    }
}
