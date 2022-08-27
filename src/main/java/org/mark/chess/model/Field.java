package org.mark.chess.model;

import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.logic.ButtonLogic;
import org.mark.chess.logic.FieldLogic;

import javax.swing.JButton;

public class Field {
    private static final String  CODE_UNKNOWN         = "xx";
    private static final int     ID_UNKNOWN           = -1;
    private static final Integer VALUE_NOT_CALCULATED = null;

    private final FieldLogic fieldLogic = new FieldLogic(new ButtonLogic());

    private int         id            = ID_UNKNOWN;
    private String      code          = CODE_UNKNOWN;
    private Coordinates coordinates;
    private JButton     button;
    private Piece       piece;
    private Integer     value         = VALUE_NOT_CALCULATED;
    private Integer     relativeValue = VALUE_NOT_CALCULATED;
    private boolean     isValidFrom;
    private boolean     isValidMove;
    private boolean     isAttacking;
    private boolean     isUnderAttack;
    private boolean     isCheckMate;
    private boolean     isStaleMate;

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

    public Integer getRelativeValue() {
        return relativeValue;
    }

    public Field setRelativeValue(Integer relativeValue) {
        this.relativeValue = relativeValue;
        return this;
    }

    public Integer getValue() {
        return value;
    }

    public Field setValue(Integer value) {
        this.value = value;
        return this;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public Field setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
        return this;
    }

    public boolean isCheckMate() {
        return isCheckMate;
    }

    public Field setCheckMate(boolean isCheckMate) {
        this.isCheckMate = isCheckMate;
        return this;
    }

    public boolean isStaleMate() {
        return isStaleMate;
    }

    public Field setStaleMate(boolean isStaleMate) {
        this.isStaleMate = isStaleMate;
        return this;
    }

    public boolean isUnderAttack() {
        return isUnderAttack;
    }

    public Field setUnderAttack(boolean underAttack) {
        isUnderAttack = underAttack;
        return this;
    }

    public boolean isValidFrom() {
        return isValidFrom;
    }

    public Field setValidFrom(boolean isValidFrom) {
        this.isValidFrom = isValidFrom;
        return this;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public Field setValidMove(boolean isValidMove) {
        this.isValidMove = isValidMove;

        if (this.button != null) {
            this.button.setBackground(BackgroundColorFactory.getBackgroundColor(this));
        }

        return this;
    }
}
