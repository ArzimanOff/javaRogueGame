package org.arzimanoff.model.common;

import java.util.ArrayDeque;

/**
 * Logger represented by a queue-type collection.
 * It consumes a message from model classes and provide it for third-party consumers
 */
public class Logger {
    ArrayDeque<String> logger = new ArrayDeque<>();

    public void add(final String message) {
        logger.offer(message);
    }

    public String get() {
        return logger.poll();
    }

    @Override
    public String toString() {
        return "Logger{" +
               "logger=" + logger +
               '}';
    }
}
