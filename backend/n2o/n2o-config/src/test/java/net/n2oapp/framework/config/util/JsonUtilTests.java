package net.n2oapp.framework.config.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.n2oapp.framework.api.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * User: operhod
 * Date: 03.04.14
 * Time: 10:47
 */
public class JsonUtilTests {

    private ObjectMapper objectMapper = new ObjectMapper();
    public static final String path1 = "net/n2oapp/framework/config/util/test1.json";
    public static final String path2 = "net/n2oapp/framework/config/util/test2.json";
    public static final String pathRes = "net/n2oapp/framework/config/util/test-res.json";

    @Test
    void mergeTest() throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        ObjectNode doc1 = (ObjectNode) objectMapper.readTree(cl.getResourceAsStream(path1));
        ObjectNode doc2 = (ObjectNode) objectMapper.readTree(cl.getResourceAsStream(path2));
        JsonNode expectedRes = objectMapper.readTree(cl.getResourceAsStream(pathRes));
        ObjectWriter prettyWriter = objectMapper.writer().withDefaultPrettyPrinter();
        System.out.println("first doc: " + prettyWriter.writeValueAsString(doc1));
        JsonUtil.merge(doc1, doc2);
        assert objectMapper.writeValueAsString(doc1).equals(objectMapper.writeValueAsString(expectedRes));
        System.out.println("second doc: " + prettyWriter.writeValueAsString(doc2));
        System.out.println("result doc: " + prettyWriter.writeValueAsString(doc1));
    }

}
