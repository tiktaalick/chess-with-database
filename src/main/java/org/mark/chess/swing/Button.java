package org.mark.chess.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class Button extends JButton {
    public static final int FIELD_WIDTH = 50;

    public Button(int x, int y, Color color, String text, ActionListener actionListener, MouseListener mouseListener) {
        setText(text);
        setBounds(x, y, FIELD_WIDTH, FIELD_WIDTH);
        addActionListener(actionListener);
        addMouseListener(mouseListener);
        this.setBackground(color);
    }
}
