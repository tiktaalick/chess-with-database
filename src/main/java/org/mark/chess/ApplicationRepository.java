package org.mark.chess;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Repository with a maximum of one application.
 */
public final class ApplicationRepository {

    private static Application instance;

    private ApplicationRepository() {
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new SpringApplicationBuilder(Application.class).headless(false).run().getBean(Application.class);
        }

        return instance;
    }
}
