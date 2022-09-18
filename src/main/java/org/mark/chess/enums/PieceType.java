package org.mark.chess.enums;

import org.mark.chess.model.Bishop;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.rulesengine.BishopIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.KingIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.KnightIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.PawnIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.QueenIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.RookIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;

public enum PieceType {
    KING("king", 0) {
        @Override
        public King createPiece(Color color) { return new King(color); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return kingIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    PAWN("pawn", 1) {
        @Override
        public Pawn createPiece(Color color) { return new Pawn(color); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return pawnIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    QUEEN("queen", 9) {
        @Override
        public Queen createPiece(Color color) { return new Queen(color); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return queenIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    ROOK("rook", 5) {
        @Override
        public Rook createPiece(Color color) { return new Rook(color); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return rookIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    BISHOP("bishop", 3) {
        @Override
        public Bishop createPiece(Color color) { return new Bishop(color); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return bishopIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    KNIGHT("knight", 3) {
        @Override
        public Knight createPiece(Color color) { return new Knight(color); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return knightIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    };

    private static final BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine = new BishopIsValidMoveRulesEngine();
    private static final KingIsValidMoveRulesEngine   kingIsValidMoveRulesEngine   = new KingIsValidMoveRulesEngine();
    private static final KnightIsValidMoveRulesEngine knightIsValidMoveRulesEngine = new KnightIsValidMoveRulesEngine();
    private static final PawnIsValidMoveRulesEngine   pawnIsValidMoveRulesEngine   = new PawnIsValidMoveRulesEngine();
    private static final QueenIsValidMoveRulesEngine  queenIsValidMoveRulesEngine  = new QueenIsValidMoveRulesEngine();
    private static final RookIsValidMoveRulesEngine   rookIsValidMoveRulesEngine   = new RookIsValidMoveRulesEngine();

    private final String name;
    private final int    value;

    PieceType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Piece createPiece(Color color) {
        return new Queen(color);
    }

    public String getName() {
        return name;
    }

    public PieceType getNextPawnPromotion() {
        return this == KNIGHT
                ? QUEEN
                : values()[ordinal() + 1];
    }

    public int getValue() {
        return value;
    }

    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return queenIsValidMoveRulesEngine.process(isValidMoveParameter);
    }
}
