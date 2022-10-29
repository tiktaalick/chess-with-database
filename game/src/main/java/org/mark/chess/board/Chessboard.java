package org.mark.chess.board;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.game.Game;
import org.mark.chess.piece.InitialPieceRepository;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.piece.PieceType.KING;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Contains methods related to the backend representation of a chessboard.
 */
@Getter
@Setter
@Accessors(chain = true)
public final class Chessboard {

    public static final  int NUMBER_OF_COLUMNS_AND_ROWS = 8;
    private static final int MAXIMUM_SQUARE_ID          = 63;

    private List<Field> fields;
    private Field       kingField;
    private Field       opponentKingField;
    private int         gridValue;

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
     * Sets the checkmate or stalemate flag on a field that contains a king, if applicable.
     *
     * @param game          The game.
     * @param allValidMoves All the valid moves.
     * @param kingField     The field that contains a king.
     */
    public static void setKingFieldFlags(@NotNull Game game, Collection<Field> allValidMoves, @NotNull Field kingField) {
        boolean isInCheckNow = kingField.isInCheckNow(game.getChessboard(), false);

        kingField
                .setCheckMate(kingField.isNotAbleToMove(game, allValidMoves) && isInCheckNow)
                .setStaleMate(kingField.isNotAbleToMove(game, allValidMoves) && !isInCheckNow);
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
}
