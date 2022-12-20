package org.mark.chess.game;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.ai.ChessboardValueParameter;
import org.mark.chess.ai.ChessboardValueRulesEngine;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.ChessboardDirector;
import org.mark.chess.board.Field;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveDirector;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.player.Computer;
import org.mark.chess.player.Human;
import org.mark.chess.player.Player;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mark.chess.piece.PieceType.KING;
import static org.mark.chess.piece.PieceType.PAWN;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Contains methods to start a game and alter the state of the game.
 */
@Data
@Accessors(chain = true)
public class Game {

    public static final int MAX_COLOR_VALUE = 255;
    public static final int MIN_COLOR_VALUE = 0;

    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final ChessboardValueRulesEngine CHESSBOARD_VALUE_RULES_ENGINE = new ChessboardValueRulesEngine();
    private static final int                        LEFT_CLICK                    = 1;
    private static final int                        MAXIMUM_SQUARE_ID             = 63;
    private static final int                        RIGHT_CLICK                   = 3;

    private static ChessboardDirector chessboardDirector = new ChessboardDirector();
    private static GameDirector       gameDirector       = new GameDirector();
    private static MoveDirector       moveDirector       = new MoveDirector();

    private Move                    move    = new Move(new Field(null));
    private List<Player>            players = Arrays.asList(new Human(WHITE), new Computer(BLACK));
    private boolean                 inProgress;
    private PlayerColor             humanPlayerColor;
    private Player                  activePlayer;
    private Chessboard              chessboard;
    private Map<Field, List<Field>> allValidFromToCombinations;

    /**
     * Creates a new game.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @param chessboard       The backend representation of a chessboard.
     */
    public Game(PlayerColor humanPlayerColor, Chessboard chessboard) {
        this.inProgress = true;
        this.humanPlayerColor = humanPlayerColor;
        this.activePlayer = players.get(WHITE.ordinal());
        this.chessboard = chessboard;
    }

    /**
     * Creates a new game with a frontend chessboard.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return A new game.
     */
    public static @NotNull Game create(PlayerColor humanPlayerColor) {
        return gameDirector.create(humanPlayerColor);
    }

    /**
     * Starts a new game based on the finished game.
     *
     * @param oldGame The finished game.
     * @return The new game.
     */
    public static @NotNull Game restart(@NotNull Game oldGame) {
        return gameDirector.restart(oldGame.getHumanPlayerColor().getOpposite());
    }

    /**
     * Sets the move director.
     *
     * @param moveDirector The move director.
     */
    public static void setMoveDirector(MoveDirector moveDirector) {
        Game.moveDirector = moveDirector;
    }

    /**
     * Changes the active player.
     */
    public void changeTurn() {
        this.setActivePlayer(players.get(getActivePlayer().getColor().getOpposite().ordinal()));
    }

