package org.mark.chess.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.rulesengine.PawnMayBeCapturedEnPassantRulesEngine;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Pawn extends Piece {
    private PawnMayBeCapturedEnPassantRulesEngine pawnMayBeCapturedEnPassantRulesEngine = new PawnMayBeCapturedEnPassantRulesEngine();
    private boolean                               mayBeCapturedEnPassant;

    public Pawn(Color color) {
        super(PieceType.PAWN, color);
    }

    public Pawn setMayBeCapturedEnPassant(Grid grid, Field from, Field to, CheckLogic checkLogic) {
        setMayBeCapturedEnPassant(pawnMayBeCapturedEnPassantRulesEngine.process(new IsValidMoveParameter(grid, from, to, checkLogic, false)));

        return this;
    }

    public Pawn setPawnBeingPromoted(Field from, Field to) {
        setPawnBeingPromoted(from.getPiece().isPawnBeingPromoted() ||
                from.getCoordinates().getY() == from.getPiece().getColor().getOpposite().getBaseline() ||
                to.getCoordinates().getY() == from.getPiece().getColor().getOpposite().getBaseline());

        return this;
    }
}
