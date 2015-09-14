package org.kubek2k.either;

import java.util.stream.Stream;

public class EitherStreams {
    public static <T> Either<Stream<T>> eitherAllOrNone(final Stream<Either<T>> s) {
        return s.reduce(Either.of(Stream.empty()),
                (streamEither, valueEither) ->
                        valueEither.flatMap(value ->
                                streamEither.map(stream ->
                                        Stream.concat(stream, Stream.of(value)))),
                (streamEither1, streamEither2) ->
                        streamEither1.flatMap(stream1 ->
                            streamEither2.map(stream2 ->
                                Stream.concat(stream1, stream2))));
    }

    public static <T> Stream<T> allNonProblematic(final Stream<Either<T>> s) {
        return s.map(tEither ->
                tEither.map(Stream::of).orElse(p -> Stream.empty()))
                    .reduce(Stream::concat)
                    .orElse(Stream.empty());
    }

    public static <T> Stream<T> problemToEmpty(final Either<T> e) {
        return e.map(Stream::of).orElse(p -> Stream.empty());
    }
}
