package org.mark.chess.game;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
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

    public static final  int                        MAX_COLOR_VALUE               = 255;
    public static final  int                        MIN_COLOR_VALUE               = 0;
    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final int                        LEFT_CLICK                    = 1;
    private static final int                        MAXIMUM_SQUARE_ID             = 63;
    private static final int                        RIGHT_CLICK                   = 3;

    private static MoveDirector moveDirector = new MoveDirector();

    private Move         move    = new Move(new Field(null));
    private List<Player> players = Arrays.asList(new Human().setColor(WHITE), new Computer().setColor(BLACK));
    private boolean      inProgress;
    private PlayerColor  humanPlayerColor;
    private PlayerColor  currentPlayerColor;
    private Chessboard   chessboard;

    /**
     * Creates a new game.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @param chessboard       The backend representation of a chessboard.
     */
    public Game(PlayerColor humanPlayerColor, Chessboard chessboard) {
        this.inProgress = true;
        this.humanPlayerColor = humanPlayerColor;
        this.currentPlayerColor = WHITE;
        this.chessboard = chessboard;
    }

    /**
     * Creates a new game with a frontend chessboard.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return A new game.
     */
    public static @NotNull Game create(PlayerColor humanPlayerColor) {
        return new Game(humanPlayerColor, Chessboard.create());
    }

    /**
     * Starts a new game based on the finished game.
     *
     * @param oldGame The finished game.
     * @return The new game.
     */
    public static @NotNull Game restart(@NotNull Game oldGame) {
        Game newGame = Game.create(oldGame.getHumanPlayerColor().getOpposite());
        newGame.resetValidMoves();

        return newGame;
    }

    /**
     * Sets the move director.
     *
     * @param moveDirector
     */
    public static void setMoveDirector(MoveDirector moveDirector) {
        Game.moveDirector = moveDirector;
    }

    /**
     * Changes the active player.
     *
     * @return The game.
     */
    public Game changeTurn() {
        return this.setCurrentPlayerColor(this.getCurrentPlayerColor().getOpposite());
    }

    /**
     * Creates a list of valid moves.
     *
     * @param from The field from which the chess piece moves.
     * @return A list of valid moves.
     */
    public List<Field> createValidMoves(@NotNull Field from) {
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
        this.getChessboard().getFields().forEach(field -> field.setValidFrom(false).setValidMove(false).setAttacking(false).setUnderAttack(false));

        List<Field> validMoves = this.createValidMoves(from);
        validMoves.forEach((Field validMove) -> {
            from.setValidFrom(true);
            validMove.setValidMove(true);
        });

        this.setValidMoveColors(this.getChessboard(), from, validMoves, validMoves);
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

        if (leftRightClick == LEFT_CLICK && fieldClick.isValidMove() && move.isFrom(this, fieldClick)) {
            this.move = moveDirector.performFromMove(this, move, fieldClick);
        } else if (leftRightClick == LEFT_CLICK && fieldClick.isValidMove() && !move.isFrom(this, fieldClick)) {
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
     * @return The game.
     */
    public List<Field> resetValidMoves() {
        Map<Field, List<Field>> allValidFromsAndValidMoves = new HashMap<>();
        List<Field> allValidMoves = new ArrayList<>();

        this.getChessboard().getFields().forEach((Field from) -> {
            from.setAttacking(false).setUnderAttack(false).setValidFrom(false);

            setValidMoves(allValidFromsAndValidMoves, allValidMoves, from);

            if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
                ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
            }
        });

        allValidFromsAndValidMoves.forEach((from, validMoves) -> setValidMoveColors(this.getChessboard(), from, validMoves, allValidMoves));

        return allValidMoves;
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
     * @param chessboard    The backend representation of a chessboard.
     * @param from          The field from which the chess piece moves.
     * @param validMoves    The list of valid moves for the chess piece standing on the from field.
     * @param allValidMoves The list of valid moves for all the chess pieces of the active player.
     */
    public void setValidMoveColors(@NotNull Chessboard chessboard,
            Field from,
            Collection<Field> validMoves,
            @NotNull Collection<Field> allValidMoves) {
        chessboard.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> createAbsoluteFieldValues(chessboard, from, to));
        createRelativeFieldValues(validMoves, allValidMoves, from);
    }

    void createAbsoluteFieldValues(Chessboard chessboard, Field from, Field to) {
        if (from != null && from.getPieceType() != null) {
            var gridAfterMovement = Chessboard.createAfterMovement(chessboard, from, to);

            to.setValue(gridAfterMovement.getGridValue());
            from.setValue(from.getValue() == null
                    ? to.getValue()
                    : Math.max(from.getValue(), to.getValue()));
        }
    }

    void createRelativeFieldValues(@NotNull Collection<Field> validMoves, Collection<Field> allValidMoves, @NotNull Field from) {
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

    private static double calculateRelativeValue(int minValue, int maxValue, Field gridField) {
        return (((double) getCurrentFieldValueComparedToMinimumValue(gridField, minValue)) /
                getMaximumFieldValueComparedToMinimumValue(minValue, maxValue)) * (MAX_COLOR_VALUE - MIN_COLOR_VALUE) + MIN_COLOR_VALUE;
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

    private void setValidMoves(Map<Field, List<Field>> allValidFromsAndValidMoves, @NotNull List<Field> allValidMoves, Field from) {
        List<Field> validMoves = createValidMoves(from);
        from.setValidMove(!validMoves.isEmpty());
        from.setValidFrom(from.isValidMove());
        allValidMoves.addAll(validMoves);

        if (from.isValidFrom()) {
            allValidFromsAndValidMoves.put(from, validMoves);
        }
    }
}
