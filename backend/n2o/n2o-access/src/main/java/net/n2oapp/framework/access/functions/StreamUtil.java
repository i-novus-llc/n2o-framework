package net.n2oapp.framework.access.functions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * @author V. Alexeev.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtil {

    @SafeVarargs
    public static <T> Stream<T> safeStreamOf(T... t) {
        return t == null ? Stream.empty() : stream(t);
    }

    public static <T> Stream<T> safeStreamOf(Collection<T> t) {
        return t == null ? Stream.empty() : t.stream();
    }
}