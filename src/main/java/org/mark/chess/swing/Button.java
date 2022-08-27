package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.model.Field;

import javax.swing.JButton;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public final class Button extends JButton {
    public static final int FIELD_WIDTH_AND_HEIGHT = 75;

    public Button(Board board, Field field) {
        setText(String.valueOf(field.getCode()));
        setBounds(field.getCoordinates().getX() * FIELD_WIDTH_AND_HEIGHT,
                field.getCoordinates().getY() * FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT);
        addActionListener(board);
        addMouseListener(board);
        this.setBackground(BackgroundColorFactory.getBackgroundColor(field));
    }
}