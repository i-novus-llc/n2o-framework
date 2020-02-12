package net.n2oapp.framework.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.Context;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static net.n2oapp.framework.api.PlaceHoldersResolver.replaceOptional;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тест шаблонизатора текста
 */
public class PlaceHoldersResolverTest {

    @Test
    public void resolve() {
        String prefix = "{";
        String suffix = "}";

        PlaceHoldersResolver resolver = new PlaceHoldersResolver(prefix, suffix);
        DataSet data = new DataSet();
        data.put("name", "Foo");
        data.put("surname", "Bar");
        Assert.assertEquals("bla {noname} bla", resolver.resolve("bla {noname} bla", data));
        Assert.assertEquals("bla Foo bla Bar bla", resolver.resolve("bla {name} bla {surname} bla", data));
        Assert.assertEquals("bla test bla test bla", resolver.resolve("bla {name} bla {surname} bla", p -> "test"));
    }

    @Test
    public void resolve2() {
        String prefix = "${";
        String suffix = "}";
        PlaceHoldersResolver resolver = new PlaceHoldersResolver(prefix, suffix);
        DataSet data = new DataSet();
        data.put("name", "Foo");
        data.put("surname", "Bar");

        assertThat(resolver.resolve("${name}", data), is("Foo"));
        assertThat(resolver.resolve("a${name}b", data), is("aFoob"));
        assertThat(resolver.resolve("${name} ${surname}", data), is("Foo Bar"));
        assertThat(resolver.resolve("{name}", data), is("{name}"));
        assertThat(resolver.resolve("a${name}b{surname}", data), is("aFoob{surname}"));
    }

    @Test
    public void resolveWithoutEnd() {
        String prefix = ":";
        String suffix = "";
        PlaceHoldersResolver resolver = new PlaceHoldersResolver(prefix, suffix);
        DataSet data = new DataSet();
        data.put("b", 1);
        data.put("abc", 2);

        assertThat(resolver.resolve(":b", data), is("1"));
        assertThat(resolver.resolve(":a", data), is(":a"));
        assertThat(resolver.resolve("/:abc", data), is("/2"));
        assertThat(resolver.resolve("/a/:b/c", data), is("/a/1/c"));
    }

    @Test
    public void resolveUrls() {
        DataSet data = new DataSet();
        data.put("b", 1);
        data.put("abc", 2);

        PlaceHoldersResolver resolver = new PlaceHoldersResolver(":", "", true);
        assertThat(resolver.resolve("http://example.com", data), is("http://example.com"));
        assertThat(resolver.resolve("http://example.com:9090/", data), is("http://example.com:9090/"));
        assertThat(resolver.resolve("http://example.com:9090/ss", data), is("http://example.com:9090/ss"));
        assertThat(resolver.resolve("http://example.com:9090/:b/:abc/c", data), is("http://example.com:9090/1/2/c"));
        assertThat(resolver.resolve("/:b/:abc/c", data), is("/1/2/c"));
        assertThat(resolver.resolve("http://:abc", data), is("http://2"));
    }

    @Test
    public void extract() {
        String prefix = "{";
        String suffix = "}";

        PlaceHoldersResolver placeHoldersResolver = new PlaceHoldersResolver(prefix, suffix);
        Set result;

        result = placeHoldersResolver.extractPlaceHolders("{test}");
        assert result.size() == 1;
        assert result.contains("test");

        result = placeHoldersResolver.extractPlaceHolders("aaa{test1}bb{test2}cc");
        assert result.size() == 2;
        assert result.contains("test1");
        assert result.contains("test2");

        result = placeHoldersResolver.extractPlaceHolders("{test1}bb{test2}");
        assert result.size() == 2;
        assert result.contains("test1");
        assert result.contains("test2");

        result = placeHoldersResolver.extractPlaceHolders("{test1}{test2}");
        assert result.size() == 2;
        assert result.contains("test1");
        assert result.contains("test2");
    }

    @Test
    public void resolveValue() {
        PlaceHoldersResolver resolver = new PlaceHoldersResolver("#{", "}");
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        Assert.assertNull(resolver.resolveValue(null, data));
        Assert.assertEquals(1, resolver.resolveValue(1, data));
        Assert.assertEquals("", resolver.resolveValue("", data));
        Assert.assertEquals("{name}", resolver.resolveValue("{name}", data));
        Assert.assertNull(resolver.resolveValue("#{noname}", data));
        Assert.assertEquals("test", resolver.resolveValue("#{name}", data));
        Assert.assertEquals("test", resolver.resolveValue("#{name}", (k) -> "test"));
    }

