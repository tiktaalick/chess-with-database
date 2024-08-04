package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.application.IconFactory;
import org.mark.chess.board.Field;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.player.PlayerColor;

import javax.swing.JButton;

import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Contains front-end field related methods.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public final class FrontendField extends JButton {

    public static final  int                        FIELD_WIDTH_AND_HEIGHT        = 75;
    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final int                        MAXIMUM_FIELD_ID              = 63;

    private int id;

    /**
     * Constructor for the front-end field.
     *
     * @param frontendChessboard The front-end chessboard.
     * @param field              A back-end field.
     */
    public FrontendField(FrontendChessboard frontendChessboard, @NotNull Field field) {
        this.setText(String.valueOf(field.getCode()));
        this.setBounds(field.getCoordinates().getX() * FIELD_WIDTH_AND_HEIGHT,
                field.getCoordinates().getY() * FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT,
                FIELD_WIDTH_AND_HEIGHT);
        this.addActionListener(frontendChessboard);
        this.addMouseListener(frontendChessboard);
        this.setBackground(BACKGROUND_COLOR_RULES_ENGINE.process(field));
        this.updateGraphics(field);
    }

    /**
     * Creates a front-end field id.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @param fieldId          The back-end field id.
     * @return The front-end field id.
     */
    public static int createButtonId(PlayerColor humanPlayerColor, int fieldId) {
        return humanPlayerColor == WHITE ? fieldId : (MAXIMUM_FIELD_ID - fieldId);
    }

    /**
     * Reset the front-end field.
     *
     * @param field A back-end field.
     * @return The front-end field.
     */
    public FrontendField reset(@NotNull Field field) {
        this.setText(field.getCode());
        this.setIcon(null);

        return this;
    }

    /**
     * Updates the front-end field.
     *
     * @param field The back-end field.
     * @return The front-end field.
     */
    public FrontendField updateGraphics(@NotNull Field field) {
        this.id = field.getId();

        if (field.getPieceType() == null) {
            return this;
        }

        this.setText(null);
        this.setIcon(IconFactory.getIcon(field));

        return this;
    }
}
