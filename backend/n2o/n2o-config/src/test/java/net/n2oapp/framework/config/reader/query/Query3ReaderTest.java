package net.n2oapp.framework.config.reader.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oJavaMethod;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oRestInvocation;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения запроса версии 2.0 из xml файла
 */
public class Query3ReaderTest {

    @Test
    public void testQueryReadWithSql() {
        N2oQuery query = new SelectiveStandardReader()
                .addReader(new QueryElementReaderV3())
                .readByPath("net/n2oapp/framework/config/reader/query/testQueryElementReaderV3withSql.query.xml");

        assert query.getName().equals("test");
        assert query.getObjectId().equals("blank");
        assert query.getLists().length == 1;
        assert query.getLists()[0].getInvocation() instanceof N2oSqlDataProvider;
        assert ((N2oSqlDataProvider)query.getLists()[0].getInvocation()).getQuery().equals("select items limit :limit offset :offset");
        assert query.getCounts().length == 1;
        assert query.getCounts()[0].getInvocation() instanceof N2oSqlDataProvider;
        assert ((N2oSqlDataProvider)query.getCounts()[0].getInvocation()).getQuery().equals("select count");
        assert query.getFields()[0].getExpression().equals("aa.id");
        assert query.getUniques() == null;
        assert query.getFields()[0].getId().equals("id");
        assert query.getFields()[0].getName().equals("test");
        assert query.getFields()[0].getDomain().equals("integer");
        assert query.getFields()[0].getSelectBody().equals("test");
        assert query.getFields()[0].getSortingBody().equals("test :idDirection");
        assert query.getFields()[0].getSortingMapping().equals("['idDirection']");
        assert query.getFields()[0].getJoinBody().equals("table1");
        assert query.getFields()[0].getFilterList()[0].getType().equals(FilterType.eq);
        assert query.getFields()[0].getFilterList()[0].getFilterField().equals("id");
        assert query.getFields()[0].getFilterList()[0].getDefaultValue().equals("test");
        assert query.getFields()[0].getFilterList()[0].getDomain().equals("integer");
        assert query.getFields()[0].getFilterList()[0].getNormalize().equals("#this.toLowerCase()");
        assert query.getFields()[0].getFilterList()[0].getText().equals("test");
        assert query.getFields()[1].getSortingBody().equals(":expression :idDirection");
    }

    @Test
    public void testQueryReadWithRest() {
        N2oQuery query = new SelectiveStandardReader()
                .addReader(new QueryElementReaderV3())
                .readByPath("net/n2oapp/framework/config/reader/query/testQueryElementReaderV3withRest.query.xml");

        assert query.getName().equals("test");
        assert query.getObjectId().equals("blank");
        assert query.getLists().length == 1;
        assert query.getLists()[0].getInvocation() instanceof N2oRestInvocation;
        assert query.getLists()[0].getResultMapping().equals("['collection']");
        assert query.getLists()[0].getCountMapping().equals("['count']");
        assert ((N2oRestInvocation)query.getLists()[0].getInvocation()).getQuery().equals("query");
        assert ((N2oRestInvocation)query.getLists()[0].getInvocation()).getProxyHost().equals("host");
        assert ((N2oRestInvocation)query.getLists()[0].getInvocation()).getProxyPort().equals(2222);
        assert ((N2oRestInvocation)query.getLists()[0].getInvocation()).getDateFormat().equals("dd.MM.yyyy");
        assert ((N2oRestInvocation)query.getLists()[0].getInvocation()).getErrorMapping().getMessage().equals("error-message");
        assert ((N2oRestInvocation)query.getLists()[0].getInvocation()).getErrorMapping().getDetailedMessage().equals("error-detail");
        assert ((N2oRestInvocation)query.getLists()[0].getInvocation()).getErrorMapping().getStackTrace().equals("error-stack-trace");
        assert query.getUniques().length == 1;
        assert query.getUniques()[0].getResultMapping().equals("#this");
        assert query.getUniques()[0].getCountMapping() == null;
        assert query.getUniques()[0].getInvocation() instanceof N2oRestInvocation;
        assert ((N2oRestInvocation)query.getUniques()[0].getInvocation()).getQuery().equals("query-by-id");
        assert ((N2oRestInvocation)query.getUniques()[0].getInvocation()).getProxyHost().equals("host");
        assert ((N2oRestInvocation)query.getUniques()[0].getInvocation()).getProxyPort().equals(2222);
        assert ((N2oRestInvocation)query.getUniques()[0].getInvocation()).getDateFormat().equals("dd.MM.yyyy");
        assert ((N2oRestInvocation)query.getUniques()[0].getInvocation()).getErrorMapping().getMessage().equals("error-message");
        assert ((N2oRestInvocation)query.getUniques()[0].getInvocation()).getErrorMapping().getDetailedMessage().equals("error-detail");
        assert ((N2oRestInvocation)query.getUniques()[0].getInvocation()).getErrorMapping().getStackTrace().equals("error-stack-trace");
        assert query.getCounts() == null;
    }

    @Test
    public void testQueryReadWithJavaCriteria() {
        N2oQuery query = new SelectiveStandardReader()
                .addReader(new QueryElementReaderV3())
                .readByPath("net/n2oapp/framework/config/reader/query/testQueryElementReaderV3withJavaCriteria.query.xml");

        assert query.getName().equals("test");
        assert query.getObjectId().equals("blank");
        assert query.getLists().length == 1;
        assert query.getLists()[0].getInvocation() instanceof N2oJavaMethod;
        assert ((N2oJavaMethod)query.getLists()[0].getInvocation()).getBean().equals("bean");
        assert ((N2oJavaMethod)query.getLists()[0].getInvocation()).getArguments()[0].getClassName().equals("class");
        assert query.getCounts() == null;
        assert query.getUniques() == null;
    }


}
