package net.n2oapp.framework.engine.sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.context.CacheTemplateByMapMock;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.PGobject;
import org.springframework.context.ApplicationContext;
import net.n2oapp.context.StaticSpringContext;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: operhod
 * Date: 18.04.14
 * Time: 14:10
 */
public class PostgresUtilTest {

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean("n2oObjectMapper", ObjectMapper.class)).thenReturn(new ObjectMapper());
        StaticSpringContext staticSpringContext = new StaticSpringContext();
        staticSpringContext.setApplicationContext(applicationContext);
        staticSpringContext.setCacheTemplate(new CacheTemplateByMapMock());
    }

    @Test
    public void testGetObject() throws Exception {
        PGobject pGobject = new PGobject();
        pGobject.setType("json");
        //объект
        pGobject.setValue("{\"id\":1, \"name\":\"oleg\"}");
        Map<String, Object> res = (Map<String, Object>) PostgresUtil.resolveValue(pGobject);
        assert res.get("id") == Integer.valueOf(1);
        assert res.get("name").equals("oleg");
        //массив
        pGobject.setValue("[{\"id\":1, \"name\":\"oleg\"}]");
        res = (Map<String, Object>) ((List) PostgresUtil.resolveValue((pGobject))).get(0);
        assert res.get("id") == Integer.valueOf(1);
        assert res.get("name").equals("oleg");
        // массив примитивов
        pGobject.setValue("[0,1,2]");
        List list = ((List) PostgresUtil.resolveValue((pGobject)));
        assert list.size() == 3;
        assert list.get(0).equals(0);
        assert list.get(1).equals(1);
        assert list.get(2).equals(2);
        pGobject.setValue("[\"0\"]");
        list = ((List) PostgresUtil.resolveValue((pGobject)));
        assert list.size() == 1;
        assert list.get(0).equals("0");


    }
}
