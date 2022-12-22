package org.mark.chess.board;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.ai.ChessboardValueParameter;
import org.mark.chess.ai.ChessboardValueRulesEngine;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.game.Game;
import org.mark.chess.move.Move;
import org.mark.chess.piece.InitialPieceRepository;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
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

import static org.mark.chess.piece.PieceType.KING;
import static org.mark.chess.piece.PieceType.PAWN;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Contains methods related to the backend representation of a chessboard.
 */
@Getter
@Setter
@Accessors(chain = true)
public final class Chessboard {

    public static final int MAX_COLOR_VALUE            = 255;
    public static final int MIN_COLOR_VALUE            = 0;
    public static final int NUMBER_OF_COLUMNS_AND_ROWS = 8;

    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final ChessboardValueRulesEngine CHESSBOARD_VALUE_RULES_ENGINE = new ChessboardValueRulesEngine();
    private static final int                        MAXIMUM_SQUARE_ID             = 63;

    private Map<Field, List<Field>> allValidFromToCombinations;
    private List<Field>             fields;
    private int                     gridValue;
    private Field                   kingField;
    private Field                   opponentKingField;

    private Chessboard(List<Field> fields) {
        this.fields = new ArrayList<>(fields);
        this.kingField = getKingField(this, WHITE);
        this.opponentKingField = getKingField(this, BLACK);
    }

    private Chessboard(@NotNull Chessboard chessboardBeforeTheMove, @NotNull Field from, Field to) {
        this.fields = chessboardBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> !Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .collect(Collectors.toList());

        List<Field> movementList = chessboardBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .map(field -> Objects.equals(field.getCode(), from.getCode())
                        ? new Field(null).setCoordinates(from.getCoordinates())
                        : new Field(from.getPieceType()).setCoordinates(to.getCoordinates()))
                .collect(Collectors.toList());

        this.fields.addAll(movementList);

        this.kingField = getKingField(this, from.getPieceType().getColor());
        this.opponentKingField = getKingField(this, from.getPieceType().getColor().getOpposite());
    }

    /**
     * Creates a chessboard with chess pieces in their initial positions.
     *
     * @return A chessboard with chess pieces in their initial positions.
     */
    public static @NotNull Chessboard create() {
        return new Chessboard(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .mapToObj(id -> new Field(null).setId(id).setPieceType(InitialPieceRepository.getInitialPiece(id)))
                .collect(Collectors.toList()));
    }

