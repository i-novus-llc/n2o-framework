package net.n2oapp.framework.ui.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.context.ContextProcessor;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppConfigJsonWriterTest {

    @Test
    public void testResolveValues() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Properties props = new Properties();
        props.put("testProp", "Test_Props");
        AppConfigJsonWriter appConfigJsonWriter = new AppConfigJsonWriter();
        appConfigJsonWriter.setObjectMapper(new ObjectMapper());
        Context context = mock(Context.class);
        List<String> roles = Arrays.asList("user", "looser");
        when(context.get("testContext")).thenReturn("testValue");
        when(context.get("roles")).thenReturn(roles);
        when(context.get("username")).thenReturn("testUsername");
        when(context.get("age")).thenReturn(99);
        when(context.get("isActive")).thenReturn(true);
        ContextProcessor processor = new ContextProcessor(context);
        appConfigJsonWriter.setContextProcessor(processor);
        appConfigJsonWriter.setProperties(props);
        appConfigJsonWriter.setPath("META-INF/config.json");
        appConfigJsonWriter.setOverridePath("META-INF/config-build.json");

        appConfigJsonWriter.loadValues();
        StringWriter sw = new StringWriter();
        appConfigJsonWriter.writeValues(new PrintWriter(sw), new HashMap<>());

        ObjectNode result = (ObjectNode) objectMapper.readTree(sw.toString());
        assertThat(result.get("user").get("username").toString().equals("\"testUsername\""), is(true));
        assertThat(result.get("user").get("roles").toString().equals("[\"user\",\"looser\"]"), is(true));
        assertThat(result.get("user").get("age").toString().equals("99"), is(true));
        assertThat(result.get("user").get("isActive").toString().equals("true"), is(true));
        assertThat(result.get("prop").asText().equals("Test_Props"), is(true));
    }

    @Test
    public void overrideValues() throws IOException {
        AppConfigJsonWriter writer = new AppConfigJsonWriter();
        List<String> configs = Collections.singletonList("{\"test\":{\"inner-value\":123}}");
        writer.setConfigs(configs);
        writer.setObjectMapper(new ObjectMapper());
        Map<String, Object> added = new HashMap<>();
        added.put("test", new Sub("test"));

        StringWriter sw = new StringWriter();
        writer.writeValues(new PrintWriter(sw), added);
        ObjectNode result = (ObjectNode) new ObjectMapper().readTree(sw.toString());
        assertThat(result.get("test").get("inner-value").asInt(), is(123));
        assertThat(result.get("test").get("inner-class").asText(), is("test"));
    }

    public static class Sub {
        @JsonProperty("inner-class")
        private String innerClass;

        public Sub(String innerClass) {
            this.innerClass = innerClass;
        }

        public String getInnerClass() {
            return innerClass;
        }
    }

}
