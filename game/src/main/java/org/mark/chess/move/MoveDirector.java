package org.mark.chess.move;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;

/**
 * Contains all the possible moves.
 */
public class MoveDirector {

    protected static MoveBuilder generalMoveBuilder = new MoveBuilder();
    private static   MoveBuilder rookMoveBuilder    = new MoveBuilder();

    public static void setGeneralMoveBuilder(MoveBuilder moveBuilder) {
        generalMoveBuilder = moveBuilder;
    }

    public static void setRookMoveBuilder(MoveBuilder moveBuilder) {
        rookMoveBuilder = moveBuilder;
    }

    /**
     * Performs the from-part of a move.
     *
     * @param game       The game.
     * @param move       The move.
     * @param fieldClick The from-field that has been clicked upon.
     * @return The built move.
     */
    public Move performFromMove(Game game, Move move, Field fieldClick) {
        return generalMoveBuilder.setMove(move).setFrom(fieldClick).enableValidMoves(game).build();
    }

    /**
     * Performs the resetting of a move.
     *
     * @param game The game.
     * @param move The move.
     * @return The built move.
     */
    public Move performResetMove(Game game, Move move) {
        return generalMoveBuilder.setMove(move).setKingFieldColors(game).build();
    }

    /**
     * Performs a rook move during castling.
     *
     * @param chessboard The back-end representation of a chessboard.
     * @param from       The field the rook moves from.
     * @param to         The field the rook moves to.
     * @return The built move.
     */
    public Move performRookMove(Chessboard chessboard, Field from, Field to) {
        return rookMoveBuilder.setMove(new Move(from)).setTo(chessboard, to).resetFrom().build();
    }

    /**
     * Performs the to-part of a move.
     *
     * @param game       The game.
     * @param move       The move.
     * @param fieldClick The to-field that has been clicked upon.
     * @return The built move.
     */
    public Move performToMove(@NotNull Game game, Move move, Field fieldClick) {
        return generalMoveBuilder
                .setMove(move)
                .setTo(game.getChessboard(), fieldClick)
                .setPieceTypeSpecificAttributes(game)
                .moveRookIfCastling(game)
                .changeTurn(game)
                .resetFrom()
                .setKingFieldColors(game)
                .performAiMove(game)
                .build();
    }
}