    /**
     * Creates a list of valid moves.
     *
     * @param from The field from which the chess piece moves.
     * @return A list of valid moves.
     */
    public List<Field> createValidToFields(@NotNull Field from) {
        return from.isActivePlayerField(this)
                ? this
                .getChessboard()
                .getFields()
                .stream()
                .filter(to -> from.getPieceType().isValidMove(new IsValidMoveParameter(chessboard, from, to, false)))
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    /**
     * Marks the valid from-move and all the valid to-moves as valid and gives them nice, bright colors.
     *
     * @param from The field from which the chess piece might be moving.
     */
    public void enableValidMoves(Field from) {
        this.getChessboard().getFields().forEach(field -> field.setValidFrom(false).setValidTo(false).setAttacking(false).setUnderAttack(false));

        List<Field> validMoves = this.createValidToFields(from);
        validMoves.forEach((Field validMove) -> {
            from.setValidFrom(true);
            validMove.setValidTo(true);
        });

        this.setValidMoveColors(from, validMoves, validMoves);
    }

    /**
     * Handles the input from the frontend.
     *
     * @param leftRightClick Indicates which button was clicked.
     * @param buttonId       The frontend button that was clicked.
     * @return The game.
     */
    public Game handleButtonClick(int leftRightClick, int buttonId) {
        var fieldClick = this.getChessboard().getFields().get(buttonId);

        if (leftRightClick == LEFT_CLICK && fieldClick.hasValidTo() && move.isFrom(this, fieldClick)) {
            this.move = moveDirector.performFromMove(this, move, fieldClick);
        } else if (leftRightClick == LEFT_CLICK && fieldClick.hasValidTo() && !move.isFrom(this, fieldClick)) {
            this.move = moveDirector.performToMove(this, move, fieldClick);
        } else if (leftRightClick == RIGHT_CLICK) {
            this.move = moveDirector.performResetMove(this, move);
        } else {
            // Clicks on fields that aren't occupied by the active player are ignored.
        }

        return this;
    }

    /**
     * Resets all valid moves.
     *
     * @return A list of fields.
     */
    public List<Field> resetValidMoves() {
        this.allValidFromToCombinations = new HashMap<>();
        List<Field> allValidToFields = new ArrayList<>();

        this.getChessboard().getFields().forEach((Field from) -> {
            from.setAttacking(false).setUnderAttack(false).setValidFrom(false);

            setValidMoves(this.allValidFromToCombinations, from, allValidToFields);

            if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
                ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
            }
        });

        this.allValidFromToCombinations.forEach((from, validToFields) -> setValidMoveColors(from, validToFields, allValidToFields));

        return allValidToFields;
    }

    /**
     * Determines whether the game is in progress or not.
     *
     * @param kingField A king, which might be in checkmate or stalemate.
     */
    public void setGameProgress(Field kingField) {
        this.setInProgress(this.isInProgress()
                ? (!kingField.isCheckMate() && !kingField.isStaleMate())
                : this.isInProgress());
    }

    /**
     * Colors the field of a king that is in checkmate or stalemate and then marks the game as finished.
     *
     * @param allValidMoves All the valid to-fields together.
     */
    public void setKingFieldColors(Collection<Field> allValidMoves) {
        this.getChessboard().getFields().stream().filter(field -> field.getPieceType() != null).forEach((Field field) -> {
            if (field.getPieceType().getName().equals(KING)) {
                Chessboard.setKingFieldFlags(this, allValidMoves, field);
                this.setGameProgress(field);
            }

            if (!this.isInProgress()) {
                field.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(field));
            }
        });
    }

    /**
     * Gives all valid moves a color.
     *
     * @param from          The field from which the chess piece moves.
     * @param validMoves    The list of valid moves for the chess piece standing on the from field.
     * @param allValidMoves The list of valid moves for all the chess pieces of the active player.
     */
    public void setValidMoveColors(Field from, Collection<Field> validMoves, @NotNull Collection<Field> allValidMoves) {
        this.chessboard.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> createAbsoluteFieldValues(from, to));
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

    private void createAbsoluteFieldValues(Field from, Field to) {
        if (from != null && from.getPieceType() != null) {
            var chessboardAfterMovement = Chessboard.createAfterMovement(this.chessboard, from, to);
            to.setValue(CHESSBOARD_VALUE_RULES_ENGINE
                    .process(new ChessboardValueParameter(chessboardAfterMovement, getActivePlayer().getColor()))
                    .getTotalValue());
            from.setValue(from.getValue() == null
                    ? to.getValue()
                    : Math.max(from.getValue(), to.getValue()));
        }
    }

    private void setValidMoves(Map<Field, List<Field>> allValidFromToCombinations, Field from, @NotNull List<Field> allValidToFields) {
        List<Field> validToFields = createValidToFields(from);
        from.setValidTo(!validToFields.isEmpty());
        from.setValidFrom(from.hasValidTo());
        allValidToFields.addAll(validToFields);

        if (from.isValidFrom()) {
            allValidFromToCombinations.put(from, validToFields);
        }
    }
}
