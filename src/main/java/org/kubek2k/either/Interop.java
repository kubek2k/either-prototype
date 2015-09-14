package org.kubek2k.either;

import java.util.concurrent.Callable;

public class Interop {

    public static <R> Either<R> exceptionToProblematic(final Callable<R> callable) {
        try {
            return Either.of(callable.call());
        }
        catch(final Exception e) {
            return Either.problem(e);
        }
    }
}
