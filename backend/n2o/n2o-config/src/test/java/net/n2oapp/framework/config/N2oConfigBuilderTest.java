package net.n2oapp.framework.config;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.api.config.ConfigBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class N2oConfigBuilderTest {
    @TempDir
    public Path folder;
    private File configJsonFile;
    private File configJsonFile2;

    @BeforeEach
    void setUp() throws Exception {
        configJsonFile = Files.createFile(folder.resolve("config.json")).toFile();
        List<String> configJson = Collections.singletonList("{\"name\" : \"test\", \"version\":\"1.0\", " +
                "\"menu\": {\"items\": {\"items1\": [\"item1\"]}, \"simple\": 1}, \"array\": [1]}");
        Files.write(configJsonFile.toPath(), configJson);

        configJsonFile2 = Files.createFile(folder.resolve("config2.json")).toFile();
        List<String> configJson2 = Collections.singletonList("{\"version\":\"2.0\", \"desc\":\"description\", " +
                "\"menu\": {\"items\": {\"items2\":[\"item2\", \"item3\"]}, \"simple\": 9}, \"array\": [2, 3]}");
        Files.write(configJsonFile2.toPath(), configJson2);
    }

    @Test
    void test() throws IOException {
        ConfigBuilder<TestAppConfig> builder = new N2oConfigBuilder<>(TestAppConfig.class);
        builder.read(configJsonFile);

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
        
        builder.write(configJsonFile);
        String actual = Files.readAllLines(configJsonFile.toPath()).get(0);
        assertThat(actual, containsString("1.0"));
        assertThat(actual, containsString("test"));
        assertThat(actual, containsString("myProp"));
        assertThat(actual, containsString("otherProps"));
    }

    @Test
    void doubleRead() {
        TestAppConfig testAppConfig = new TestAppConfig();
        testAppConfig.setName("testOld");
        ConfigBuilder<TestAppConfig> builder = new N2oConfigBuilder<>(testAppConfig);
        builder.read(configJsonFile);
        builder.read(configJsonFile2);

        TestAppConfig config = builder.get();
        assertThat(config.getName(), is("test"));
        assertThat(config.getProperty("version"), is("2.0"));
        assertThat(config.getProperty("desc"), is("description"));
        assertThat(((HashMap) ((HashMap) config.getProperty("menu")).get("items")).get("items1"), is(Arrays.asList("item1")));
        assertThat(((HashMap) ((HashMap) config.getProperty("menu")).get("items")).get("items2"), is(Arrays.asList("item2", "item3")));
        assertThat(((HashMap) config.getProperty("menu")).get("simple"), is(9));
        assertThat(config.getProperty("array"), is(Arrays.asList(1, 2, 3)));
    }

    @Getter
    @Setter
    public static class TestAppConfig extends AppConfig {
        private String name;
    }
}
