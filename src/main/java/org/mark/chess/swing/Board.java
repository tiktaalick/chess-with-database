package org.mark.chess.swing;

import org.mark.chess.model.Game;
import org.mark.chess.service.GameService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JFrame implements ActionListener, MouseListener {
    private final Game game;

    private GameService gameService;

    public Board(GameService gameService) {
        this.gameService = gameService;
        game = gameService.initializeGame(this);
        gameService.initializeBoard(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Ignored
    }

    @Override
    public void mousePressed(MouseEvent event) {
        gameService.handleButtonClick(game, this, event.getButton(), (JButton) event.getSource());
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mouseExited(MouseEvent event) {
        // Ignored
    }
}
