package net.n2oapp.framework.config.register;

import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.util.FileSystemUtil.FILE_NAME_WITHOUT_DOTS;
import static net.n2oapp.framework.config.util.FileSystemUtil.getNodesByLocationPattern;

/**
 * @author operehod
 * @since 15.04.2015
 */
public class FileSystemUtilTest {

    @Test
    void testGetNodesByLocationPattern() {
        String xmlPath = "classpath*:net/n2oapp/framework/config/register/file/**/*.xml";
        String groovyPath = "classpath*:net/n2oapp/framework/config/register/file/**/*.groovy";
        Set<String> tmp;

        // ищем все xml файлы
        List<Node> nodes = FileSystemUtil.getNodesByLocationPattern(xmlPath);
        assert nodes.size() == 3;
        tmp = nodes.stream().map(Node::getLocalPath).collect(Collectors.toSet());
        assert tmp.contains("a/b/c/test.xml");
        assert tmp.contains("b/c/d/test.xml");
        assert tmp.contains("b/c/test.xml");
        // ищем все groovy файлы
        nodes = FileSystemUtil.getNodesByLocationPattern(groovyPath);
        assert nodes.size() == 2;
        tmp = nodes.stream().map(Node::getLocalPath).collect(Collectors.toSet());
        assert tmp.contains("a/b/c/test.groovy");
        assert tmp.contains("b/c/d/test.object.groovy");
        // ищем groovy файлы без точек
        nodes = getNodesByLocationPattern(groovyPath, FILE_NAME_WITHOUT_DOTS);
        assert nodes.size() == 1;
        assert nodes.get(0).getLocalPath().equals("a/b/c/test.groovy");
        // ищем groovy файлы с точками
        nodes = FileSystemUtil.getNodesByLocationPattern("classpath*:net/n2oapp/framework/config/register/file/**/*.*.groovy");
        assert nodes.size() == 1;
        assert nodes.get(0).getLocalPath().equals("b/c/d/test.object.groovy");
        // нету файлов
        nodes = FileSystemUtil.getNodesByLocationPattern("classpath*:net/n2oapp/framework/config/register/file/non_exists/**/*.xml");
        assert nodes.size() == 0;
    }

    @Test
    void testRetrieveContent() {
        String path = "classpath:net/n2oapp/framework/config/register/file/a/b/c/test.xml";
        assert FileSystemUtil.getContentByUri(path).equals("<xml/>");

    }
}
