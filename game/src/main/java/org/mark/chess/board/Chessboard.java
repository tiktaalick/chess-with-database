package org.mark.chess.board;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.ai.BestMove;
import org.mark.chess.ai.ChessboardValueParameter;
import org.mark.chess.ai.ChessboardValueRulesEngine;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.game.Game;
import org.mark.chess.move.Move;
import org.mark.chess.piece.general.InitialPieceFactory;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.pawn.Pawn;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.piece.general.PieceType.KING;
import static org.mark.chess.piece.general.PieceType.PAWN;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Contains methods related to the backend representation of a chessboard.
 */
@Getter
@Setter
@Accessors(chain = true)
public final class Chessboard {

    public static final int MAXIMUM_COLOR_VALUE        = 255;
    public static final int MAXIMUM_SQUARE_ID          = 63;
    public static final int MINIMUM_COLOR_VALUE        = 0;
    public static final int MINIMUM_SQUARE_ID          = 0;
    public static final int NUMBER_OF_COLUMNS_AND_ROWS = 8;

    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final ChessboardValueRulesEngine CHESSBOARD_VALUE_RULES_ENGINE = new ChessboardValueRulesEngine();
    private static final int                        ONE_LEVEL                     = 1;

    private List<Field> fields;
    private Field       kingField;
    private Field       opponentKingField;

    // Hoort hier niet thuis
    private int              level    = BestMove.NUMBER_OF_MOVES_AHEAD;
    private Move             fromParentToChildMove;
    private List<Chessboard> children = new ArrayList<>();

    private Chessboard(List<Field> fields) {
        this.fields = new ArrayList<>(fields);
        this.kingField = getKingField(WHITE);
        this.opponentKingField = getKingField(BLACK);
    }

    private Chessboard(@NotNull Chessboard chessboardBeforeTheMove, @NotNull Field from, Field to) {
        this.fields = createFieldsWithoutThePiecesThatHaveMoved(chessboardBeforeTheMove, from, to);
        this.fields.addAll(createFieldsOnlyContainingThePiecesThatHaveMoved(chessboardBeforeTheMove, from, to));
        this.kingField = getKingField(from.getPieceType().getColor());
        this.opponentKingField = getKingField(from.getPieceType().getColor().getOpposite());
        this.level = chessboardBeforeTheMove.level - ONE_LEVEL;
        this.fromParentToChildMove = new Move(from).setTo(to);
    }

    /**
     * Creates a chessboard with chess pieces in their initial positions.
     *
     * @return A chessboard with chess pieces in their initial positions.
     */
    public static @NotNull Chessboard create() {
        return new Chessboard(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .mapToObj(id -> new Field(null).setId(id).setPieceType(InitialPieceFactory.createInitialPiece(id)))
                .collect(Collectors.toList()));
    }

    /**
     * Creates a chessboard without chess pieces.
     *
     * @return A chessboard without chess pieces.
     */
    public static @NotNull Chessboard createEmpty() {
        return new Chessboard(IntStream.rangeClosed(0, MAXIMUM_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()));
    }

    /**
     * Creates a chessboard with chess pieces in their future positions, based on their current positions and the current move.
     *
     * @param from The field from which a piece is moving.
     * @param to   The field to which a piece is moving.
     * @return A chessboard with chess pieces in their future positions.
     */
    public @NotNull Chessboard createOneStepBeyond(Field from, Field to) {
        return new Chessboard(this, from, to);
    }

    /**
     * Marks the valid from-move and all the valid to-moves as valid and gives them nice, bright colors.
     *
     * @param from              The field from which the chess piece might be moving.
     * @param activePlayerColor The color with which the active player plays.
     */
    public void enableValidMoves(Field from, PlayerColor activePlayerColor) {
        this.getFields().forEach(field -> field.setValidFrom(false).setValidTo(false).setAttacking(false).setUnderAttack(false));

        List<Field> validMoves = this.createValidToFields(from, activePlayerColor);

        validMoves.forEach((Field validMove) -> {
            from.setValidFrom(true);
            validMove.setValidTo(true);
        });

        this.setValidMoveColors(from, validMoves, validMoves, activePlayerColor);
    }

