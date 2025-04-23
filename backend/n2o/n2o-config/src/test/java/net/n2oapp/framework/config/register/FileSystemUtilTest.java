package net.n2oapp.framework.config.register;

import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.util.FileSystemUtil.getNodesByLocationPattern;

/**
 * @author operehod
 * @since 15.04.2015
 */
class FileSystemUtilTest {

    @Test
    void testGetNodesByLocationPattern() {
        String xmlPath = "classpath*:net/n2oapp/framework/config/register/file/**/*.xml";
        Set<String> tmp;

        // ищем все xml файлы
        List<Node> nodes = getNodesByLocationPattern(xmlPath);
        assert nodes.size() == 3;
        tmp = nodes.stream().map(Node::getLocalPath).collect(Collectors.toSet());
        assert tmp.contains("a/b/c/test.xml");
        assert tmp.contains("b/c/d/test.xml");
        assert tmp.contains("b/c/test.xml");
        // нету файлов
        nodes = getNodesByLocationPattern("classpath*:net/n2oapp/framework/config/register/file/non_exists/**/*.xml");
        assert nodes.size() == 0;
    }

    @Test
    void testRetrieveContent() {
        String path = "classpath:net/n2oapp/framework/config/register/file/a/b/c/test.xml";
        assert FileSystemUtil.getContentByUri(path).equals("<xml/>");

    }
}