    @Test
    public void resolveWithFunctions() {
        PlaceHoldersResolver resolver = new PlaceHoldersResolver("{", "}");
        DataSet data = new DataSet();
        data.put("name", "Foo");
        Assert.assertEquals("bla Foo bla  bla",
                resolver.resolve("bla {name} bla {noname} bla",
                        PlaceHoldersResolver.replaceNullByEmpty(data::get)));
        try {
            resolver.resolve("bla {name} bla {noname} bla",
                    PlaceHoldersResolver.replaceRequired(data::get));
            Assert.fail();
        } catch (NotFoundPlaceholderException e) {
        }
        Assert.assertEquals("bla Foo bla test bla",
                resolver.resolve("bla {name} bla {noname?test} bla",
                        PlaceHoldersResolver.replaceOptional(data::get)));
        Assert.assertEquals("bla Foo bla {noname?} bla",
                resolver.resolve("bla {name} bla {noname?} bla",
                        PlaceHoldersResolver.replaceOptional(data::get)));
        Assert.assertEquals("bla Foo bla {noname} bla",
                resolver.resolve("bla {name} bla {noname} bla",
                        PlaceHoldersResolver.replaceOptional(data::get)));
        try {
            resolver.resolve("bla {name} bla {noname!} bla",
                    PlaceHoldersResolver.replaceOptional(data::get));
            Assert.fail();
        } catch (NotFoundPlaceholderException e) {
        }
        Assert.assertEquals("bla Foo bla  bla",
                resolver.resolve("bla {name} bla {noname} bla",
                        PlaceHoldersResolver.replaceNullByEmpty(
                                PlaceHoldersResolver.replaceOptional(data::get))));
    }

    @Test
    public void resolveJson() {
        Context context = mock(Context.class);
        List<String> roles = Arrays.asList("user", "looser");
        when(context.get("testContext")).thenReturn("testValue");
        when(context.get("roles")).thenReturn(roles);
        when(context.get("username")).thenReturn("testUsername");
        when(context.get("age")).thenReturn(99);
        when(context.get("isActive")).thenReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        Assert.assertEquals("testValue", PlaceHoldersResolver.replaceByJson(replaceOptional(context::get), mapper).apply("testContext"));
        Assert.assertEquals("testUsername", PlaceHoldersResolver.replaceByJson(replaceOptional(context::get), mapper).apply("username"));
        Assert.assertEquals("99", PlaceHoldersResolver.replaceByJson(replaceOptional(context::get), mapper).apply("age"));
        Assert.assertEquals("[\"user\",\"looser\"]", PlaceHoldersResolver.replaceByJson(replaceOptional(context::get), mapper).apply("roles"));
        Assert.assertEquals("true", PlaceHoldersResolver.replaceByJson(replaceOptional(context::get), mapper).apply("isActive"));
    }

    @Test
    public void testResolveJsonObject() throws IOException {
        Context context = mock(Context.class);
        List<String> strings = Arrays.asList("user", "looser");
        List<Integer> ints = Arrays.asList(1, 2);
        when(context.get("int")).thenReturn(1);
        when(context.get("double")).thenReturn(1.0);
        when(context.get("bool")).thenReturn(false);
        when(context.get("text")).thenReturn("text");
        when(context.get("strings")).thenReturn(strings);
        when(context.get("ints")).thenReturn(ints);
        when(context.get("combined")).thenReturn("Value");
        when(context.get("nullable")).thenReturn(null);
        when(context.get("pojo")).thenReturn(new TestPOJO(1, "name", true, null));
        PlaceHoldersResolver resolver = new PlaceHoldersResolver("#{", "}");
        ObjectMapper mapper = new ObjectMapper();
        String input = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("net/n2oapp/framework/api/testObject.json"), "UTF-8");
        String result = resolver.resolveJson(input, replaceOptional(context::get), mapper);
        JsonNode root = mapper.readTree(result);

        assertThat(root.get("test").size(), is(9));
        assertThat(root.get("test").get("int").isInt(), is(true));
        assertThat(root.get("test").get("int").numberValue(), is(1));
        assertThat(root.get("test").get("double").isDouble(), is(true));
        assertThat(root.get("test").get("double").numberValue(), is(1.0));
        assertThat(root.get("test").get("bool").isBoolean(), is(true));
        assertThat(root.get("test").get("bool").booleanValue(), is(false));
        assertThat(root.get("test").get("text").isTextual(), is(true));
        assertThat(root.get("test").get("text").textValue(), is("text"));