    /**
     * Retrieves a field based on its {@link Coordinates}.
     *
     * @param coordinates The {@link Coordinates} of the field.
     * @return The field.
     */
    public Field getField(Coordinates coordinates) {
        return this.getFields().stream().filter(field -> field.getCoordinates().getX() == coordinates.getX()).filter(field -> field
                .getCoordinates()
                .getY() == coordinates.getY()).findAny().orElse(null);
    }

    /**
     * Resets all valid moves.
     *
     * @param move              The current move.
     * @param activePlayerColor The color with which the active player plays.
     * @return A list of fields.
     */
    public List<Field> resetValidMoves(Move move, PlayerColor activePlayerColor) {
        Map<Field, List<Field>> allValidFromToCombinations = new HashMap<>();
        List<Field> allValidToFields = new ArrayList<>();

        this.getFields().forEach((Field from) -> collectAllValidFromToCombinations(move,
                activePlayerColor,
                from,
                allValidFromToCombinations,
                allValidToFields));

        allValidFromToCombinations.forEach((from, validToFields) -> {
            setValidMoveColors(from, validToFields, allValidToFields, activePlayerColor);
            validToFields.forEach(to -> this.children.add(this.createOneStepBeyond(from, to)));
        });

        return allValidToFields;
    }

    /**
     * Colors the field of a king that is in checkmate or stalemate and then marks the game as finished.
     *
     * @param game          The game.
     * @param allValidMoves All the valid to-fields together.
     */
    public void setKingFieldColors(Game game, Collection<Field> allValidMoves) {
        game.getChessboard().getFields().stream().filter(field -> field.getPieceType() != null).forEach((Field field) -> {
            if (field.getPieceType().getName().equals(KING)) {
                setKingFieldFlags(game, allValidMoves, field);
                game.setGameProgress(field);
            }

            if (!game.isInProgress()) {
                field.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(field));
            }
        });
    }

    private static double calculateRelativeValue(int minValue, int maxValue, Field gridField) {
        return (((double) getCurrentFieldValueComparedToMinimumValue(gridField, minValue)) / getMaximumFieldValueComparedToMinimumValue(minValue,
                maxValue)) * (MAXIMUM_COLOR_VALUE - MINIMUM_COLOR_VALUE) + MINIMUM_COLOR_VALUE;
    }

    private static @NotNull List<Field> createFieldsOnlyContainingThePiecesThatHaveMoved(@NotNull Chessboard chessboardBeforeTheMove,
            @NotNull Field from,
            Field to) {
        return chessboardBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .map(field -> Objects.equals(field.getCode(), from.getCode())
                        ? new Field(null).setCoordinates(from.getCoordinates())
                        : new Field(from.getPieceType()).setCoordinates(to.getCoordinates()))
                .collect(Collectors.toList());
    }

    private static @NotNull List<Field> createFieldsWithoutThePiecesThatHaveMoved(@NotNull Chessboard chessboardBeforeTheMove,
            @NotNull Field from,
            Field to) {
        return chessboardBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> !Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .collect(Collectors.toList());
    }

    private static void createRelativeFieldValues(@NotNull Collection<Field> validMoves, Collection<Field> allValidMoves, @NotNull Field from) {
        int minValue = getMinValue(allValidMoves);
        int maxValue = getMaxValue(allValidMoves);
        validMoves.forEach((Field gridField) -> {
            double relativeValue = maxValue - minValue <= 0 ? MAXIMUM_COLOR_VALUE : calculateRelativeValue(minValue, maxValue, gridField);

            gridField.setRelativeValue((int) relativeValue);

            from.setRelativeValue(from.getRelativeValue() == null
                    ? gridField.getRelativeValue()
                    : Math.max(from.getRelativeValue(), gridField.getRelativeValue()));

            gridField.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(gridField));
        });

        from.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(from));
    }

    private static int getCurrentFieldValueComparedToMinimumValue(@NotNull Field gridField, int minValue) {
        return gridField.getValue() - minValue;
    }

    private static int getMaxValue(Collection<Field> validMoves) {
        return validMoves == null ? 0 : validMoves.stream().filter(field -> field.getValue() != null).mapToInt(Field::getValue).max().orElse(0);
    }

    private static int getMaximumFieldValueComparedToMinimumValue(int minValue, int maxValue) {
        return (maxValue - minValue);
    }

    private static int getMinValue(Collection<Field> validMoves) {
        return validMoves == null ? 0 : validMoves.stream().filter(field -> field.getValue() != null).mapToInt(Field::getValue).min().orElse(0);
    }

    private static void setKingFieldFlags(@NotNull Game game, Collection<Field> allValidMoves, @NotNull Field kingField) {
        boolean isInCheckNow = kingField.isInCheckNow(game.getChessboard());
        boolean isCheckMate = kingField.isCheckMate() || (kingField.isNotAbleToMove(game, allValidMoves) && isInCheckNow);
        boolean isStaleMate = kingField.isStaleMate() || (kingField.isNotAbleToMove(game, allValidMoves) && !isInCheckNow);

        kingField.setCheckMate(isCheckMate).setStaleMate(isStaleMate);
    }

    private void collectAllValidFromToCombinations(Move move,
            PlayerColor activePlayerColor,
            Field from,
            Map<Field, List<Field>> allValidFromToCombinations,
            List<Field> allValidToFields) {
        from.setAttacking(false).setUnderAttack(false).setValidFrom(false);

        setValidMoves(allValidFromToCombinations, from, allValidToFields, activePlayerColor);

        if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
            ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
        }
    }

    private void createAbsoluteFieldValues(Field from, Field to, PlayerColor activePlayerColor) {
        if (from != null && from.getPieceType() != null) {
            var chessboardAfterMovement = this.createOneStepBeyond(from, to);
            to.setValue(CHESSBOARD_VALUE_RULES_ENGINE
                    .process(new ChessboardValueParameter(chessboardAfterMovement, activePlayerColor))
                    .getTotalValue());
            from.setValue(from.getValue() == null ? to.getValue() : Math.max(from.getValue(), to.getValue()));
        }
    }

    private List<Field> createValidToFields(@NotNull Field from, PlayerColor activePlayerColor) {
        return from.isActivePlayerField(activePlayerColor) ? this.getFields().stream().filter(to -> from
                .getPieceType()
                .isValidMove(new IsValidMoveParameter(this, from, to, false))).collect(Collectors.toList()) : new ArrayList<>();
    }

    private Field getKingField(PlayerColor color) {
        return this
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .filter(field -> field.getPieceType().getColor() == color)
                .filter(field -> field.getPieceType().getName().equals(KING))
                .findAny()
                .orElse(null);
    }

    private void setValidMoveColors(Field from,
            Collection<Field> validMoves,
            @NotNull Collection<Field> allValidMoves,
            PlayerColor activePlayerColor) {
        this.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> createAbsoluteFieldValues(from, to, activePlayerColor));
        createRelativeFieldValues(validMoves, allValidMoves, from);
    }

    private void setValidMoves(Map<Field, List<Field>> allValidFromToCombinations,
            Field from,
            @NotNull List<Field> allValidToFields,
            PlayerColor activePlayerColor) {
        List<Field> validToFields = createValidToFields(from, activePlayerColor);

        from.setValidTo(!validToFields.isEmpty()).setValidFrom(from.hasValidTo());

        allValidToFields.addAll(validToFields);

        if (from.isValidFrom()) {
            allValidFromToCombinations.put(from, validToFields);
        }
    }
}
