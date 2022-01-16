package org.mark.chess;

import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.mark.chess"})
public class Application {

    @Autowired
    private GameService gameService;

    public static void main(String[] args) {
        getInstance().startApplication();
    }

    public static Application getInstance() {
        return new SpringApplicationBuilder(Application.class)
                .headless(false)
                .run()
                .getBean(Application.class);
    }

    public void startApplication() {
        new Board(gameService);
    }
}
