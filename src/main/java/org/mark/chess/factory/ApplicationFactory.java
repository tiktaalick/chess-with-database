package org.mark.chess.factory;

import org.mark.chess.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;

public final class ApplicationFactory {
    private ApplicationFactory() {
    }

    public static Application getInstance() {
        return new SpringApplicationBuilder(Application.class).headless(false).run().getBean(Application.class);
    }
}
