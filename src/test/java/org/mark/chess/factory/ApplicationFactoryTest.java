package org.mark.chess.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ApplicationFactoryTest {
    @Test
    void testGetInstance() {
        assertNotNull(ApplicationFactory.getInstance());
    }
}