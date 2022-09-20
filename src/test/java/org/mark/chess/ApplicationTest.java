package org.mark.chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.factory.ApplicationFactory;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {
    @Mock
    private Application application;

    @Test
    void testMain() {
        try (MockedStatic<ApplicationFactory> applicationFactoryMockedStatic = Mockito.mockStatic(ApplicationFactory.class)) {
            applicationFactoryMockedStatic.when(() -> ApplicationFactory.getInstance()).thenReturn(application);
            Application.main(new String[]{"bla"});
            verify(application).startApplication(WHITE);
        }
    }
}