package org.mark.chess.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.PieceType;
import org.mark.chess.logic.BishopLogic;
import org.mark.chess.logic.KingLogic;
import org.mark.chess.logic.KnightLogic;
import org.mark.chess.logic.PawnLogic;
import org.mark.chess.logic.PieceLogic;
import org.mark.chess.logic.QueenLogic;
import org.mark.chess.logic.RookLogic;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PieceLogicFactoryTest {
    @InjectMocks
    private PieceLogicFactory pieceLogicFactory;

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
    void getLogic_WhenPieceTypeBishop_ThenReturnBishop() {
        PieceLogic pieceLogic = pieceLogicFactory.getLogic(PieceType.BISHOP);
        assertTrue(pieceLogic instanceof BishopLogic);
    }

    @Test
    void getLogic_WhenPieceTypeKing_ThenReturnKing() {
        PieceLogic pieceLogic = pieceLogicFactory.getLogic(PieceType.KING);
        assertTrue(pieceLogic instanceof KingLogic);
    }

    @Test
    void getLogic_WhenPieceTypeKnight_ThenReturnKnight() {
        PieceLogic pieceLogic = pieceLogicFactory.getLogic(PieceType.KNIGHT);
        assertTrue(pieceLogic instanceof KnightLogic);
    }

    @Test
    void getLogic_WhenPieceTypePawn_ThenReturnPawn() {
        PieceLogic pieceLogic = pieceLogicFactory.getLogic(PieceType.PAWN);
        assertTrue(pieceLogic instanceof PawnLogic);
    }

    @Test
    void getLogic_WhenPieceTypeQueen_ThenReturnQueen() {
        PieceLogic pieceLogic = pieceLogicFactory.getLogic(PieceType.QUEEN);
        assertTrue(pieceLogic instanceof QueenLogic);
    }

    @Test
    void getLogic_WhenPieceTypeRook_ThenReturnRook() {
        PieceLogic pieceLogic = pieceLogicFactory.getLogic(PieceType.ROOK);
        assertTrue(pieceLogic instanceof RookLogic);
    }
}