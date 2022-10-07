package org.mark.chess.piece;

/**
 * An {@link IllegalStateException} that will be thrown when a pawn is incorrectly promoted.
 */
public class IllegalPawnPromotionException extends IllegalStateException {

    /**
     * Constructor for the {@link IllegalPawnPromotionException}.
     */
    public IllegalPawnPromotionException() {
        super("It's not allowed to promote a pawn to a king.");
    }
}