        assertThat(root.get("test").get("strings").isArray(), is(true));
        assertThat(root.get("test").get("strings").size(), is(2));
        assertThat(root.get("test").get("strings").get(0).isTextual(), is(true));
        assertThat(root.get("test").get("strings").get(0).textValue(), is("user"));
        assertThat(root.get("test").get("strings").get(1).isTextual(), is(true));
        assertThat(root.get("test").get("strings").get(1).textValue(), is("looser"));

        assertThat(root.get("test").get("ints").isArray(), is(true));
        assertThat(root.get("test").get("ints").size(), is(2));
        assertThat(root.get("test").get("ints").get(0).isNumber(), is(true));
        assertThat(root.get("test").get("ints").get(0).numberValue(), is(1));
        assertThat(root.get("test").get("ints").get(1).isNumber(), is(true));
        assertThat(root.get("test").get("ints").get(1).numberValue(), is(2));

        assertThat(root.get("test").get("combined").isTextual(), is(true));
        assertThat(root.get("test").get("combined").textValue(), is("testValue"));

        assertThat(root.get("test").get("nullable").isNull(), is(true));


        assertThat(root.get("test").get("pojo").get("id").isInt(), is(true));
        assertThat(root.get("test").get("pojo").get("id").intValue(), is(1));
        assertThat(root.get("test").get("pojo").get("name").isTextual(), is(true));
        assertThat(root.get("test").get("pojo").get("name").textValue(), is("name"));
        assertThat(root.get("test").get("pojo").get("gender").isBoolean(), is(true));
        assertThat(root.get("test").get("pojo").get("gender").booleanValue(), is(true));
        assertThat(root.get("test").get("pojo").get("nullable").isNull(), is(true));
    }


    @Test
    public void testResolveJsonArray() throws IOException {
        Context context = mock(Context.class);
        List<String> strings = Arrays.asList("user", "looser");
        List<Integer> ints = Arrays.asList(1, 2);
        when(context.get("int")).thenReturn(1);
        when(context.get("double")).thenReturn(1.0);
        when(context.get("bool")).thenReturn(false);
        when(context.get("text")).thenReturn("text");
        when(context.get("strings")).thenReturn(strings);
        when(context.get("ints")).thenReturn(ints);
        when(context.get("combined")).thenReturn("Value");
        PlaceHoldersResolver resolver = new PlaceHoldersResolver("#{", "}");

        ObjectMapper mapper = new ObjectMapper();
        String input = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("net/n2oapp/framework/api/testArray.json"), "UTF-8");
        String result = resolver.resolveJson(input, replaceOptional(context::get), mapper);
        JsonNode root = mapper.readTree(result);

        assertThat(root.get(0).size(), is(7));
        assertThat(root.get(0).get("int").isInt(), is(true));
        assertThat(root.get(0).get("int").numberValue(), is(1));
        assertThat(root.get(0).get("double").isDouble(), is(true));
        assertThat(root.get(0).get("double").numberValue(), is(1.0));
        assertThat(root.get(0).get("bool").isBoolean(), is(true));
        assertThat(root.get(0).get("bool").booleanValue(), is(false));
        assertThat(root.get(0).get("text").isTextual(), is(true));
        assertThat(root.get(0).get("text").textValue(), is("text"));

        assertThat(root.get(0).get("strings").isArray(), is(true));
        assertThat(root.get(0).get("strings").size(), is(2));
        assertThat(root.get(0).get("strings").get(0).isTextual(), is(true));
        assertThat(root.get(0).get("strings").get(0).textValue(), is("user"));
        assertThat(root.get(0).get("strings").get(1).isTextual(), is(true));
        assertThat(root.get(0).get("strings").get(1).textValue(), is("looser"));

        assertThat(root.get(0).get("ints").isArray(), is(true));
        assertThat(root.get(0).get("ints").size(), is(2));
        assertThat(root.get(0).get("ints").get(0).isNumber(), is(true));
        assertThat(root.get(0).get("ints").get(0).numberValue(), is(1));
        assertThat(root.get(0).get("ints").get(1).isNumber(), is(true));
        assertThat(root.get(0).get("ints").get(1).numberValue(), is(2));

        assertThat(root.get(0).get("combined").isTextual(), is(true));
        assertThat(root.get(0).get("combined").textValue(), is("testValue"));

        assertThat(root.get(0).equals(root.get(1)), is(true));
        assertThat(root.get(1).equals(root.get(2)), is(true));
    }

    @AllArgsConstructor
    private static class TestPOJO implements Serializable {
        @JsonProperty
        private int id;
        @JsonProperty
        private String name;
        @JsonProperty
        private boolean gender;
        @JsonProperty
        private Object nullable;
    }
}
