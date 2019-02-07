package net.n2oapp.framework.engine;

import net.n2oapp.framework.engine.data.QueryUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тесты {@link net.n2oapp.framework.engine.data.QueryUtil}
 */
public class QueryUtilTest {
    @Test
    public void replaceListPlaceholder() {
        AtomicInteger i = new AtomicInteger();
        assertThat(QueryUtil.replaceListPlaceholder("a?{b}", "{b}",
                Arrays.asList("x=:x", "y=:y", "z=:z"), "",
                v -> v.replaceAll(":[xyz]", ""+i.incrementAndGet()),
                (a, b) -> a + "&" + b), is("a?x=1&y=2&z=3"));
    }
}
