package org.mark.chess.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.game.Game;
import org.mark.chess.piece.PieceType;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.mark.chess.piece.PieceType.KING;

/**
 * Contains methods that are Field related.
 */
@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class Field implements Comparable<Field> {

    private static final String                     CODE_UNKNOWN               = "xx";
    private static final int                        ID_UNKNOWN                 = -1;
    private static final Integer                    VALUE_NOT_CALCULATED       = null;
    private static final BackgroundColorRulesEngine backgroundColorRulesEngine = new BackgroundColorRulesEngine();

    private int         id            = ID_UNKNOWN;
    private String      code          = CODE_UNKNOWN;
    private Coordinates coordinates   = new Coordinates(ID_UNKNOWN, ID_UNKNOWN);
    private PieceType   pieceType;
    private Color       backgroundColor;
    private Integer     value         = VALUE_NOT_CALCULATED;
    private Integer     relativeValue = VALUE_NOT_CALCULATED;
    private boolean     isValidFrom;
    private boolean     isValidMove;
    private boolean     isAttacking;
    private boolean     isUnderAttack;
    private boolean     isCheckMate;
    private boolean     isStaleMate;

    public Field(PieceType pieceType) {
        this.pieceType = pieceType;
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

    public boolean isActivePlayerField(Game game) {
        return this.getPieceType() != null && this.getPieceType().getColor() == game.getCurrentPlayerColor();
    }

    public boolean isInCheckNow(Grid grid, boolean isOpponent) {
        if (isOpponent) {
            return false;
        }

        grid.getFields().forEach(field -> field.setUnderAttack(false));

        List<Field> attackers = grid.getFields().stream().filter(isAttacking(grid)).collect(Collectors.toList());

        attackers.forEach((Field field) -> field.setAttackingColors(grid));

        return !attackers.isEmpty();
    }

    public boolean isMovingIntoCheck(Grid grid, Field to, boolean isOpponent) {
        if (isOpponent) {
            return false;
        }

        var gridAfterMovement = Grid.createAfterMovement(grid, this, to);

        List<Field> attackers = gridAfterMovement
                .getFields()
                .stream()
                .filter(opponentField -> opponentField.getPieceType() != null)
                .filter(opponentField -> opponentField.getPieceType().getColor() != this.getPieceType().getColor())
                .filter(opponentField -> isValidMove(gridAfterMovement, opponentField))
                .collect(Collectors.toList());

        return !attackers.isEmpty();
    }

    public boolean isNotAbleToMove(Game game, Collection<Field> allValidMoves) {
        return game.getCurrentPlayerColor() == this.getPieceType().getColor() && game.isInProgress() && allValidMoves.isEmpty();
    }

    public Field setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;

        return this;
    }

    public void setAttackingColors(Grid grid) {
        this.setAttacking(true).setBackgroundColor(backgroundColorRulesEngine.process(this));

        grid.getFields().stream().filter(isUnderAttack(this)).forEach(Field::setUnderAttackColor);
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
        this.setBackgroundColor(backgroundColorRulesEngine.process(this));

        return this;
    }

    @NotNull
    private static Predicate<Field> isUnderAttack(Field attacking) {
        return field -> null != field.getPieceType() &&
                field.getPieceType().getName().equals(KING) &&
                field.getPieceType().getColor() != attacking.getPieceType().getColor();
    }

    private static boolean isValidMove(Grid gridAfterMovement, Field opponentField) {
        return opponentField
                .getPieceType()
                .isValidMove(new IsValidMoveParameter(gridAfterMovement, opponentField, gridAfterMovement.getKingField(), true));
    }

    private static void setUnderAttackColor(Field attackedKingField) {
        attackedKingField.setUnderAttack(true).setBackgroundColor(backgroundColorRulesEngine.process(attackedKingField));
    }

    @NotNull
    private Predicate<Field> isAttacking(Grid grid) {
        return field -> null != field.getPieceType() &&
                field.getPieceType().getColor() != this.getPieceType().getColor() &&
                field.getPieceType().isValidMove(new IsValidMoveParameter(grid, field, this, true));
    }
}
