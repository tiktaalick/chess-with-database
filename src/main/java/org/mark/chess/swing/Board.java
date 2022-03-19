package org.mark.chess.swing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.mark.chess.model.Game;
import org.mark.chess.model.Move;
import org.mark.chess.service.GameService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(fluent = true)
public class Board extends JFrame implements ActionListener, MouseListener {
    private final Game game;
    private Move move;

    private GameService gameService;

    public Board(GameService gameService) {
        this.gameService = gameService;
        game = gameService.initializeGame(this);
        move = new Move();
        gameService.initializeBoard(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Ignored
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        // Ignored
    }

    @Override
    public void mousePressed(MouseEvent event) {
        gameService.handleButtonClick(game, this, event.getButton(), (JButton) event.getSource());
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
