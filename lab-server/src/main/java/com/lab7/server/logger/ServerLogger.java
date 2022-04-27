package com.lab7.server.logger;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public final class ServerLogger {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ServerLogger.class);

    private ServerLogger() {
    }

    public static void logInfoMessage(String format, Object... arg) {
        LOGGER.info(format, arg);
    }

    public static void logDebugMessage(String format, Object... arg) {
        LOGGER.debug(format, arg);
    }

    public static void logErrorMessage(String format, Object... arg) {
        LOGGER.error(format, arg);
    }
}
