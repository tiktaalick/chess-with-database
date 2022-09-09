package org.mark.chess.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.logic.BishopLogic;
import org.mark.chess.logic.KingLogic;
import org.mark.chess.logic.KnightLogic;
import org.mark.chess.logic.PawnLogic;
import org.mark.chess.logic.PieceLogic;
import org.mark.chess.logic.QueenLogic;
import org.mark.chess.logic.RookLogic;
import org.mark.chess.model.Piece;
import org.mark.chess.model.PieceTypeLogic;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.BISHOP;
import static org.mark.chess.enums.PieceType.KING;
import static org.mark.chess.enums.PieceType.KNIGHT;
import static org.mark.chess.enums.PieceType.PAWN;
import static org.mark.chess.enums.PieceType.QUEEN;
import static org.mark.chess.enums.PieceType.ROOK;

@ExtendWith(MockitoExtension.class)
class PieceTypeTest {
    @InjectMocks
    private PieceTypeLogic pieceTypeLogic;

    @Mock
    private BishopLogic bishopLogic;

    @Mock
    private KingLogic kingLogic;

    @Mock
    private KnightLogic knightLogic;

    @Mock
    private PawnLogic pawnLogic;

    @Mock
    private QueenLogic queenLogic;

    @Mock
    private RookLogic rookLogic;

    @Test
    void testCreatePiece_WhenPieceTypeBishop_ThenReturnBishop() {
        Piece piece = BISHOP.createPiece(WHITE);
        assertEquals("bishop", piece.getPieceType().getName());
    }

    @Test
    void testCreatePiece_WhenPieceTypeKing_ThenReturnKing() {
        Piece piece = KING.createPiece(WHITE);
        assertEquals("king", piece.getPieceType().getName());
    }

    @Test
    void testCreatePiece_WhenPieceTypeKnight_ThenReturnKnight() {
        Piece piece = KNIGHT.createPiece(WHITE);
        assertEquals("knight", piece.getPieceType().getName());
    }

    @Test
    void testCreatePiece_WhenPieceTypePawn_ThenReturnPawn() {
        Piece piece = PAWN.createPiece(WHITE);
        assertEquals("pawn", piece.getPieceType().getName());
    }

    @Test
    void testCreatePiece_WhenPieceTypeQueen_ThenReturnQueen() {
        Piece piece = QUEEN.createPiece(WHITE);
        assertEquals("queen", piece.getPieceType().getName());
    }

    @Test
    void testCreatePiece_WhenPieceTypeRook_ThenReturnRook() {
        Piece piece = ROOK.createPiece(WHITE);
        assertEquals("rook", piece.getPieceType().getName());
    }

    @Test
    void testGetLogic_WhenPieceTypeBishop_ThenReturnBishop() {
        PieceLogic pieceLogic = BISHOP.getLogic(pieceTypeLogic);
        assertTrue(pieceLogic instanceof BishopLogic);
    }

    @Test
    void testGetLogic_WhenPieceTypeKing_ThenReturnKing() {
        PieceLogic pieceLogic = KING.getLogic(pieceTypeLogic);
        assertTrue(pieceLogic instanceof KingLogic);
    }

    @Test
    void testGetLogic_WhenPieceTypeKnight_ThenReturnKnight() {
        PieceLogic pieceLogic = KNIGHT.getLogic(pieceTypeLogic);
        assertTrue(pieceLogic instanceof KnightLogic);
    }

    @Test
    void testGetLogic_WhenPieceTypePawn_ThenReturnPawn() {
        PieceLogic pieceLogic = PAWN.getLogic(pieceTypeLogic);
        assertTrue(pieceLogic instanceof PawnLogic);
    }

    @Test
    void testGetLogic_WhenPieceTypeQueen_ThenReturnQueen() {
        PieceLogic pieceLogic = QUEEN.getLogic(pieceTypeLogic);
        assertTrue(pieceLogic instanceof QueenLogic);
    }

    @Test
    void testGetLogic_WhenPieceTypeRook_ThenReturnRook() {
        PieceLogic pieceLogic = ROOK.getLogic(pieceTypeLogic);
        assertTrue(pieceLogic instanceof RookLogic);
    }
}