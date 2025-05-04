package net.n2oapp.framework.config.register;

import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.util.FileSystemUtil.getNodesByLocationPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(3, nodes.size());
        tmp = nodes.stream().map(Node::getLocalPath).collect(Collectors.toSet());
        assertTrue(tmp.contains("a/b/c/test.xml"));
        assertTrue(tmp.contains("b/c/d/test.xml"));
        assertTrue(tmp.contains("b/c/test.xml"));

        // нет файлов
        nodes = getNodesByLocationPattern("classpath*:net/n2oapp/framework/config/register/file/non_exists/**/*.xml");
        assertEquals(0, nodes.size());
    }

    @Test
    void testRetrieveContent() {
        String path = "classpath:net/n2oapp/framework/config/register/file/a/b/c/test.xml";
        assertEquals("<xml/>", FileSystemUtil.getContentByUri(path));
    }
}
