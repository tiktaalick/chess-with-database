package org.mark.chess.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Piece;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.BISHOP;
import static org.mark.chess.enums.PieceType.KING;
import static org.mark.chess.enums.PieceType.KNIGHT;
import static org.mark.chess.enums.PieceType.PAWN;
import static org.mark.chess.enums.PieceType.QUEEN;
import static org.mark.chess.enums.PieceType.ROOK;

@ExtendWith(MockitoExtension.class)
class PieceTypeTest {
//    @InjectMocks
//    private PieceTypeLogic pieceTypeLogic;
//
//    @Test
//    void testCreatePiece_WhenPieceTypeBishop_ThenReturnBishop() {
//        Piece piece = BISHOP.createPiece(WHITE);
//        assertEquals("bishop", piece.getPieceType().getName());
//    }
//
//    @Test
//    void testCreatePiece_WhenPieceTypeKing_ThenReturnKing() {
//        Piece piece = KING.createPiece(WHITE);
//        assertEquals("king", piece.getPieceType().getName());
//    }
//
//    @Test
//    void testCreatePiece_WhenPieceTypeKnight_ThenReturnKnight() {
//        Piece piece = KNIGHT.createPiece(WHITE);
//        assertEquals("knight", piece.getPieceType().getName());
//    }
//
//    @Test
//    void testCreatePiece_WhenPieceTypePawn_ThenReturnPawn() {
//        Piece piece = PAWN.createPiece(WHITE);
//        assertEquals("pawn", piece.getPieceType().getName());
//    }
//
//    @Test
//    void testCreatePiece_WhenPieceTypeQueen_ThenReturnQueen() {
//        Piece piece = QUEEN.createPiece(WHITE);
//        assertEquals("queen", piece.getPieceType().getName());
//    }
//
//    @Test
//    void testCreatePiece_WhenPieceTypeRook_ThenReturnRook() {
//        Piece piece = ROOK.createPiece(WHITE);
//        assertEquals("rook", piece.getPieceType().getName());
//    }
}