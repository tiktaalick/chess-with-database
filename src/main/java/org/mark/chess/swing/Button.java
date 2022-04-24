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
public class Button extends JButton {
    public static final int FIELD_WIDTH = 50;

    private final BackgroundColorFactory backgroundColorFactory = new BackgroundColorFactory();

    public Button(Board board, Field field) {
        setText(String.valueOf(field.getCode()));
        setBounds(field.getCoordinates().getX() * FIELD_WIDTH, field.getCoordinates().getY() * FIELD_WIDTH, FIELD_WIDTH, FIELD_WIDTH);
        addActionListener(board);
        addMouseListener(board);
        this.setBackground(backgroundColorFactory.getBackgroundColor(field));
    }
}