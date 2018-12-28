package net.n2oapp.framework.export;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.export.streaming.LargeCollectionPageReader;
import net.n2oapp.properties.StaticProperties;
import net.n2oapp.properties.test.TestStaticProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import java.util.LinkedList;
import java.util.Properties;
import java.util.function.Function;

public class LargeCollectionPageReaderTest {
    @Before
    public void setUp() {
        TestStaticProperties testProperties = new TestStaticProperties();
        testProperties.setProperties(new Properties());
        testProperties.getProperties().put("n2o.ui.export.maxCount", "150");
        ApplicationContext context = Mockito.mock(ApplicationContext.class);
        Mockito.when(context.getBean(StaticProperties.class)).thenReturn(testProperties);
    }

    @Test
    public void test() {

        //1. simple
        QueryRequestInfo request = new QueryRequestInfo();
        request.setCriteria(new N2oPreparedCriteria());
        request.getCriteria().setPage(1);
        request.getCriteria().setSize(100);

        LargeCollectionPageReader reader = new LargeCollectionPageReader(request, new TestExecutor(105), 5);

        for (int i = 0; i < 100; i++) {
            assert reader.hasNext();
            DataSet dataSet = reader.next();
            assert dataSet.get("num").equals(i);
            assert dataSet.get("page").equals(i / 5 + 1);
            assert dataSet.get("size").equals(5);
        }

        assert !reader.hasNext();

        //2. получить всё
        request = new QueryRequestInfo();
        request.setCriteria(new N2oPreparedCriteria());
        request.getCriteria().setPage(1);
        request.getCriteria().setSize(-1);

        reader = new LargeCollectionPageReader(request, new TestExecutor(95), 5);

        for (int i = 0; i < 95; i++) {
            assert reader.hasNext();
            DataSet dataSet = reader.next();
            assert dataSet.get("num").equals(i);
            assert dataSet.get("page").equals(i / 5 + 1);
            assert dataSet.get("size").equals(5);
        }

        assert !reader.hasNext();

        // получить максимальное количество
        reader = new LargeCollectionPageReader(request, new TestExecutor(200), 5);
        for (int i = 0; i < 150; i++) {
            assert reader.hasNext();
            DataSet dataSet = reader.next();
            assert dataSet.get("num").equals(i);
            assert dataSet.get("page").equals(i / 5 + 1);
            assert dataSet.get("size").equals(5);
        }

        assert !reader.hasNext();
    }

    private static class TestExecutor implements Function<QueryRequestInfo, LinkedList<DataSet>> {
        public TestExecutor(int count) {
            this.count = count;
        }

        @Override
        public LinkedList<DataSet> apply(QueryRequestInfo request) {
            LinkedList<DataSet> res = new LinkedList<DataSet>();
            N2oPreparedCriteria criteria = request.getCriteria();
            Integer len = 0;
            if (criteria.getPage() * criteria.getSize() <= count) {
                len = criteria.getSize();
            } else {
                len = count - criteria.getSize() * (criteria.getPage() - 1);
            }

            for (int i = 0; i < len; i++) {
                res.add(new DataSet("size", criteria.getSize()).add("page", criteria.getPage()).add("num", counter++));
            }

            return res;
        }

        public int getCounter() {
            return counter;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }

        private int count;
        private int counter;
    }
}
