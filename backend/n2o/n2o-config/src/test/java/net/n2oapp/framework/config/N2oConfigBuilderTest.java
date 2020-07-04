package net.n2oapp.framework.config;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.api.config.ConfigBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class N2oConfigBuilderTest {
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        File configJsonFile = folder.newFile("config.json");
        List<String> configJson = Collections.singletonList("{\"name\" : \"test\", \"version\":\"1.0\"}");
        Files.write(configJsonFile.toPath(), configJson);

        File configJsonFile2 = folder.newFile("config2.json");
        List<String> configJson2 = Collections.singletonList("{\"version\":\"2.0\", \"desc\":\"description\"}");
        Files.write(configJsonFile2.toPath(), configJson2);
    }

    @Test
    public void test() throws IOException {
        ConfigBuilder<TestAppConfig> builder = new N2oConfigBuilder<>(TestAppConfig.class);
        builder.read(new File(folder.getRoot(), "config.json"));

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("prop1", 1);
        props.put("prop2", "value2");
        builder.add("myProp", props);

        Map<String, Object> props2 = new LinkedHashMap<>();
        props2.put("otherProps", "other");
        builder.addAll(props2);

        TestAppConfig config = builder.get();
        assertThat(config.getName(), is("test"));
        assertThat(config.getProperties().get("version"), is("1.0"));
        assertThat(config.getProperties().get("myProp"), is(props));
        assertThat(config.getProperties().get("otherProps"), is("other"));

        File newFile = new File(folder.getRoot(), "config.json");
        builder.write(newFile);
        String actual = Files.readAllLines(newFile.toPath()).get(0);
        assertThat(actual, containsString("1.0"));
        assertThat(actual, containsString("test"));
        assertThat(actual, containsString("myProp"));
        assertThat(actual, containsString("otherProps"));
    }

    @Test
    public void doubleRead() throws IOException {
        TestAppConfig testAppConfig = new TestAppConfig();
        testAppConfig.setName("testOld");
        ConfigBuilder<TestAppConfig> builder = new N2oConfigBuilder<>(testAppConfig);
        builder.read(new File(folder.getRoot(), "config.json"));
        builder.read(new File(folder.getRoot(), "config2.json"));

        TestAppConfig config = builder.get();
        assertThat(config.getName(), is("test"));
        assertThat(config.getProperty("version"), is("2.0"));
        assertThat(config.getProperty("desc"), is("description"));
    }

    @Getter
    @Setter
    public static class TestAppConfig extends AppConfig {
        private String name;
    }
}
