package org.mark.chess.factory;

import org.mark.chess.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ApplicationFactory {
    public Application getInstance() {
        return new SpringApplicationBuilder(Application.class).headless(false).run().getBean(Application.class);
    }
}
