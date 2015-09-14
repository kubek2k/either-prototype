package org.kubek2k.either;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Either<T> {

    public abstract T orElse(Function<? super Problematic, T> f);

    public abstract T orBlow();

    public abstract <R> Either<R> map(Function<? super T, R> f);

    public abstract <R> Either<R> flatMap(Function<? super T, Either<R>> f);

    public abstract Either<T> filter(Predicate<? super T> p);

    public Either<T> onProblem(final Consumer<? super Problematic> c) {
        return this;
    }

    public static <T> Either<T> of(final T value) {
        return new ValueEither<T>(value);
    }

    public static <T> Either<T> problem(final String message) {
        return new ProblematicEither<>(new Problematic(message));
    }

    public static <T> Either<T> problem(final Throwable cause) {
        return new ProblematicEither<>(new Problematic(cause));
    }

    private static class ValueEither<T> extends Either<T> {

        private final T value;

        private ValueEither(final T value) {
            this.value = value;
        }

        @Override
        public T orElse(final Function<? super Problematic, T> f) {
            return this.value;
        }

        @Override
        public T orBlow() {
            return this.value;
        }

        @Override
        public <R> Either<R> map(final Function<? super T, R> f) {
            return new ValueEither<R>(f.apply(this.value));
        }

        @Override
        public <R> Either<R> flatMap(final Function<? super T, Either<R>> f) {
            return f.apply(this.value);
        }

        @Override
        public Either<T> filter(final Predicate<? super T> p) {
            if (p.test(this.value)) {
                return this;
            }
            return problem("Thing got filtered out here");
        }

        @Override
        public String toString() {
            return this.value.toString();
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof ValueEither && this.value.equals(((ValueEither)other).value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
    }

    private static class ProblematicEither<T> extends Either<T> {

        private final Problematic problem;

        private ProblematicEither(final Problematic problematic) {
            this.problem = problematic;
        }

        @Override
        public T orElse(final Function<? super Problematic, T> f) {
            return f.apply(this.problem);
        }

        @Override
        public T orBlow() {
            throw new RuntimeException(this.problem.cause());
        }

        @Override
        public <R> Either<R> map(final Function<? super T, R> f) {
            return coerce();
        }

        @Override
        public <R> Either<R> flatMap(final Function<? super T, Either<R>> f) {
            return coerce();
        }

        @Override
        public Either<T> filter(final Predicate<? super T> p) {
            return coerce();
        }

        @Override
        public Either<T> onProblem(final Consumer<? super Problematic> consumer) {
            consumer.accept(this.problem);
            return this;
        }

        private <R> Either<R> coerce() {
            return new ProblematicEither<R>(this.problem);
        }

        @Override
        public String toString() {
            return this.problem.message();
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof ProblematicEither && this.problem.equals(((ProblematicEither)other).problem);
        }

        @Override
        public int hashCode() {
            return this.problem.hashCode();
        }
    }
}
