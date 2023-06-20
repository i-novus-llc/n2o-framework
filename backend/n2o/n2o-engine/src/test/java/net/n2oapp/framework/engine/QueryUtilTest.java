package net.n2oapp.framework.engine;

import net.n2oapp.framework.engine.data.QueryUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тесты {@link net.n2oapp.framework.engine.data.QueryUtil}
 */
public class QueryUtilTest {
    
    @Test
    void replaceListPlaceholder() {
        AtomicInteger i = new AtomicInteger();
        assertThat(QueryUtil.replaceListPlaceholder("a?{b}", "{b}",
                Arrays.asList("x=:x", "y=:y", "z=:z"), "",
                v -> v.replaceAll(":[xyz]", "" + i.incrementAndGet()),
                (a, b) -> a + "&" + b), is("a?x=1&y=2&z=3"));
    }

    @Test
    void normalizeParams() {
        //empty amps
        assertThat(QueryUtil.normalizeQueryParams("/test?&"), CoreMatchers.is("/test"));
        assertThat(QueryUtil.normalizeQueryParams("/test?&size=10"), CoreMatchers.is("/test?size=10"));
        assertThat(QueryUtil.normalizeQueryParams("/test?&&size=10"), CoreMatchers.is("/test?size=10"));
        assertThat(QueryUtil.normalizeQueryParams("/test?&&"), CoreMatchers.is("/test"));
        //double amps
        assertThat(QueryUtil.normalizeQueryParams("/test?page=1&&size=10"), CoreMatchers.is("/test?page=1&size=10"));
        assertThat(QueryUtil.normalizeQueryParams("/test?page=1&&&size=10"), CoreMatchers.is("/test?page=1&size=10"));
    }
}
