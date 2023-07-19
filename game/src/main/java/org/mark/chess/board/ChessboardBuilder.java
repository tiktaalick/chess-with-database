package org.mark.chess.board;

import org.mark.chess.move.Move;
import org.mark.chess.piece.Pawn;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.List;

import static org.mark.chess.player.PlayerColor.WHITE;

public class ChessboardBuilder {

    private static final Move NOT_DURING_A_MOVE = new Move(new Field(new Pawn(WHITE)));

    private Chessboard       chessboard;
    private List<Chessboard> children;

    public Chessboard build() {
        return chessboard;
    }

    public List<Chessboard> buildChildren() {
        return children;
    }

    public ChessboardBuilder createChessboard() {
        this.chessboard = Chessboard.create();

        return this;
    }

    public ChessboardBuilder createChildren() {
        this.children = new ArrayList<>();

        this.chessboard
                .getAllValidFromToCombinations()
                .forEach((from, toList) -> toList.forEach(to -> children.add(Chessboard.createAfterMovement(this.chessboard, from, to))));

        return this;
    }

    public ChessboardBuilder resetValidMoves(PlayerColor activePlayerColor) {
        this.chessboard.resetValidMoves(NOT_DURING_A_MOVE, activePlayerColor);

        return this;
    }

    public ChessboardBuilder setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;

        return this;
    }
}
