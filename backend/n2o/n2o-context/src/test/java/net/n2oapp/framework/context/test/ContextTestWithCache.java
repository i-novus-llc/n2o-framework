package net.n2oapp.framework.context.test;

import net.n2oapp.framework.api.context.ContextEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-smart-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ContextTestWithCache {

    @Autowired
    private ContextEngine contextEngine;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    // проверка независимых провайдеров
    public void testCachedEngine() throws Exception {
        // получаем в первый раз, значение в кэше нет, идет обращение к провайдеру.
        MockCache.clearStatistics();
        assert contextEngine.get("org.id").equals(1);
        assert MockCache.miss == 1: MockCache.miss;//попытались взять из кеша, но там нет
        assert MockCache.hit == 0: MockCache.hit;
        assert MockCache.put == 1: MockCache.put;//вставил в кеш org.id
        MockCache.clearStatistics();
        assert contextEngine.get("org.name").equals("1");
        assert MockCache.hit == 1: MockCache.hit;//обратился к org.id, а он в кеше
        assert MockCache.miss == 1: MockCache.miss;//обратился к org.name, а он в кеше
        assert MockCache.put == 1: MockCache.put;//вставил в кеш org.name

        // получаем во второй раз, значение есть в кэше, провайдер не трогаем.
        MockCache.clearStatistics();
        assert contextEngine.get("org.id").equals(1);
        assert MockCache.hit == 1: MockCache.hit;//org.id в кеше
        assert MockCache.miss == 0: MockCache.miss;
        assert MockCache.put == 0: MockCache.put;
        MockCache.clearStatistics();
        assert contextEngine.get("org.name").equals("1");
        assert MockCache.hit == 2: MockCache.hit;//дважды попадаем в кэш, получая org.id и org.name
        assert MockCache.miss == 0;
        assert MockCache.put == 0;

        //сетим новые значения
        MockCache.clearStatistics();
        Map<String, Object> values = new HashMap<>();
        values.put("org.id", 2);
        values.put("dep.id", 3);
        contextEngine.set(values);
        assert MockCache.put == 2;//засунули в кеш org.id и dep.id
        assert MockCache.hit == 0;
        assert MockCache.miss == 0;

        // проверяем что ни для одного из параметров не идет обращение к провайдеру, все уже в кэше
        MockCache.clearStatistics();
        assert contextEngine.get("org.id").equals(2);
        assert contextEngine.get("dep.id").equals(3);
        assert MockCache.hit == 2;//все в кеше
        assert MockCache.miss == 0;
        assert MockCache.put == 0;

        // проверяем что зависящие значения пересчитались и обновленные добавились в кэш
        MockCache.clearStatistics();
        assert contextEngine.get("org.name").equals("2");
        assert contextEngine.get("dep.name").equals("3");
        assert MockCache.hit == 2;//нашли в кеше org.id и dep.id
        assert MockCache.miss == 2: MockCache.miss;//не нашли в кеше org.name, потому что org.id изменилось значение, а к dep.name даже не обращались ранее
        assert MockCache.put == 2;//вставили в кеш org.name и dep.name

        // проверяем одиночный set, а потом get
        MockCache.clearStatistics();
        contextEngine.set("cab.id", 8);
        assert MockCache.put == 1;
        assert MockCache.hit == 0;
        assert MockCache.miss == 0;
        MockCache.clearStatistics();
        assert contextEngine.get("cab.id").equals(8);
        assert MockCache.put == 0;
        assert MockCache.hit == 1;
        assert MockCache.miss == 0;
    }

    @Test
    public void testBaseParams() throws Exception {
        // получаем в первый раз, значение в кэше нет, идет обращение к провайдеру.
        MockCache.clearStatistics();
        assert contextEngine.get("dep.fullName").equals("11");
        assert MockCache.miss == 5: MockCache.miss;//попытались взять из кеша: dep.fullName, org.name, org.id, dep.name, dep.id
        assert MockCache.hit == 0: MockCache.hit;
        assert MockCache.put == 5: MockCache.put;

    }
}

