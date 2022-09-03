package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.Icon;

import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ButtonLogicTest {
    private static final int BUTTON_WIDTH = 75;

    @InjectMocks
    private ButtonLogic buttonLogic;

    @Mock
    private Button button;

    @Mock
    private FieldLogic fieldLogic;

    @Test
    void testInitializeButton_WhenHappyFlow_ThenIconSet() {
        Field field = new Field(null).setCoordinates(new Coordinates(3, 4));
        Piece piece = new Pawn(WHITE);

        field.setButton(button).setPiece(piece);

        buttonLogic.initializeButton(field);

        verify(button).setIcon(any(Icon.class));
    }
}