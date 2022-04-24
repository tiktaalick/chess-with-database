package org.mark.chess.model;

import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.logic.FieldLogic;

import javax.swing.JButton;

public class Field {
    private final FieldLogic             fieldLogic             = new FieldLogic();
    private final BackgroundColorFactory backgroundColorFactory = new BackgroundColorFactory();

    private int         id;
    private String      code;
    private Coordinates coordinates;
    private JButton     button;
    private Piece       piece;
    private boolean     isValidMove;

    public JButton getButton() {
        return button;
    }

    public Field setButton(JButton button) {
        this.button = button;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Field setCode(String code) {
        this.id = fieldLogic.createId(code);
        this.code = code;
        this.coordinates = fieldLogic.createCoordinates(code);

        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Field setCoordinates(Coordinates coordinates) {
        this.id = fieldLogic.createId(coordinates);
        this.code = fieldLogic.createCode(coordinates);
        this.coordinates = coordinates;
        return this;
    }

    public int getId() {
        return id;
    }

    public Field setId(int id) {
        this.id = id;
        this.code = fieldLogic.createCode(id);
        this.coordinates = fieldLogic.createCoordinates(id);

        return this;
    }

    public Piece getPiece() {
        return piece;
    }

    public Field setPiece(Piece piece) {
        this.piece = piece;
        return this;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public Field setValidMove(boolean validMove) {
        isValidMove = validMove;

        if (this.button != null) {
            this.button.setBackground(backgroundColorFactory.getBackgroundColor(this));
        }

        return this;
    }
}
