package org.mark.chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {
    @Mock
    private Application application;

    @Test
    void testMain() {
        try (MockedStatic<ApplicationRepository> applicationFactoryMockedStatic = Mockito.mockStatic(ApplicationRepository.class)) {
            applicationFactoryMockedStatic.when(() -> ApplicationRepository.getInstance()).thenReturn(application);
            Application.main(new String[]{"bla"});
            verify(application).startApplication(WHITE);
        }
    }
}