    /**
     * Creates a chessboard with chess pieces in their future positions, based on their current positions and the current move.
     *
     * @param chessboardBeforeTheMove The chessboard with the chess pieces in their positions
     * @param from                    The field from which a piece is moving.
     * @param to                      The field to which a piece is moving.
     * @return A chessboard with chess pieces in their future positions.
     */
    public static @NotNull Chessboard createAfterMovement(Chessboard chessboardBeforeTheMove, Field from, Field to) {
        return new Chessboard(chessboardBeforeTheMove, from, to);
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
        return this
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == coordinates.getX())
                .filter(field -> field.getCoordinates().getY() == coordinates.getY())
                .findAny()
                .orElse(null);
    }

    /**
     * Retrieves a field based on its code.
     *
     * @param code The code of the field.
     * @return The field.
     */
    public Field getField(String code) {
        return this.getFields().stream().filter(field -> field.getCode().equals(code)).findAny().orElse(null);
    }

    /**
     * Retrieves a field that contains a king in the given color.
     *
     * @param chessboard The backend representation of a chessboard.
     * @param color      The color of the king.
     * @return The field that contains a king in the given color.
     */
    public Field getKingField(@NotNull Chessboard chessboard, PlayerColor color) {
        return chessboard
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .filter(field -> field.getPieceType().getColor() == color)
                .filter(field -> field.getPieceType().getName().equals(KING))
                .findAny()
                .orElse(null);
    }

    /**
     * Resets all valid moves.
     *
     * @param move              The current move.
     * @param activePlayerColor The color with which the active player plays.
     * @return A list of fields.
     */
    public List<Field> resetValidMoves(Move move, PlayerColor activePlayerColor) {
        this.allValidFromToCombinations = new HashMap<>();
        List<Field> allValidToFields = new ArrayList<>();

        this.getFields().forEach((Field from) -> {
            from.setAttacking(false).setUnderAttack(false).setValidFrom(false);

            setValidMoves(this.allValidFromToCombinations, from, allValidToFields, activePlayerColor);

            if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
                ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
            }
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

    /**
     * Gives all valid moves a color.
     *
     * @param from              The field from which the chess piece moves.
     * @param validMoves        The list of valid moves for the chess piece standing on the from field.
     * @param allValidMoves     The list of valid moves for all the chess pieces of the active player.
     * @param activePlayerColor
     */
    public void setValidMoveColors(Field from,
            Collection<Field> validMoves,
            @NotNull Collection<Field> allValidMoves,
            PlayerColor activePlayerColor) {
        this.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> createAbsoluteFieldValues(from, to, activePlayerColor));
        createRelativeFieldValues(validMoves, allValidMoves, from);
    }

    private static double calculateRelativeValue(int minValue, int maxValue, Field gridField) {
        return (((double) getCurrentFieldValueComparedToMinimumValue(gridField, minValue)) /
                getMaximumFieldValueComparedToMinimumValue(minValue, maxValue)) * (MAX_COLOR_VALUE - MIN_COLOR_VALUE) + MIN_COLOR_VALUE;
    }

    private static void createRelativeFieldValues(@NotNull Collection<Field> validMoves, Collection<Field> allValidMoves, @NotNull Field from) {
        int minValue = getMinValue(allValidMoves);
        int maxValue = getMaxValue(allValidMoves);
        validMoves.forEach((Field gridField) -> {
            double relativeValue = maxValue - minValue <= 0
                    ? MAX_COLOR_VALUE
                    : calculateRelativeValue(minValue, maxValue, gridField);

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
        return validMoves == null
                ? 0
                : validMoves.stream().mapToInt(Field::getValue).max().orElse(0);
    }

    private static int getMaximumFieldValueComparedToMinimumValue(int minValue, int maxValue) {
        return (maxValue - minValue);
    }

    private static int getMinValue(Collection<Field> validMoves) {
        return validMoves == null
                ? 0
                : validMoves.stream().mapToInt(Field::getValue).min().orElse(0);
    }

    /**
     * Sets the checkmate or stalemate flag on a field that contains a king, if applicable.
     *
     * @param game          The game.
     * @param allValidMoves All the valid moves.
     * @param kingField     The field that contains a king.
     */
    private static void setKingFieldFlags(@NotNull Game game, Collection<Field> allValidMoves, @NotNull Field kingField) {
        boolean isInCheckNow = kingField.isInCheckNow(game.getChessboard(), false);
        boolean isCheckMate = kingField.isCheckMate() || (kingField.isNotAbleToMove(game, allValidMoves) && isInCheckNow);
        boolean isStaleMate = kingField.isStaleMate() || (kingField.isNotAbleToMove(game, allValidMoves) && !isInCheckNow);

        kingField.setCheckMate(isCheckMate).setStaleMate(isStaleMate);
    }

    private void createAbsoluteFieldValues(Field from, Field to, PlayerColor activePlayerColor) {
        if (from != null && from.getPieceType() != null) {
            var chessboardAfterMovement = Chessboard.createAfterMovement(this, from, to);
            to.setValue(CHESSBOARD_VALUE_RULES_ENGINE
                    .process(new ChessboardValueParameter(chessboardAfterMovement, activePlayerColor))
                    .getTotalValue());
            from.setValue(from.getValue() == null
                    ? to.getValue()
                    : Math.max(from.getValue(), to.getValue()));
        }
    }

    /**
     * Creates a list of valid moves.
     *
     * @param from              The field from which the chess piece moves.
     * @param activePlayerColor
     * @return A list of valid moves.
     */
    private List<Field> createValidToFields(@NotNull Field from, PlayerColor activePlayerColor) {
        return from.isActivePlayerField(activePlayerColor)
                ? this
                .getFields()
                .stream()
                .filter(to -> from.getPieceType().isValidMove(new IsValidMoveParameter(this, from, to, false)))
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    private void setValidMoves(Map<Field, List<Field>> allValidFromToCombinations,
            Field from,
            @NotNull List<Field> allValidToFields,
            PlayerColor activePlayerColor) {
        List<Field> validToFields = createValidToFields(from, activePlayerColor);
        from.setValidTo(!validToFields.isEmpty());
        from.setValidFrom(from.hasValidTo());
        allValidToFields.addAll(validToFields);

        if (from.isValidFrom()) {
            allValidFromToCombinations.put(from, validToFields);
        }
    }
}
