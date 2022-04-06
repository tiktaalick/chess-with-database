package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.GameStatus;

import java.util.List;

@Data
@Accessors(chain = true)
public class Game {
    private List<Player> players;
    private int          currentPlayerId;
    private List<Field>  grid;
    private GameStatus   gameStatus;

    public void setLost() {
        gameStatus = GameStatus.HAS_LOST;
    }

    public void setWon() {
        gameStatus = GameStatus.HAS_WON;
    }
}
