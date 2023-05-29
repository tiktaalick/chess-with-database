package org.mark.chess.board;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.game.Game;
import org.mark.chess.piece.PieceType;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.player.PlayerColor;
import org.springframework.util.CollectionUtils;

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

    @Accessors(fluent = true)
    private boolean hasValidTo;
    private boolean isAttacking;
    private boolean isUnderAttack;
    private boolean isCheckMate;
    private boolean isStaleMate;

    /**
     * Constructor that creates a field based on a piece-type.
     *
     * @param pieceType A piece-type.
     */
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id, code, coordinates);
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

        return id == field.id && Objects.equal(code, field.code) && Objects.equal(coordinates, field.coordinates);
    }

    /**
     * True if the field that contains the piece belongs to the active player.
     *
     * @param activePlayerColor The color with which the active player plays.
     * @return True if the field that contains the piece belongs to the active player.
     */
    public boolean isActivePlayerField(PlayerColor activePlayerColor) {
        return this.getPieceType() != null && this.getPieceType().getColor() == activePlayerColor;
    }

    /**
     * True if the active player is in check now.
     *
     * @param chessboard The chessboard.
     * @param isOpponent Indicates whether the player is the opponent of the active player.
     * @return True if the active player is in check now.
     */
    public boolean isInCheckNow(Chessboard chessboard, boolean isOpponent) {
        if (isOpponent) {
            return false;
        }

        chessboard.getFields().forEach(field -> field.setUnderAttack(false));

        List<Field> attackers = chessboard.getFields().stream().filter(isAttacking(chessboard)).collect(Collectors.toList());

        attackers.forEach((Field field) -> field.setAttackingColors(chessboard));

        return !attackers.isEmpty();
    }

    /**
     * True if the active player moves into check.
     *
     * @param chessboard The chessboard.
     * @param to         The field to which the piece is moved.
     * @param isOpponent Indicates whether the player is the opponent of the active player.
     * @return True if the active player moves into check.
     */
    public boolean isMovingIntoCheck(Chessboard chessboard, Field to, boolean isOpponent) {
        if (isOpponent) {
            return false;
        }

        var gridAfterMovement = Chessboard.createAfterMovement(chessboard, this, to);

        List<Field> attackers = gridAfterMovement
                .getFields()
                .stream()
                .filter(opponentField -> opponentField.getPieceType() != null)
                .filter(opponentField -> opponentField.getPieceType().getColor() != this.getPieceType().getColor())
                .filter(opponentField -> isValidMove(gridAfterMovement, opponentField))
                .collect(Collectors.toList());

        return !attackers.isEmpty();
    }

    /**
     * True if the player is not able to move.
     *
     * @param game          The game.
     * @param allValidMoves A list of all valid moves.
     * @return True if the player is not able to move.
     */
    public boolean isNotAbleToMove(@NotNull Game game, Collection<Field> allValidMoves) {
        return game.getActivePlayer().getColor() == this.getPieceType().getColor() && game.isInProgress() && CollectionUtils.isEmpty(allValidMoves);
    }

    public boolean isValid() {
        return this.id >= Chessboard.MINIMUM_SQUARE_ID && this.id <= Chessboard.MAXIMUM_SQUARE_ID;
    }

    /**
     * Sets whether a piece is attacking another piece from this field.
     *
     * @param isAttacking True if a piece is attacking another piece from this field.
     * @return The field.
     */
    public Field setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;

        return this;
    }

    /**
     * Sets the attacking and under-attack colors.
     *
     * @param chessboard The chessboard.
     */
    public void setAttackingColors(@NotNull Chessboard chessboard) {
        this.setAttacking(true).setBackgroundColor(backgroundColorRulesEngine.process(this));

        chessboard.getFields().stream().filter(isUnderAttack(this)).forEach(Field::setUnderAttackColor);
    }

    /**
     * Sets the field code.
     *
     * @param code The code.
     * @return The field.
     */
    public Field setCode(String code) {
        this.id = Coordinates.createId(code);
        this.code = code;
        this.coordinates = Coordinates.create(code);

        return this;
    }

    /**
     * Sets the field coordinates.
     *
     * @param coordinates The coordinates.
     * @return The field.
     */
    public Field setCoordinates(Coordinates coordinates) {
        this.id = Coordinates.createId(coordinates);
        this.code = Coordinates.createCode(coordinates);
        this.coordinates = coordinates;
        return this;
    }

    /**
     * Sets whether the field has valid fields to move to.
     *
     * @param hasValidTo True if the field has valid fields to move to.
     * @return The field.
     */
    public Field setValidTo(boolean hasValidTo) {
        this.hasValidTo = hasValidTo;
        this.setBackgroundColor(backgroundColorRulesEngine.process(this));

        return this;
    }

    @NotNull
    private static Predicate<Field> isUnderAttack(Field attacking) {
        return field -> null != field.getPieceType() &&
                field.getPieceType().getName().equals(KING) &&
                field.getPieceType().getColor() != attacking.getPieceType().getColor();
    }

    private static boolean isValidMove(Chessboard chessboardAfterMovement, @NotNull Field opponentField) {
        return opponentField
                .getPieceType()
                .isValidMove(new IsValidMoveParameter(chessboardAfterMovement, opponentField, chessboardAfterMovement.getKingField(), true));
    }

    private static void setUnderAttackColor(@NotNull Field attackedKingField) {
        attackedKingField.setUnderAttack(true).setBackgroundColor(backgroundColorRulesEngine.process(attackedKingField));
    }

    @NotNull
    private Predicate<Field> isAttacking(Chessboard chessboard) {
        return field -> null != field.getPieceType() &&
                field.getPieceType().getColor() != this.getPieceType().getColor() &&
                field.getPieceType().isValidMove(new IsValidMoveParameter(chessboard, field, this, true));
    }
}
