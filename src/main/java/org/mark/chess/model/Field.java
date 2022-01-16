package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.swing.*;

@Data
@Accessors(fluent = true)
public class Field {
    private int id;
    private Coordinates coordinates;
    private JButton button;
    private Piece piece;

}

