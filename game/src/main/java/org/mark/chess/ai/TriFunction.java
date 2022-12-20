package org.mark.chess.ai;

/**
 * A function that receives three parameters.
 *
 * @param <T> Parameter one.
 * @param <U> Parameter two.
 * @param <V> Parameter three.
 * @param <R> The result.
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * A method that receives three parameters and returns a result.
     *
     * @param t Parameter one.
     * @param u Parameter two.
     * @param v Parameter three.
     * @return The result.
     */
    R apply(T t, U u, V v);
}
