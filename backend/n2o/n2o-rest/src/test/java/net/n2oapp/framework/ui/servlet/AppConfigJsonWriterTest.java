package net.n2oapp.framework.ui.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.test.TestContextEngine;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.PropertyResolver;
import org.springframework.mock.env.MockEnvironment;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppConfigJsonWriterTest {

    @Test
    void testResolveValues() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PropertyResolver props = new MockEnvironment();
        ((MockEnvironment) props).setProperty("testProp", "Test_Props");
        AppConfigJsonWriter appConfigJsonWriter = new AppConfigJsonWriter();
        appConfigJsonWriter.setObjectMapper(new ObjectMapper());
        Context context = mock(Context.class);
        List<String> roles = Arrays.asList("user", "looser");
        when(context.get("testContext")).thenReturn("testValue");
        when(context.get("roles")).thenReturn(roles);
        when(context.get("username")).thenReturn("testUsername");
        when(context.get("age")).thenReturn(99);
        when(context.get("isActive")).thenReturn(true);
        when(context.get("value")).thenReturn("Value");
        ContextProcessor processor = new ContextProcessor(context);
        appConfigJsonWriter.setContextProcessor(processor);
        appConfigJsonWriter.setPropertyResolver(props);
        appConfigJsonWriter.setPath("META-INF/config.json");
        appConfigJsonWriter.setOverridePath("META-INF/config-build.json");

        StringWriter sw = new StringWriter();
        appConfigJsonWriter.writeValues(new PrintWriter(sw), new HashMap<>());
        ObjectNode result = (ObjectNode) objectMapper.readTree(sw.toString());
        assertThat(result.get("user").get("username").isTextual(), is(true));
        assertThat(result.get("user").get("username").textValue(), is("testUsername"));
        assertThat(result.get("user").get("roles").isArray(), is(true));
        assertThat(result.get("user").get("roles").get(0).isTextual(), is(true));
        assertThat(result.get("user").get("roles").get(0).textValue(), is("user"));
        assertThat(result.get("user").get("roles").get(1).isTextual(), is(true));
        assertThat(result.get("user").get("roles").get(1).textValue(), is("looser"));
        assertThat(result.get("user").get("age").isInt(), is(true));
        assertThat(result.get("user").get("age").intValue(), is(99));
        assertThat(result.get("user").get("isActive").isBoolean(), is(true));
        assertThat(result.get("user").get("isActive").booleanValue(), is(true));
        assertThat(result.get("user").get("combined").isTextual(), is(true));
        assertThat(result.get("user").get("combined").textValue(), is("testValue"));
        assertThat(result.get("prop").isTextual(), is(true));
        assertThat(result.get("prop").textValue(), is("Test_Props"));

        Map<String, Object> values = appConfigJsonWriter.getValues(new HashMap<>());
        assertThat(values, notNullValue());
        assertThat(values.get("user"), notNullValue());
    }

    @Test
    void overrideValues() throws IOException {
        AppConfigJsonWriter writer = new AppConfigJsonWriter();
        TestContextEngine testContextEngine = new TestContextEngine();
        testContextEngine.put("name", "some text \"text in quotes\"");
        ContextProcessor processor = new ContextProcessor(testContextEngine);
        writer.setContextProcessor(processor);
        writer.setObjectMapper(new ObjectMapper());

        List<String> configs = Arrays.asList("{\"test\":{\"inner-value\":123}}", "{\"test2\":{\"inner-value\":\"#{name}\"}}");
        writer.setConfigs(configs);
        StringWriter sw = new StringWriter();
        writer.writeValues(new PrintWriter(sw), Collections.emptyMap());
        ObjectNode result = (ObjectNode) new ObjectMapper().readTree(sw.toString());
        assertThat(result.get("test").get("inner-value").asInt(), is(123));
        assertThat(result.get("test2").get("inner-value").asText(), is("some text \"text in quotes\""));

        Map<String, Object> added = new HashMap<>();
        added.put("test", new Sub("test"));
        added.put("test2", new Sub("test2"));

        sw = new StringWriter();
        writer.writeValues(new PrintWriter(sw), added);
        result = (ObjectNode) new ObjectMapper().readTree(sw.toString());
        assertThat(result.get("test").get("inner-class").asText(), is("test"));
        assertThat(result.get("test2").get("inner-class").asText(), is("test2"));
    }

    public static class Sub {
        @JsonProperty("inner-class")
        private String innerClass;
        @JsonProperty("inner-value")
        private Object innerValue;

        public Sub(String innerClass) {
            this.innerClass = innerClass;
        }

        public String getInnerClass() {
            return innerClass;
        }

        public Object getInnerValue() {
            return innerValue;
        }
    }

}
