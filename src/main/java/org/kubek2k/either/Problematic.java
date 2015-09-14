package org.kubek2k.either;

public class Problematic {

    private final Throwable cause;

    public Problematic(final String message) {
        this.cause = generateCause(message);
    }

    public Problematic(final Throwable t) {
        this.cause = t;
    }

    private Throwable generateCause(final String message) {
        try {
            throw new RuntimeException(message);
        } catch (final RuntimeException e) {
            return  e;
        }
    }

    public Throwable cause() {
        return this.cause;
    }

    public String message() {
        return this.cause.getMessage();
    }
}
