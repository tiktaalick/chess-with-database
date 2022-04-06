package org.mark.chess.model;

import org.mark.chess.logic.FieldLogic;

import javax.swing.JButton;

public class Field {
    private final FieldLogic fieldLogic = new FieldLogic();

    private int         id;
    private String      code;
    private Coordinates coordinates;
    private JButton     button;
    private Piece       piece;

    public int getId() {
        return id;
    }

    public Field setId(int id) {
        this.id = id;
        this.code = fieldLogic.createCode(id);

        return this;
    }

    public String getCode() {
        return code;
    }

    public Field setCode(String code) {
        this.code = code;
        this.id = fieldLogic.createId(code);

        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Field setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public JButton getButton() {
        return button;
    }

    public Field setButton(JButton button) {
        this.button = button;
        return this;
    }

    public Piece getPiece() {
        return piece;
    }

    public Field setPiece(Piece piece) {
        this.piece = piece;
        return this;
    }
}
