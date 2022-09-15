package org.mark.chess.model;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

public class Field implements Comparable<Field> {
    private static final String  CODE_UNKNOWN         = "xx";
    private static final int     ID_UNKNOWN           = -1;
    private static final Integer VALUE_NOT_CALCULATED = null;

    private int         id            = ID_UNKNOWN;
    private String      code          = CODE_UNKNOWN;
    private Coordinates coordinates   = new Coordinates(ID_UNKNOWN, ID_UNKNOWN);
    private Button      button;
    private Piece       piece;
    private Integer     value         = VALUE_NOT_CALCULATED;
    private Integer     relativeValue = VALUE_NOT_CALCULATED;
    private boolean     isValidFrom;
    private boolean     isValidMove;
    private boolean     isAttacking;
    private boolean     isUnderAttack;
    private boolean     isCheckMate;
    private boolean     isStaleMate;

    public Field(Piece piece) {
        this.piece = piece;
    }

    public Field addChessPiece(Piece piece) {
        return this.setPiece(piece).setButton(Button.initialize(this));
    }

    @Override
    public int compareTo(@NotNull Field other) {
        return this.id - other.id;
    }

    public Button getButton() {
        return button;
    }

    public Field setButton(Button button) {
        this.button = button;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Field setCode(String code) {
        this.id = Coordinates.createId(code);
        this.code = code;
        this.coordinates = Coordinates.create(code);

        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Field setCoordinates(Coordinates coordinates) {
        this.id = Coordinates.createId(coordinates);
        this.code = Coordinates.createCode(coordinates);
        this.coordinates = coordinates;
        return this;
    }

    public int getId() {
        return id;
    }

    public Field setId(int id) {
        this.id = id;
        this.code = Coordinates.createCode(id);
        this.coordinates = Coordinates.create(id);

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

    @Override
    public int hashCode() {
        return Objects.hashCode(id,
                code,
                coordinates,
                button,
                piece,
                value,
                relativeValue,
                isValidFrom,
                isValidMove,
                isAttacking,
                isUnderAttack,
                isCheckMate,
                isStaleMate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var field = (Field) o;
        return id == field.id &&
                isValidFrom == field.isValidFrom &&
                isValidMove == field.isValidMove &&
                isAttacking == field.isAttacking &&
                isUnderAttack == field.isUnderAttack &&
                isCheckMate == field.isCheckMate &&
                isStaleMate == field.isStaleMate &&
                Objects.equal(code, field.code) &&
                Objects.equal(coordinates, field.coordinates) &&
                Objects.equal(button, field.button) &&
                Objects.equal(piece, field.piece) &&
                Objects.equal(value, field.value) &&
                Objects.equal(relativeValue, field.relativeValue);
    }

    public Field initialize(Board board, int id) {
        this.setId(id);
        this.setButton(new Button(board, this));
        board.add(this.getButton());

        return this;
    }

    public boolean isActivePlayerField(Game game) {
        return this.getPiece() != null && this.getPiece().getColor() == game.getCurrentPlayerColor();
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
