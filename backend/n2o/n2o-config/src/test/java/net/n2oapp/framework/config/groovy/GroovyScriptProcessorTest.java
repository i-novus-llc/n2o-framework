package net.n2oapp.framework.config.groovy;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.util.async.MultiThreadRunner;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.junit.Ignore;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;

/**
 * @author iryabov
 * @since 11.04.2015
 */
public class GroovyScriptProcessorTest {


    @Test
    public void scriptEngine() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("Groovy");
        Bindings bindings = engine.createBindings();
        bindings.put("org", 1);
        Object value = engine.eval(
                "a = 1\n" +
                "a = a + 1\n" +
                "a", bindings);
        System.out.println(value);
        assert 2 == (Integer)value;
        assert bindings.containsKey("a");
        assert bindings.get("a").equals(2);
    }

    @Test
    public void testCollectFromScript() throws Exception {
        String content = FileSystemUtil.getContentByUri("net/n2oapp/framework/config/groovy/testCollectFromScript.groovy");

        //объекты
        Set<N2oObject> objects = GroovyScriptProcessor.collectFromScript(content, N2oObject.class);
        assert objects.size() == 3;
        assert objects.stream().anyMatch(o -> o.getId().equals("object1"));
        assert objects.stream().anyMatch(o -> o.getId().equals("object2"));
        assert objects.stream().anyMatch(o -> o.getId().equals("object3"));

        //страницы
        Set<N2oPage> pages = GroovyScriptProcessor.collectFromScript(content, N2oPage.class);
        assert pages.size() == 2;
        assert pages.stream().anyMatch(p -> p.getId().equals("page1"));
        assert pages.stream().anyMatch(p -> p.getId().equals("page2"));

        //выборка
        Set<N2oQuery> queries = GroovyScriptProcessor.collectFromScript(content, N2oQuery.class);
        assert queries.size() == 1;
        assert queries.iterator().next().getId().equals("query");

        //все
        Set<N2oMetadata> metadataList = GroovyScriptProcessor.collectFromScript(content, N2oMetadata.class);
        assert metadataList.size() == 6;
    }

    @Test
    public void testGetFromScript() throws Exception {
        //читаем из файла
        String content = FileSystemUtil.getContentByUri("net/n2oapp/framework/config/groovy/testGetFromScript.groovy");
        N2oObject result = GroovyScriptProcessor.getFromScript(content, N2oObject.class);
        assert "test".equals(result.getId());
        //пишем скрипт сами
        content = "import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject; object = new N2oObject(id:'test', name: 'Igor')";
        result = GroovyScriptProcessor.getFromScript(content, N2oObject.class);
        assert "test".equals(result.getId());

        //более чем один элемент - ошибка
        String errorContent = FileSystemUtil.getContentByUri("net/n2oapp/framework/config/groovy/testCollectFromScript.groovy");
        assertOnException(() -> GroovyScriptProcessor.getFromScript(errorContent, N2oObject.class), RuntimeException.class);
    }

    @Test
    @Ignore
    public void testThreadSafety() throws ExecutionException, InterruptedException {
        String content = FileSystemUtil.getContentByUri("net/n2oapp/framework/config/groovy/testThreadSafety.groovy");
        MultiThreadRunner runner = new MultiThreadRunner();
        runner.run(() -> {
            String id = "thread" + Thread.currentThread().getId();
            System.out.println("I'm " + id + ", and you?");
            N2oObject result = GroovyScriptProcessor.getFromScript(content, N2oObject.class);
            return result != null && result.getId().equals(id);
        });
    }

}
