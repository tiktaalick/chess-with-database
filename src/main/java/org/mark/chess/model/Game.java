package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;

import java.util.List;

@Data
@Accessors(chain = true)
public class Game {
    private boolean      inProgress;
    private Color        humanPlayerColor;
    private Color        currentPlayerColor;
    private Grid         grid;
    private List<Player> players;
}
