package net.n2oapp.framework.engine.data;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueryUtilTest {
    @Test
    public void normalizeParams() {
        //empty amps
        assertThat(QueryUtil.normalizeQueryParams("/test?&"), is("/test"));
        assertThat(QueryUtil.normalizeQueryParams("/test?&size=10"), is("/test?size=10"));
        assertThat(QueryUtil.normalizeQueryParams("/test?&&size=10"), is("/test?size=10"));
        assertThat(QueryUtil.normalizeQueryParams("/test?&&"), is("/test"));
        //double amps
        assertThat(QueryUtil.normalizeQueryParams("/test?page=1&&size=10"), is("/test?page=1&size=10"));
        assertThat(QueryUtil.normalizeQueryParams("/test?page=1&&&size=10"), is("/test?page=1&size=10"));
    }
}
