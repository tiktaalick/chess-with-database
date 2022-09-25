package org.mark.chess.repository;

import org.junit.jupiter.api.Test;
import org.mark.chess.piece.InitialPieceRepository;
import org.mark.chess.piece.PieceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InitialPieceRepositoryTest {

    private static final int FIELD_B8 = 1;
    private static final int FIELD_C8 = 2;
    private static final int FIELD_D8 = 3;
    private static final int FIELD_E1 = 60;
    private static final int FIELD_F1 = 61;
    private static final int FIELD_G1 = 62;

    @Test
    void testGetInitialPiece_WhenB8_ThenReturnBlackKnight() {
        PieceType initialPieceType = InitialPieceRepository.getInitialPiece(FIELD_B8);
        assertNotNull(initialPieceType);
        assertEquals("black", initialPieceType.getColor().getName());
        assertEquals("knight", initialPieceType.getName());
    }

    @Test
    void testGetInitialPiece_WhenC8_ThenReturnBlackBishop() {
        PieceType initialPieceType = InitialPieceRepository.getInitialPiece(FIELD_C8);
        assertNotNull(initialPieceType);
        assertEquals("black", initialPieceType.getColor().getName());
        assertEquals("bishop", initialPieceType.getName());
    }

    @Test
    void testGetInitialPiece_WhenD8_ThenReturnBlackQueen() {
        PieceType initialPieceType = InitialPieceRepository.getInitialPiece(FIELD_D8);
        assertNotNull(initialPieceType);
        assertEquals("black", initialPieceType.getColor().getName());
        assertEquals("queen", initialPieceType.getName());
    }

    @Test
    void testGetInitialPiece_WhenE1_ThenReturnWhiteBishop() {
        PieceType initialPieceType = InitialPieceRepository.getInitialPiece(FIELD_F1);
        assertNotNull(initialPieceType);
        assertEquals("white", initialPieceType.getColor().getName());
        assertEquals("bishop", initialPieceType.getName());
    }

    @Test
    void testGetInitialPiece_WhenE1_ThenReturnWhiteKing() {
        PieceType initialPieceType = InitialPieceRepository.getInitialPiece(FIELD_E1);
        assertNotNull(initialPieceType);
        assertEquals("white", initialPieceType.getColor().getName());
        assertEquals("king", initialPieceType.getName());
    }

    @Test
    void testGetInitialPiece_WhenG1_ThenReturnWhiteKnight() {
        PieceType initialPieceType = InitialPieceRepository.getInitialPiece(FIELD_G1);
        assertNotNull(initialPieceType);
        assertEquals("white", initialPieceType.getColor().getName());
        assertEquals("knight", initialPieceType.getName());
    }
}