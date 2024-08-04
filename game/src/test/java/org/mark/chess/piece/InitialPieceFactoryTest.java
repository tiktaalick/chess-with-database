package org.mark.chess.piece;

import org.junit.jupiter.api.Test;
import org.mark.chess.piece.general.InitialPieceFactory;
import org.mark.chess.piece.general.PieceType;

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
    void testCreateInitialPiece_WhenB8_ThenReturnBlackKnight() {
        PieceType initialPieceType = InitialPieceFactory.createInitialPiece(FIELD_B8);
        assertNotNull(initialPieceType);
        assertEquals("black", initialPieceType.getColor().getName());
        assertEquals("knight", initialPieceType.getName());
    }

    @Test
    void testCreateInitialPiece_WhenC8_ThenReturnBlackBishop() {
        PieceType initialPieceType = InitialPieceFactory.createInitialPiece(FIELD_C8);
        assertNotNull(initialPieceType);
        assertEquals("black", initialPieceType.getColor().getName());
        assertEquals("bishop", initialPieceType.getName());
    }

    @Test
    void testCreateInitialPiece_WhenD8_ThenReturnBlackQueen() {
        PieceType initialPieceType = InitialPieceFactory.createInitialPiece(FIELD_D8);
        assertNotNull(initialPieceType);
        assertEquals("black", initialPieceType.getColor().getName());
        assertEquals("queen", initialPieceType.getName());
    }

    @Test
    void testCreateInitialPiece_WhenE1_ThenReturnWhiteBishop() {
        PieceType initialPieceType = InitialPieceFactory.createInitialPiece(FIELD_F1);
        assertNotNull(initialPieceType);
        assertEquals("white", initialPieceType.getColor().getName());
        assertEquals("bishop", initialPieceType.getName());
    }

    @Test
    void testCreateInitialPiece_WhenE1_ThenReturnWhiteKing() {
        PieceType initialPieceType = InitialPieceFactory.createInitialPiece(FIELD_E1);
        assertNotNull(initialPieceType);
        assertEquals("white", initialPieceType.getColor().getName());
        assertEquals("king", initialPieceType.getName());
    }

    @Test
    void testCreateInitialPiece_WhenG1_ThenReturnWhiteKnight() {
        PieceType initialPieceType = InitialPieceFactory.createInitialPiece(FIELD_G1);
        assertNotNull(initialPieceType);
        assertEquals("white", initialPieceType.getColor().getName());
        assertEquals("knight", initialPieceType.getName());
    }
}
