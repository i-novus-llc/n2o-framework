package net.n2oapp.framework.config.util;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class StylesResolverTest {

    @Test
    public void test() {
        Map<String, String> oneStyle = StylesResolver.resolveStyles("margin-top: 16px; ");
        assertThat(oneStyle.size(), is(1));
        assertThat(oneStyle.get("marginTop"), is("16px"));

        Map<String, String> twoStyles = StylesResolver.resolveStyles("box-shadow: 0 0 1px #fff,0 0 5px rgba(0,0,0,0.3);    padding: 3px 5px");
        assertThat(twoStyles.size(), is(2));
        assertThat(twoStyles.get("boxShadow"), is("0 0 1px #fff,0 0 5px rgba(0,0,0,0.3)"));
        assertThat(twoStyles.get("padding"), is("3px 5px"));

        assertThat(StylesResolver.resolveStyles(" "), nullValue());
        assertThat(StylesResolver.resolveStyles(null), nullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidInputTest() {
        StylesResolver.resolveStyles("box-shadow: illegal : argument;");
    }

}
