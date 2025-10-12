package dev.creoii.chaos.util.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Logger(String id) {
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String timestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMAT);
    }

    private void log(Level level, String message) {
        switch (level) {
            case INFO, DEBUG -> System.out.println("[" + level.name() + "] [" + id + "]: " + timestamp() + ": " + message);
            case WARN -> System.out.println("[" + level.name() + "] [" + id + "]: " + timestamp() + ": \u001B[33m" + YELLOW + message + "\u001B[0m");
            case ERROR -> System.out.println("[" + level.name() + "] [" + id + "]: " + timestamp() + ": \u001B[31m" + RED + message + "\u001B[0m");
        }
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void warn(String message) {
        log(Level.WARN, message);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }

    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    public void info(String message, int maxLength) {
        log(Level.INFO, message.length() > maxLength ? message.substring(0, maxLength) + "..." : message);
    }

    public void warn(String message, int maxLength) {
        log(Level.WARN, message.length() > maxLength ? message.substring(0, maxLength) + "..." : message);
    }

    public void error(String message, int maxLength) {
        log(Level.ERROR, message.length() > maxLength ? message.substring(0, maxLength) + "..." : message);
    }

    public void debug(String message, int maxLength) {
        log(Level.DEBUG, message.length() > maxLength ? message.substring(0, maxLength) + "..." : message);
    }

    public enum Level {
        INFO,
        WARN,
        ERROR,
        DEBUG
    }
}
