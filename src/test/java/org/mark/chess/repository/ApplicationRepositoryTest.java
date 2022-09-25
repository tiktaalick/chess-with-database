package org.mark.chess.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.ApplicationRepository;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ApplicationRepositoryTest {
    @Test
    void testGetInstance() {
        assertNotNull(ApplicationRepository.getInstance());
    }
}