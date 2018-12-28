package net.n2oapp.framework.api.metadata.local.view.widget.util;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.util.N2oTestUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author operehod
 * @since 27.04.2015
 */
public class SubModelTest {


    @Test
    public void testSingleListSubModel() {
        //успех
        SingleListFieldSubModelQuery subModelQuery = new SingleListFieldSubModelQuery("gender", "someQuery", "id", "label");
        DataSet dataSet = new DataSet("gender.id", 1);
        TestQueryExecutor queryExecutor = new TestQueryExecutor();
        subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        assert ((Map) dataSet.get("gender")).size() == 3;
        assert dataSet.get("gender.id").equals(1);
        assert dataSet.get("gender.label").equals("someLabel");
        assert dataSet.get("gender.someField").equals("someFieldValue");
        assert queryExecutor.compiledQuery != null;

        //label уже есть
        subModelQuery = new SingleListFieldSubModelQuery("gender", "someQuery", "id", "label");
        dataSet = new DataSet("gender.id", 1).add("gender.label", "someLabel");
        queryExecutor = new TestQueryExecutor();
        subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        assert ((Map) dataSet.get("gender")).size() == 2;
        assert dataSet.get("gender.id").equals(1);
        assert dataSet.get("gender.label").equals("someLabel");
        assert queryExecutor.compiledQuery == null;

        //value == null
        subModelQuery = new SingleListFieldSubModelQuery("gender", "someQuery", "id", "label");
        dataSet = new DataSet("gender.id", null);
        queryExecutor = new TestQueryExecutor();
        subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        assert ((Map) dataSet.get("gender")).size() == 1;
        assert dataSet.get("gender.id") == null;
        assert queryExecutor.compiledQuery == null;

    }

    @Test
    public void testSingleListSubModelError() {
        //в query нету поля для value
        SingleListFieldSubModelQuery subModelQuery = new SingleListFieldSubModelQuery("gender", "someQuery", "wrong", "label");
        DataSet dataSet = new DataSet("gender.wrong", 1);
        TestQueryExecutor queryExecutor = new TestQueryExecutor();
        N2oTestUtil.assertOnException(() -> {
            subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        }, RuntimeException.class);
    }


    @Test
    public void tesMultiListSubModel() {
        //успех
        MultiListFieldSubModelQuery subModelQuery = new MultiListFieldSubModelQuery("gender", "someQuery", "id", "label");
        DataSet dataSet = new DataSet("gender[0].id", 1).add("gender[1].id", 2);
        TestQueryExecutor queryExecutor = new TestQueryExecutor();
        subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        assert ((List) dataSet.get("gender")).size() == 2;
        assert dataSet.get("gender[0].id").equals(1);
        assert dataSet.get("gender[1].id").equals(1);
        assert dataSet.get("gender[0].label").equals("someLabel");
        assert dataSet.get("gender[1].label").equals("someLabel");
        assert dataSet.get("gender[0].someField").equals("someFieldValue");
        assert dataSet.get("gender[1].someField").equals("someFieldValue");
        assert queryExecutor.compiledQuery != null;

        //label уже есть
        subModelQuery = new MultiListFieldSubModelQuery("gender", "someQuery", "id", "label");
        dataSet = new DataSet("gender[0].id", 1).add("gender[1].id", 2).add("gender[0].label", "someLabel").add("gender[1].label", "someLabel");
        queryExecutor = new TestQueryExecutor();
        subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        assert ((List) dataSet.get("gender")).size() == 2;
        assert dataSet.get("gender[0].id").equals(1);
        assert dataSet.get("gender[1].id").equals(2);
        assert dataSet.get("gender[0].label").equals("someLabel");
        assert dataSet.get("gender[1].label").equals("someLabel");
        assert dataSet.get("gender[0].someField") == null;
        assert dataSet.get("gender[1].someField") == null;
        assert queryExecutor.compiledQuery == null;
//
        //value == null
        subModelQuery = new MultiListFieldSubModelQuery("gender", "someQuery", "id", "label");
        dataSet = new DataSet("gender[0].id", null).add("gender[1].id", null);
        queryExecutor = new TestQueryExecutor();
        subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        assert ((List) dataSet.get("gender")).isEmpty();
    }


    @Test
    public void testMultiListSubModelError() {
        //в query нету поля для value
        MultiListFieldSubModelQuery subModelQuery = new MultiListFieldSubModelQuery("gender", "someQuery", "wrong", "label");
        DataSet dataSet = new DataSet("gender[0].wrong", 1);
        TestQueryExecutor queryExecutor = new TestQueryExecutor();
        N2oTestUtil.assertOnException(() -> {
            subModelQuery.applySubModel(dataSet, new TestCompiledQuery("someQuery"), queryExecutor);
        }, RuntimeException.class);
    }


    //моки
    private static class TestCompiledQuery extends CompiledQuery {
        public TestCompiledQuery(String queryId) {
            this.id = queryId;
            this.fieldsMap = new HashMap<>();
            N2oQuery.Field idField = new N2oQuery.Field("id");
            idField.setDomain("integer");
            this.fieldsMap.put("id", idField);
            this.displayFields = Arrays.asList(idField, new N2oQuery.Field("label"), new N2oQuery.Field("someField"));
        }
    }

    private static class TestQueryExecutor implements BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> {
        CompiledQuery compiledQuery;
        N2oPreparedCriteria criteria;

        @Override
        public CollectionPage<DataSet> apply(CompiledQuery compiledQuery, N2oPreparedCriteria criteria) {
            this.compiledQuery = compiledQuery;
            this.criteria = criteria;
            return new CollectionPage<>(1,
                    Arrays.asList(new DataSet("label", "someLabel").add("someField", "someFieldValue").add("id", 1)),
                    criteria);
        }
    }
}
