package org.mark.chess.enums;

import org.mark.chess.logic.BishopLogic;
import org.mark.chess.logic.KingLogic;
import org.mark.chess.logic.KnightLogic;
import org.mark.chess.logic.PawnLogic;
import org.mark.chess.logic.PieceLogic;
import org.mark.chess.logic.QueenLogic;
import org.mark.chess.logic.RookLogic;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.PieceTypeLogic;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.rulesengine.BishopIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.KnightIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.QueenIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.RookIsValidMoveRulesEngine;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;

public enum PieceType {
    KING("king", 0) {
        @Override
        public King createPiece(Color color) { return new King(color); }

        @Override
        public KingLogic getLogic(PieceTypeLogic pieceTypeLogic) { return pieceTypeLogic.getKingLogic(); }
    },
    PAWN("pawn", 1) {
        @Override
        public Pawn createPiece(Color color) { return new Pawn(color); }

        @Override
        public PawnLogic getLogic(PieceTypeLogic pieceTypeLogic) { return pieceTypeLogic.getPawnLogic(); }
    },
    QUEEN("queen", 9) {
        @Override
        public Queen createPiece(Color color) { return new Queen(color); }

        @Override
        public QueenLogic getLogic(PieceTypeLogic pieceTypeLogic) { return pieceTypeLogic.getQueenLogic(); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return queenIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    ROOK("rook", 5) {
        @Override
        public Rook createPiece(Color color) { return new Rook(color); }

        @Override
        public RookLogic getLogic(PieceTypeLogic pieceTypeLogic) { return pieceTypeLogic.getRookLogic(); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return rookIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    BISHOP("bishop", 3) {
        @Override
        public Bishop createPiece(Color color) { return new Bishop(color); }

        @Override
        public BishopLogic getLogic(PieceTypeLogic pieceTypeLogic) { return pieceTypeLogic.getBishopLogic(); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return bishopIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    },
    KNIGHT("knight", 3) {
        @Override
        public Knight createPiece(Color color) { return new Knight(color); }

        @Override
        public KnightLogic getLogic(PieceTypeLogic pieceTypeLogic) { return pieceTypeLogic.getKnightLogic(); }

        @Override
        public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
            return knightIsValidMoveRulesEngine.process(isValidMoveParameter);
        }
    };

    private static final BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine = new BishopIsValidMoveRulesEngine();
    private static final KnightIsValidMoveRulesEngine knightIsValidMoveRulesEngine = new KnightIsValidMoveRulesEngine();
    private static final QueenIsValidMoveRulesEngine  queenIsValidMoveRulesEngine  = new QueenIsValidMoveRulesEngine();
    private static final RookIsValidMoveRulesEngine   rookIsValidMoveRulesEngine   = new RookIsValidMoveRulesEngine();
    private final        String                       name;
    private final        int                          value;

    PieceType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Piece createPiece(Color color) {
        return new Queen(color);
    }

    public PieceLogic getLogic(PieceTypeLogic pieceTypeLogic) {
        return QUEEN.getLogic(pieceTypeLogic);
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
