package org.mark.chess.factory;

import org.junit.jupiter.api.Test;
import org.mark.chess.model.Piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InitialPieceFactoryTest {

    private static final int FIELD_B8 = 1;
    private static final int FIELD_C8 = 2;
    private static final int FIELD_D8 = 3;
    private static final int FIELD_E1 = 60;
    private static final int FIELD_F1 = 61;
    private static final int FIELD_G1 = 62;

    @Test
    void getInitialPiece_WhenB8_ThenReturnBlackKnight() {
        Piece initialPiece = InitialPieceFactory.getInitialPiece(FIELD_B8);
        assertNotNull(initialPiece);
        assertEquals("black", initialPiece.getColor().getName());
        assertEquals("knight", initialPiece.getPieceType().getName());
    }

    @Test
    void getInitialPiece_WhenC8_ThenReturnBlackBishop() {
        Piece initialPiece = InitialPieceFactory.getInitialPiece(FIELD_C8);
        assertNotNull(initialPiece);
        assertEquals("black", initialPiece.getColor().getName());
        assertEquals("bishop", initialPiece.getPieceType().getName());
    }

    @Test
    void getInitialPiece_WhenD8_ThenReturnBlackQueen() {
        Piece initialPiece = InitialPieceFactory.getInitialPiece(FIELD_D8);
        assertNotNull(initialPiece);
        assertEquals("black", initialPiece.getColor().getName());
        assertEquals("queen", initialPiece.getPieceType().getName());
    }

    @Test
    void getInitialPiece_WhenE1_ThenReturnWhiteBishop() {
        Piece initialPiece = InitialPieceFactory.getInitialPiece(FIELD_F1);
        assertNotNull(initialPiece);
        assertEquals("white", initialPiece.getColor().getName());
        assertEquals("bishop", initialPiece.getPieceType().getName());
    }

    @Test
    void getInitialPiece_WhenE1_ThenReturnWhiteKing() {
        Piece initialPiece = InitialPieceFactory.getInitialPiece(FIELD_E1);
        assertNotNull(initialPiece);
        assertEquals("white", initialPiece.getColor().getName());
        assertEquals("king", initialPiece.getPieceType().getName());
    }

    @Test
    void getInitialPiece_WhenG1_ThenReturnWhiteKnight() {
        Piece initialPiece = InitialPieceFactory.getInitialPiece(FIELD_G1);
        assertNotNull(initialPiece);
        assertEquals("white", initialPiece.getColor().getName());
        assertEquals("knight", initialPiece.getPieceType().getName());
    }
}