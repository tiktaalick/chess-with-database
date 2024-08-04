package org.mark.chess.log;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import static org.mark.chess.log.Logging.LOG_FILTERED_METHODS;
import static org.mark.chess.log.Logging.LOG_FILTERED_STRINGS;

public class LogFilter implements Filter {

    @Override
    public boolean isLoggable(LogRecord log) {
        return isLoggableMethod(log) && isLoggableString(log);
    }

    private static boolean isLoggableMethod(LogRecord log) {
        return LOG_FILTERED_METHODS.isEmpty() || LOG_FILTERED_METHODS.contains(log.getSourceMethodName());
    }

    private static boolean isLoggableString(LogRecord log) {
        return LOG_FILTERED_STRINGS.isEmpty() || LOG_FILTERED_STRINGS.stream().anyMatch(substring -> log.getMessage().contains(substring));
    }
}
