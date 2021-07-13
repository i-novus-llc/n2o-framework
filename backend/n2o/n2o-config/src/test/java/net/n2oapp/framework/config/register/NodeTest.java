package net.n2oapp.framework.config.register;

import net.n2oapp.framework.config.register.storage.Node;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

/**
 * User: operhod
 * Date: 08.05.14
 * Time: 10:47
 */
public class NodeTest {
    @Test
    public void testCalculateLocalPathByLocationPattern() throws Exception {

        String folderPath =
                "classpath*:META-INF/conf/**/*.xml";
        String absolutePath =
                "jar:file:/C:/n2o-bundle/target/n2o-bundle-1.0-SNAPSHOT/WEB-INF/lib/n2o-config.jar!/META-INF/conf/header/default.application.xml";
        String localPath
                = "header/default.application.xml";
        assert localPath.equals(Node.calculateLocalPathByLocationPattern(folderPath, absolutePath));

        assert Node.calculateLocalPathByLocationPattern("classpath*:META-INF/conf/header/default.application.xml", absolutePath).equals("default.application.xml");

    }


    @Test
    public void testCalculateLocalPathByDirectoryPath() throws Exception {

        String folderPath = "C:/Temp";
        String absolutePath = "file:/C:/Temp/header/default.application.xml";
        String localPath = "header/default.application.xml";
        assert localPath.equals(Node.calculateLocalPathByDirectoryPath(folderPath, absolutePath));

        absolutePath = "C:/Temp/header/default.application.xml";
        assert localPath.equals(Node.calculateLocalPathByDirectoryPath(folderPath, absolutePath));

    }


    @Test
    public void testCreateByLocationPattern() throws Exception {
        Resource resource = new ClassPathResource("net/n2oapp/framework/config/register/test-resource.xml");
        Node node = Node.byLocationPattern(resource, "classpath*:net/n2oapp/framework/**/*.xml");
        assert node.getLocalPath().equals("config/register/test-resource.xml");
        assert node.getName().equals("test-resource.xml");
        assert node.getFile() != null;
        assert node.getURI().endsWith("/n2oapp/framework/config/register/test-resource.xml");
        assert node.getURI().startsWith("file:");

        node = Node.byLocationPattern(resource, "classpath*:net\\n2oapp\\framework\\**\\*.xml");
        assert node.getLocalPath().equals("config/register/test-resource.xml");
        assert node.getName().equals("test-resource.xml");
        assert node.getFile() != null;
        assert node.getURI().endsWith("/n2oapp/framework/config/register/test-resource.xml");
        assert node.getURI().startsWith("file:");
    }

    @Test
    public void testCreateByDirectory() throws Exception {
        File file = new File("net/n2oapp/framework/config/test-resource.xml");
        Node node = Node.byDirectory(file, "net/n2oapp/framework");
        assert node.getLocalPath().equals("config/test-resource.xml");
        assert node.getName().equals("test-resource.xml");
        assert node.getFile() != null;
        assert node.getURI().startsWith("file:");
        assert node.getURI().endsWith("net/n2oapp/framework/config/test-resource.xml");

        file = new File("net/n2oapp/framework/config/test-resource.xml");
        node = Node.byDirectory(file, "net\\n2oapp\\framework\\");
        assert node.getLocalPath().equals("config/test-resource.xml");
        assert node.getName().equals("test-resource.xml");
        assert node.getURI().startsWith("file:");
        assert node.getURI().endsWith("net/n2oapp/framework/config/test-resource.xml");
    }

}
