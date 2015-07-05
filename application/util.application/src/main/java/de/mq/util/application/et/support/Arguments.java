package de.mq.util.application.et.support;

/**
 * Simple utility class for for argument validation.
 */
class Arguments {

    public static void ensureNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void ensureTrue(boolean value, String message) {
        if (!value) {
            throw new IllegalArgumentException(message);
        }
    }

}
