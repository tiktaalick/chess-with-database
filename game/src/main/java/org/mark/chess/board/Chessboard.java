package org.mark.chess.board;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.ai.ChildrenBuilder;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.game.Game;
import org.mark.chess.game.GameService;
import org.mark.chess.move.Move;
import org.mark.chess.piece.general.InitialPieceFactory;
import org.mark.chess.player.PlayerColor;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static org.mark.chess.piece.general.PieceType.KING;
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

    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE     = new BackgroundColorRulesEngine();
    private static final GameService                GAME_SERVICE                      = new GameService();
    private static final Logger                     LOGGER                            = Logger.getLogger(Chessboard.class.getName());
    private static final int                        ONE_WHITE_MOVE_AND_ONE_BLACK_MOVE = 2;

    private ChildrenBuilder         childrenBuilder            = new ChildrenBuilder();
    private Map<Field, List<Field>> allValidFromToCombinations = new HashMap<>();
    private List<Field>             allValidToFields           = new ArrayList<>();
    private Set<Chessboard>         children                   = new HashSet<>();
    private PlayerColor             childrenActivePlayerColor  = WHITE;
    private List<Field>             fields;
    private Move                    fromParentToChildMove;
    private AtomicInteger           bestMoveValue              = new AtomicInteger();
    private Field                   kingField;
    private int                     numberOfMovesToLookAhead   = ONE_WHITE_MOVE_AND_ONE_BLACK_MOVE;
    private Field                   opponentKingField;
    private Chessboard              parent;

    private Chessboard(List<Field> fields) {
        this.fields = new ArrayList<>(fields);
        this.kingField = getKingField(WHITE);
        this.opponentKingField = getKingField(BLACK);

        LOGGER.info(() -> "New chessboard created: " + this.hashCode());
    }

    private Chessboard(@NotNull Chessboard chessboardBeforeTheMove, @NotNull Field from, Field to) {
        this.fields = createFields(chessboardBeforeTheMove, from, to);
        this.childrenActivePlayerColor = chessboardBeforeTheMove.getChildrenActivePlayerColor().getOpposite();
        this.numberOfMovesToLookAhead = chessboardBeforeTheMove.getNumberOfMovesToLookAhead() - 1;
        this.kingField = getKingField(from.getPieceType().getColor());
        this.opponentKingField = getKingField(from.getPieceType().getColor().getOpposite());
        this.fromParentToChildMove = new Move(from).setTo(to);
        this.parent = chessboardBeforeTheMove;
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
                .toList());
    }

    /**
     * Creates a chessboard without chess pieces.
     *
     * @return A chessboard without chess pieces.
     */
    public static @NotNull Chessboard createEmpty() {
        return new Chessboard(IntStream.rangeClosed(0, MAXIMUM_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).toList());
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

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || this.getClass() != that.getClass()) {
            return false;
        }

        var diff = "";

        if (this == that || Objects.equals(this.toString(), that.toString())) {
            return true;
        } else {
            diff = createDiffForLogging((Chessboard) that);
        }

        LOGGER.log(Level.INFO, diff);

        return false;
    }

    @Override
    public String toString() {
        return this.fields
                .stream()
                .filter(field -> Objects.nonNull(field.getPieceType()))
                .sorted(Comparator.comparingInt(field -> field.getCoordinates().getX() +
                        1 +
                        field.getCoordinates().getY() * NUMBER_OF_COLUMNS_AND_ROWS))
                .map(field -> field
                        .getCode()
                        .concat(":")
                        .concat(Optional
                                .ofNullable(field.getPieceType())
                                .map(pieceType -> pieceType.getColor().getName() + " " + pieceType.getName())
                                .orElse("")))
                .collect(Collectors.joining(" "));
    }

    public synchronized void setBestMove(Move fromParentToChildMove, AtomicInteger bestMoveValue) {
        var log = "fromParentToChildMove=" + fromParentToChildMove + "; bestMoveValue=" + bestMoveValue;
        logBestMove(log, "this.getFromParentToChildMove()=" + this.getFromParentToChildMove());
//        logBestMove(log, "parent.getFromParentToChildMove()=" + parent.getFromParentToChildMove());

        List<AbstractMap.SimpleEntry<Field, Field>> fromToWithMaxValues = this.allValidFromToCombinations
                .entrySet()
                .stream()
                .reduce((max, fromTo) -> fromTo.getKey().getRelativeValueInteger() > max.getKey().getRelativeValueInteger() ? fromTo : max)
                .stream()
                .map(entrySet -> {
                    Field toField = entrySet
                            .getValue()
                            .stream()
                            .reduce((max, to) -> to.getRelativeValueInteger() > max.getRelativeValueInteger() ? to : max)
                            .orElse(new Field(null));
                    return new AbstractMap.SimpleEntry<>(entrySet.getKey(), toField);
                })
                .toList();

        logBestMove(log, fromToWithMaxValues.toString());

//        this.allValidFromToCombinations.forEach((key, value) -> {
//            if (fromParentToChildMove != null &&
//                    key.getCode().equals(fromParentToChildMove.getFrom().getCode()) &&
//                    isBetterValue(key, bestMoveValue.intValue())) {
//                key.setRelativeValue(isBetterValue()bestMoveValue);
//            }
//        });
//
////        Pair<Field, Field> bestFromToCombination = ;
//        this.allValidFromToCombinations.entrySet().stream().peek(entry -> {
//            if (fromParentToChildMove != null && entry.getKey().getCode().equals(fromParentToChildMove.getFrom().getCode())) {
//                entry.getKey().setRelativeValue(bestMoveValue);
//            }
//        }).filter(entry -> entry.getKey().getRelativeValue() != null).reduce(new Field(null), entry -> getFieldWithBestRelativeValue());
//
//        Field bestFrom = this.allValidFromToCombinations
//                .keySet()
//                .stream()
//                .map(from -> fromParentToChildMove != null && from.getCode().equals(fromParentToChildMove.getFrom().getCode())
//                             ? from.setRelativeValue(bestMoveValue)
//                             : from)
//                .filter(from -> from.getRelativeValue() != null)
//                .reduce(new Field(null), Chessboard::getFieldWithBestRelativeValue);
//        Field bestTo = this.allValidFromToCombinations
//                .entrySet()
//                .stream()
//                .filter(entry -> entry.getKey().getCode().equals(bestFrom.getCode()))
//                .flatMap(entry -> entry.getValue().stream())
//                .filter(to -> to.getRelativeValue() != null)
//                .reduce(new Field(null), Chessboard::getFieldWithBestRelativeValue);
//        logBestMove(log, "bestFrom=" + bestFrom + " -> bestTo=" + bestTo);
//        if (bestFrom.getRelativeValue() != null && isBetterValue(bestFrom, this.bestMoveValue.intValue())) {
//            logBestMove(log, "isBetterValue=true");
//            var bestMoveValueOld = this.bestMoveValue;
//            this.bestMoveValue = bestFrom.getRelativeValue();
//            logBestMove(log, "this.bestMoveValue=" + bestMoveValueOld + " -> " + this.bestMoveValue);
//            if (this.parent != null) {
//                logBestMove(log,
//                        "this.parent.getFromParentToChildMove()=" +
//                                this.parent.getFromParentToChildMove() +
//                                "; this.parent" +
//                                ".bestMoveValue=" +
//                                this.parent.bestMoveValue);
//                this.parent.setBestMove(this.fromParentToChildMove, this.bestMoveValue);
//            }
//        }
    }

    /**
     * Colors the field of a king that is in checkmate or stalemate and then marks the game as finished.
     *
     * @param game The game.
     */
    public void setKingFieldColors(Game game) {
        game.getChessboard().getFields().stream().filter(field -> field.getPieceType() != null).forEach((Field field) -> {
            if (field.getPieceType().getName().equals(KING)) {
                setKingFieldFlags(game, field);
                game.setGameProgress(field);
            }

            if (!game.isInProgress()) {
                field.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(field));
            }
        });
    }

    /**
     * Resets all valid moves.
     *
     * @param move              The current move.
     * @param activePlayerColor The color with which the active player plays.
     */
    public void setValidFromFields(Move move, PlayerColor activePlayerColor) {
        if (this.numberOfMovesToLookAhead > 0) {
            LOGGER.info(() -> "numberOfMovesToLookAhead=" +
                    numberOfMovesToLookAhead +
                    "; Chessboard " +
                    this.hashCode() +
                    " for which children will be built. " +
                    (this.parent == null ? "No parent." : ("Parent is " + this.parent.hashCode())));
            this.children = childrenBuilder.init(this, move, activePlayerColor).setBackgroundColors().buildChildren();
            this.children
//                    .parallelStream()
.forEach(child -> child.setValidFromFields(new Move(new Field(null)), child.getChildrenActivePlayerColor()));
        } else {
            LOGGER.info(() -> "numberOfMovesToLookAhead=" +
                    numberOfMovesToLookAhead +
                    "; Chessboard " +
                    this.hashCode() +
                    " for which no children will be built. Parent is " +
                    this.parent.hashCode());
            childrenBuilder.init(this, move, activePlayerColor).calculateFieldValues(null);
        }
    }

    /**
     * Marks the valid from-move and all the valid to-moves as valid and gives them nice, bright colors.
     *
     * @param move The move that the player might be performing.
     */
    public void setValidToFields(Move move) {
        childrenBuilder.resetToAttributes(move.getFrom().getCode()).calculateFieldValues(move.getFrom().getCode()).setBackgroundColors();
    }

    private static List<Field> createFields(@NotNull Chessboard chessboardBeforeTheMove, @NotNull Field from, Field to) {
        List<Field> fields = createFieldsWithoutThePiecesThatHaveMoved(chessboardBeforeTheMove, from, to);
        fields.addAll(createFieldsOnlyContainingThePiecesThatHaveMoved(chessboardBeforeTheMove, from, to));
        return fields.stream().map(Field::createClone).toList();
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
                .toList();
    }

    private static @NotNull List<Field> createFieldsWithoutThePiecesThatHaveMoved(@NotNull Chessboard chessboardBeforeTheMove,
            @NotNull Field from,
            Field to) {
        return chessboardBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> !Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static String createWordDiff(String prefix, Chessboard chessboard, String[] words, int index) {
        String thisPosition = words[index].concat(" ").concat(words[index + 1]);

        if (!chessboard.toString().contains(thisPosition)) {
            return prefix + thisPosition;
        }

        return "";
    }

    private static Field getFieldWithBestRelativeValue(Field fieldA, Field fieldB) {
//        var log = "fieldA=" + fieldA + "; fieldB=" + fieldB;
//        logBestMove(log, "");
        var fieldWithPieceType = Stream.of(fieldA, fieldB).filter(field -> Optional.ofNullable(field).map(Field::getPieceType).isPresent()).findAny();

        if (fieldWithPieceType.isEmpty()) {
//            logBestMove(log, "fieldWithPieceType.isEmpty()");
            return fieldA;
        }

//        logBestMove(log, "doReturnMaxValue(fieldWithPieceType.get())=" + doReturnMaxValue(fieldWithPieceType.get()));
        if (isMaxValueBetter(fieldWithPieceType.get())) {
            return fieldA.getRelativeValueInteger() > fieldB.getRelativeValueInteger() ? fieldA : fieldB;
        } else {
            return fieldA.getRelativeValueInteger() < fieldB.getRelativeValueInteger() ? fieldA : fieldB;
        }
    }

    private static boolean isMaxValueBetter(Field field) {
        return field.getPieceType().getColor() == WHITE;
    }

    private static synchronized void logBestMove(String log, String newLog) {
        log = log + "; " + newLog;
        LOGGER.info(log);
    }

    private String createDiffForLogging(Chessboard that) {
        String[] theseWords = this.toString().split(" ");
        String[] thoseWords = that.toString().split(" ");

        return IntStream
                .range(0, min(theseWords.length, thoseWords.length))
                .filter(index -> index % 2 == 0)
                .mapToObj(index -> createWordDiff("this=", that, theseWords, index)
                        .concat(" ")
                        .concat(createWordDiff("that=", this, thoseWords, index)))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.joining(" "));
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

    private boolean isBetterValue(@NotNull Field bestFrom, int value) {
        return isMaxValueBetter(bestFrom) ? (bestFrom.getRelativeValueInteger() > value) : (bestFrom.getRelativeValueInteger() < value);
    }

    private void setKingFieldFlags(@NotNull Game game, @NotNull Field kingField) {
        boolean isInCheckNow = kingField.isInCheckNow(game.getChessboard());
        boolean isCheckMate = kingField.isCheckMate() || (kingField.isNotAbleToMove(game, this.children) && isInCheckNow);
        boolean isStaleMate = kingField.isStaleMate() || (kingField.isNotAbleToMove(game, this.children) && !isInCheckNow);

        kingField.setCheckMate(isCheckMate).setStaleMate(isStaleMate);
    }
}
