package net.n2oapp.framework.sandbox.server;

import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = XsdSchemaParser.class,
        properties = {"n2o.sandbox.schemas.path=/schema/"}
)
public class XsdSchemaParserTest {

    @Autowired
    private XsdSchemaParser schemaParser;
    @Autowired
    private ResourceLoader resourceLoader;

    private String pageSchemaNamespace = "http://n2oapp.net/framework/config/schema/test-page-1.0";
    private String buttonSchemaNamespace = "http://n2oapp.net/framework/config/schema/test-button-1.0";
    private String actionSchemaNamespace = "http://n2oapp.net/framework/config/schema/test-action-1.0";

    @Test
    public void testParser() throws IOException {
        String schemaName = "http://n2oapp.net/framework/config/schema/page-3.0";
        Resource schema = schemaParser.getSchema(schemaName);

        // test-action-1.0
        checkResource(schemaParser.getSchema(actionSchemaNamespace), getResource("test-action-1.0"));

        // test-button-1.0 -> test-action-1.0
        checkResource(schemaParser.getSchema(buttonSchemaNamespace), getResource("exp-test-button-1.0"));

        // test-page-1.0 -> test-button-1.0 -> test-action-1.0
        checkResource(schemaParser.getSchema(pageSchemaNamespace), getResource("exp-test-page-1.0"));
    }

    private Resource getResource(String schemaNamespace) throws IOException {
        return Arrays.stream(ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader)
                .getResources("classpath:/schema/" + schemaNamespace + ".xsd"))
                .findFirst().get();
    }

    private void checkResource(Resource actual, Resource expected) throws IOException {
        try (Stream<String> actualStream = new BufferedReader(new InputStreamReader(actual.getInputStream(), StandardCharsets.UTF_8)).lines();
             Stream<String> expectedStream = new BufferedReader(new InputStreamReader(expected.getInputStream(), StandardCharsets.UTF_8)).lines()) {
            List<String> actualLines = actualStream.collect(Collectors.toList());
            List<String> expectedLines = expectedStream.collect(Collectors.toList());
            assertThat(actualLines.size(), is(expectedLines.size()));
            for (int i = 0; i < actualLines.size(); i++)
                assertThat(actualLines.get(i), is(expectedLines.get(i)));
        }
    }
}
