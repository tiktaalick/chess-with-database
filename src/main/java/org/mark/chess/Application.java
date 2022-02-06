package org.mark.chess;

import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.mark.chess"})
public class Application {

    @Autowired
    private GameService gameService;

    public static void main(String[] args) {
        new ApplicationFactory().getInstance().startApplication();
    }

    public void startApplication() {
        new Board(gameService);
    }
}
