package org.mark.chess.game;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.ChessboardDirector;
import org.mark.chess.board.Field;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveDirector;
import org.mark.chess.player.Computer;
import org.mark.chess.player.Human;
import org.mark.chess.player.Player;
import org.mark.chess.player.PlayerColor;

import java.util.Arrays;
import java.util.List;

import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Contains methods to start a game and alter the state of the game.
 */
@Data
@Accessors(chain = true)
public class Game {

    private static final int LEFT_CLICK        = 1;
    private static final int MAXIMUM_SQUARE_ID = 63;
    private static final int RIGHT_CLICK       = 3;

    private static ChessboardDirector chessboardDirector = new ChessboardDirector();
    private static GameDirector       gameDirector       = new GameDirector();
    private static MoveDirector       moveDirector       = new MoveDirector();

    private Chessboard   chessboard;
    private Move         move    = new Move(new Field(null));
    private List<Player> players = Arrays.asList(new Human(WHITE), new Computer(BLACK));
    private boolean      inProgress;
    private PlayerColor  humanPlayerColor;
    private Player       activePlayer;

    /**
     * Creates a new game.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @param chessboard       The backend representation of a chessboard.
     */
    public Game(PlayerColor humanPlayerColor, Chessboard chessboard) {
        this.inProgress = true;
        this.humanPlayerColor = humanPlayerColor;
        this.activePlayer = players.get(WHITE.ordinal());
        this.chessboard = chessboard;
    }

    /**
     * Creates a new game with a frontend chessboard.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return A new game.
     */
    public static @NotNull Game create(PlayerColor humanPlayerColor) {
        return gameDirector.createGame(humanPlayerColor);
    }

    /**
     * Starts a new game based on the finished game.
     *
     * @param oldGame The finished game.
     * @return The new game.
     */
    public static @NotNull Game restart(@NotNull Game oldGame) {
        return gameDirector.restartGame(oldGame.getHumanPlayerColor().getOpposite());
    }

    /**
     * Sets the move director.
     *
     * @param moveDirector The move director.
     */
    public static void setMoveDirector(MoveDirector moveDirector) {
        Game.moveDirector = moveDirector;
    }

    /**
     * Changes the active player.
     */
    public void changeTurn() {
        this.setActivePlayer(players.get(getActivePlayer().getColor().getOpposite().ordinal()));
    }

    /**
     * Handles the input from the frontend.
     *
     * @param leftRightClick Indicates which button was clicked.
     * @param buttonId       The frontend button that was clicked.
     * @return The game.
     */
    public Game handleButtonClick(int leftRightClick, int buttonId) {
        var fieldClick = this.getChessboard().getFields().get(buttonId);

        if (leftRightClick == LEFT_CLICK && fieldClick.hasValidTo() && move.isFrom(this, fieldClick)) {
            this.move = moveDirector.performFromMove(this, move, fieldClick);
        } else if (leftRightClick == LEFT_CLICK && fieldClick.hasValidTo() && !move.isFrom(this, fieldClick)) {
            this.move = moveDirector.performToMove(this, move, fieldClick);
        } else if (leftRightClick == RIGHT_CLICK) {
            this.move = moveDirector.performResetMove(this, move);
        } else {
            // Clicks on fields that aren't occupied by the active player are ignored.
        }

        return this;
    }

    /**
     * Determines whether the game is in progress or not.
     *
     * @param kingField A king, which might be in checkmate or stalemate.
     */
    public void setGameProgress(Field kingField) {
        this.setInProgress(this.isInProgress()
                ? (!kingField.isCheckMate() && !kingField.isStaleMate())
                : this.isInProgress());
    }
}
