package net.n2oapp.framework.engine.rest;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oRestInvocation;
import net.n2oapp.properties.test.TestStaticProperties;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Тестирование вызова rest
 */
public class RestInvocationEngineTest {
    @Test
    public void testSimple() throws Exception {
        Properties properties = new Properties();
        properties.put("n2o.engine.mapper","spel");
        new TestStaticProperties().setProperties(properties);
        //самый простой случай
        DataSet req = new DataSet();
        req.put("id", 1);
        req.put("name", "test");
        TestRestClient restClient = new TestRestClient(req);
        RestInvocationEngine actionEngine = new RestInvocationEngine(restClient, new RestProcessingEngine());
        N2oRestInvocation invocation = new N2oRestInvocation();
        invocation.setQuery("http://www.someUrl.org/{id}");
        invocation.setMethod("POST");
        Map<String, Object> request = new HashMap<>();
        request.put("id", 1);
        DataSet result = (DataSet) actionEngine.invoke(invocation, request);
        assert result.size() == 2;
        assert result.get("id").equals(1);
        assert result.get("name").equals("test");
        assert restClient.getQuery().getPath().equals("http://www.someUrl.org/1");

        //случай с повторением параметра
        restClient = new TestRestClient(req);
        actionEngine = new RestInvocationEngine(restClient, new RestProcessingEngine());
        invocation = new N2oRestInvocation();
        invocation.setQuery("http://www.someUrl.org/{id}/{id}");
        invocation.setMethod("POST");
        result = (DataSet) actionEngine.invoke(invocation, request);
        assert result.get("id").equals(1);
        assert result.get("name").equals("test");
        assert restClient.getQuery().getPath().equals("http://www.someUrl.org/1/1");
    }

    @Test
    public void testDatasetMapper() throws Exception {
        Properties properties = new Properties();
        properties.put("n2o.engine.mapper","dataset");
        new TestStaticProperties().setProperties(properties);
        DataSet req = new DataSet();
        req.put("id", 1);
        req.put("name", "test");
        req.put("child.id", 2);
        req.put("child.name", "test2");
        TestRestClient restClient = new TestRestClient(req);
        RestInvocationEngine actionEngine = new RestInvocationEngine(restClient, new RestProcessingEngine());
        N2oRestInvocation invocation = new N2oRestInvocation();
        invocation.setQuery("http://www.someUrl.org/{id}");
        invocation.setMethod("POST");
        Map<String, Object> request = new HashMap<>();
        request.put("id", 1);
        DataSet result = (DataSet) actionEngine.invoke(invocation, request);
        assert result.size() == 3;
        assert result.get("id").equals(1);
        assert result.get("name").equals("test");
        assert result.get("child.id").equals(2);
        assert result.get("child.name").equals("test2");
        assert restClient.getQuery().getPath().equals("http://www.someUrl.org/1");
    }
}
