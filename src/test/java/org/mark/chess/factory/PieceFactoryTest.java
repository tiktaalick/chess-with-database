package org.mark.chess.factory;

import org.junit.jupiter.api.Test;
import org.mark.chess.enums.PieceType;
import org.mark.chess.model.Piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.enums.Color.WHITE;

class PieceFactoryTest {

    @Test
    void getPiece_WhenPieceTypeBishop_ThenReturnBishop() {
        Piece piece = PieceFactory.getPiece(PieceType.BISHOP, WHITE);
        assertEquals("bishop", piece.getPieceType().getName());
    }

    @Test
    void getPiece_WhenPieceTypeKing_ThenReturnKing() {
        Piece piece = PieceFactory.getPiece(PieceType.KING, WHITE);
        assertEquals("king", piece.getPieceType().getName());
    }

    @Test
    void getPiece_WhenPieceTypeKnight_ThenReturnKnight() {
        Piece piece = PieceFactory.getPiece(PieceType.KNIGHT, WHITE);
        assertEquals("knight", piece.getPieceType().getName());
    }

    @Test
    void getPiece_WhenPieceTypePawn_ThenReturnPawn() {
        Piece piece = PieceFactory.getPiece(PieceType.PAWN, WHITE);
        assertEquals("pawn", piece.getPieceType().getName());
    }

    @Test
    void getPiece_WhenPieceTypeQueen_ThenReturnQueen() {
        Piece piece = PieceFactory.getPiece(PieceType.QUEEN, WHITE);
        assertEquals("queen", piece.getPieceType().getName());
    }

    @Test
    void getPiece_WhenPieceTypeRook_ThenReturnRook() {
        Piece piece = PieceFactory.getPiece(PieceType.ROOK, WHITE);
        assertEquals("rook", piece.getPieceType().getName());
    }
}