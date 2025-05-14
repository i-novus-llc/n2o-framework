package net.n2oapp.framework.config.register;

import net.n2oapp.framework.config.register.storage.Node;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * User: operhod
 * Date: 08.05.14
 * Time: 10:47
 */
class NodeTest {

    @Test
    void testCalculateLocalPathByLocationPattern() {
        String folderPath =
                "classpath*:META-INF//conf/**/*.xml";
        String absolutePath =
                "jar:file:/C:/n2o-bundle/target/n2o-bundle-1.0-SNAPSHOT/WEB-INF/lib/n2o-config.jar!/META-INF/conf/header/default.application.xml";
        String localPath
                = "header/default.application.xml";
        assertEquals(localPath, Node.calculateLocalPathByLocationPattern(folderPath, absolutePath));
        assertEquals("default.application.xml", Node.calculateLocalPathByLocationPattern("classpath*:META-INF/conf/header/default.application.xml", absolutePath));
    }

    @Test
    void testCalculateLocalPathByDirectoryPath() {
        String folderPath = "C:/Temp";
        String absolutePath = "file:/C:/Temp/header/default.application.xml";
        String localPath = "header/default.application.xml";
        assertEquals(localPath, Node.calculateLocalPathByDirectoryPath(folderPath, absolutePath));

        absolutePath = "C:/Temp/header/default.application.xml";
        assertEquals(localPath, Node.calculateLocalPathByDirectoryPath(folderPath, absolutePath));
    }


    @Test
    void testCreateByLocationPattern() throws Exception {
        Resource resource = new ClassPathResource("net/n2oapp/framework/config/register/test-resource.xml");
        Node node = Node.byLocationPattern(resource, "classpath*:net/n2oapp/framework/**/*.xml");
        assertEquals("config/register/test-resource.xml", node.getLocalPath());
        assertEquals("test-resource.xml", node.getName());
        assertNotNull(node.getFile());
        assertTrue(node.getUri().endsWith("/n2oapp/framework/config/register/test-resource.xml"));
        assertTrue(node.getUri().startsWith("file:"));

        node = Node.byLocationPattern(resource, "classpath*:net\\n2oapp\\framework\\**\\*.xml");
        assertEquals("config/register/test-resource.xml", node.getLocalPath());
        assertEquals("test-resource.xml", node.getName());
        assertNotNull(node.getFile());
        assertTrue(node.getUri().endsWith("/n2oapp/framework/config/register/test-resource.xml"));
        assertTrue(node.getUri().startsWith("file:"));
    }

    @Test
    void testCreateByDirectory() {
        File file = new File("net/n2oapp/framework/config/test-resource.xml");
        Node node = Node.byDirectory(file, "net/n2oapp/framework");
        assertEquals("config/test-resource.xml", node.getLocalPath());
        assertEquals("test-resource.xml", node.getName());
        assertNotNull(node.getFile());
        assertTrue(node.getUri().startsWith("file:"));
        assertTrue(node.getUri().endsWith("net/n2oapp/framework/config/test-resource.xml"));

        file = new File("net/n2oapp/framework/config/test-resource.xml");
        node = Node.byDirectory(file, "net\\n2oapp\\framework\\");
        assertEquals("config/test-resource.xml", node.getLocalPath());
        assertEquals("test-resource.xml", node.getName());
        assertTrue(node.getUri().startsWith("file:"));
        assertTrue(node.getUri().endsWith("net/n2oapp/framework/config/test-resource.xml"));
    }
}