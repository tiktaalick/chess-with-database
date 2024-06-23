package org.mark.chess.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Logging {

    public static final  List<String>      LOG_FILTERED_METHODS = List.of("setBestMove", "logBestMove");
    public static final  List<String>      LOG_FILTERED_STRINGS = Collections.emptyList();// List.of("pawn d", "pawn e");
    private static final Level             DEFAULT_LOG_LEVEL    = Level.INFO;
    private static final long              FROM_NANO_TO_MILLI   = 1_000_000;
    private static final Logger            LOGGER               = Logger.getLogger(Logging.class.getName());
    private static       Map<String, Long> durationMap          = new HashMap<>();

    public static void logDuration() {
        durationMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> LOGGER.info(() -> entry.getKey() +
                        (entry.getValue() == 0 ? "" : (" took: " + (entry.getValue() / FROM_NANO_TO_MILLI + " milliseconds")))));
    }

    public static void logDurationAndReset() {
        logDuration();
        durationMap = new HashMap<>();
    }

    public static void setDefaultLogLevel() {
        filterLog(DEFAULT_LOG_LEVEL);
    }

    public static void storeDuration(String methodName, long nanoBefore, long nanoAfter) {
        durationMap.put(methodName, Optional.ofNullable(durationMap.get(methodName)).orElse(0L) + (nanoAfter - nanoBefore));
    }

    private static void filterLog(Level targetLevel) {
        var rootLogger = Logger.getLogger("");
        var logFilter = new LogFilter();
        rootLogger.setLevel(targetLevel);
        rootLogger.setFilter(logFilter);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(targetLevel);
            handler.setFilter(logFilter);
            rootLogger.info(() -> "handler.level=" + handler.getLevel());
            rootLogger.info(() -> "handler.filter=" + handler.getFilter());
        }
    }
}
