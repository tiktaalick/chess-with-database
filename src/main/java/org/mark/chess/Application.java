package org.mark.chess;

import org.mark.chess.enums.Color;
import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private final GameService gameService;

    public Application(GameService gameService) {
        this.gameService = gameService;
    }

    public static void main(String[] args) {
        ApplicationFactory.getInstance().startApplication(Color.WHITE);
    }

    public void startApplication(Color humanPlayerColor) {
        new Board(gameService, humanPlayerColor);
    }
}
