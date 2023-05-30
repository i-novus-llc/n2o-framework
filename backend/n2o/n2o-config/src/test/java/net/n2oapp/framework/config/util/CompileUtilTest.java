package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.test.N2oTestBase;
import org.jdom2.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CompileUtilTest extends N2oTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
    }

    @Test
    void testNestedAttributes() {
        final Map<String, String> map = new LinkedHashMap<>();
        CompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment());
        ExtensionAttributesAware extAttributes = new ExtensionAttributesAware() {
            @Override
            public void setExtAttributes(Map<N2oNamespace, Map<String, String>> extAttributes) {
            }

            @Override
            public Map<N2oNamespace, Map<String, String>> getExtAttributes() {
                Map<N2oNamespace, Map<String, String>> attrs = new HashMap<>();
                attrs.put(new N2oNamespace(Namespace.getNamespace("test")), map);
                return attrs;
            }
        };

        map.put("a", "1");
        map.put("b-x", "2");
        map.put("b-c-x", "3");
        map.put("b-c-d-x", "4");
        map.put("b-c-d-e-x", "5");
        Map<String, Object> result = processor.mapAttributes(extAttributes);
        assertThat(result.get("a"), is(1));
        assertThat(((Map) result.get("b")).get("x"), is(2));
        assertThat(((Map) ((Map) result.get("b")).get("c")).get("x"), is(3));
        assertThat(((Map) ((Map) ((Map) result.get("b")).get("c")).get("d")).get("x"), is(4));
        assertThat(((Map) ((Map) ((Map) ((Map) result.get("b")).get("c")).get("d")).get("e")).get("x"), is(5));


        map.clear();
        map.put("a", "1");
        map.put("a-b", "true");
        try {
            processor.mapAttributes(extAttributes);
        } catch (IllegalArgumentException e) {
            //N2oCompileProcessor:365
            assertThat(e.getMessage(), is("The result already contains an element with key a"));
        }

        map.clear();
        map.put("x-x-a", "true");
        map.put("x-x", "true");
        try {
            processor.mapAttributes(extAttributes);
        } catch (IllegalArgumentException e) {
            //N2oCompileProcessor:370
            assertThat(e.getMessage(), is("The result already contains an element with key x"));
        }
    }
}
