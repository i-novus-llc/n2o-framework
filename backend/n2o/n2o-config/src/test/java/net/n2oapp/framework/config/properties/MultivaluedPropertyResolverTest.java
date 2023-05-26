package net.n2oapp.framework.config.properties;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест класса {@link MultivaluedPropertyResolver}
 */
public class MultivaluedPropertyResolverTest {

    @Test
    void test() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("k1", "v1");
        map1.put("k", "v1");
        Map<String, String> map2 = new HashMap<>();
        map2.put("k2", "v2");
        map2.put("k", "v2");
        MultivaluedPropertyResolver resolver = new MultivaluedPropertyResolver(
                new MutablePropertyResolver(map1), new MutablePropertyResolver(map2));

        //getProperty
        assertThat(resolver.getProperty("k1"), is("v1"));
        assertThat(resolver.getProperty("k2"), is("v2"));
        assertThat(resolver.getProperty("k"), is("v1"));
        assertThat(resolver.getProperty("k3"), nullValue());

        //getRequiredProperty
        assertThat(resolver.getRequiredProperty("k1"), is("v1"));
        assertThat(resolver.getRequiredProperty("k2"), is("v2"));
        assertThat(resolver.getRequiredProperty("k"), is("v1"));
        try {
            resolver.getRequiredProperty("k3");
        } catch (Exception e) {
            assertThat(e, instanceOf(NoSuchElementException.class));
        }

        //resolvePlaceholders
        assertThat(resolver.resolvePlaceholders("Hello ${k1}"), is("Hello v1"));
        assertThat(resolver.resolvePlaceholders("Hello ${k2}"), is("Hello v2"));
        assertThat(resolver.resolvePlaceholders("Hello ${k}"), is("Hello v1"));
        assertThat(resolver.resolvePlaceholders("Hello ${k3}"), is("Hello ${k3}"));
        assertThat(resolver.resolvePlaceholders("Hello ${k1} ${k2}"), is("Hello v1 v2"));
        assertThat(resolver.resolvePlaceholders("Hello ${k1} ${k2} ${k3}"), is("Hello v1 v2 ${k3}"));

        //resolveRequiredPlaceholders
        assertThat(resolver.resolveRequiredPlaceholders("Hello ${k1}"), is("Hello v1"));
        assertThat(resolver.resolveRequiredPlaceholders("Hello ${k2}"), is("Hello v2"));
        assertThat(resolver.resolveRequiredPlaceholders("Hello ${k}"), is("Hello v1"));
        try {
            resolver.resolveRequiredPlaceholders("Hello ${k3}");
        } catch (Exception e) {
            assertThat(e, instanceOf(NoSuchElementException.class));
        }
        assertThat(resolver.resolvePlaceholders("Hello ${k1} ${k2}"), is("Hello v1 v2"));
        try {
            resolver.resolveRequiredPlaceholders("Hello ${k1} ${k2} ${k3}");
        } catch (Exception e) {
            assertThat(e, instanceOf(NoSuchElementException.class));
        }
    }
}
