package org.mark.chess.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
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

    public int getId() {
        return id;
    }

    public Field setId(int id) {
        this.id = id;
        this.code = Coordinates.createCode(id);
        this.coordinates = Coordinates.create(id);

        return this;
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

    public Field setCode(String code) {
        this.id = Coordinates.createId(code);
        this.code = code;
        this.coordinates = Coordinates.create(code);

        return this;
    }

    public Field setCoordinates(Coordinates coordinates) {
        this.id = Coordinates.createId(coordinates);
        this.code = Coordinates.createCode(coordinates);
        this.coordinates = coordinates;
        return this;
    }

    public Field setValidMove(boolean isValidMove) {
        this.isValidMove = isValidMove;

        if (this.button != null) {
            this.button.setBackground(BackgroundColorFactory.getBackgroundColor(this));
        }

        return this;
    }
}
