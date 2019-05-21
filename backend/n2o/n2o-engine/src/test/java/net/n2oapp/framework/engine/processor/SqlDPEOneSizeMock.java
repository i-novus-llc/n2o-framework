package net.n2oapp.framework.engine.processor;

import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Заглушка SqlQueryEngine специально подготовленная для тестирования запроса одной записи:
 * {@link net.n2oapp.framework.engine.data.N2oQueryProcessor#executeOneSizeQuery}
 */
public class SqlDPEOneSizeMock implements MapInvocationEngine<N2oSqlDataProvider> {

    private final int returnCount;

    public SqlDPEOneSizeMock(int returnCount) {
        this.returnCount = returnCount;
    }

    @Override
    public Object invoke(N2oSqlDataProvider invocation, Map<String, Object> data) {
        if (!invocation.getQuery().contains("count")) {
            assertThat(invocation.getQuery(), containsString("limit :limit"));
            assertThat(invocation.getQuery(), containsString("offset :offset"));
            assertThat(data.get("limit"), is(2));
            assertThat(data.get("offset"), is(0));
            assertThat(data.get("count"), is(2));
            Object[] record = new Object[]{"Фамилия", null};//id поставит query processor
            List<Object[]> list = new ArrayList<>();
            for (int i = 0; i < returnCount; i++) {
                list.add(record);
            }
            return list;
        } else {
            return new Object[]{ new Object[] {returnCount}};
        }
    }

    @Override
    public Class<? extends N2oSqlDataProvider> getType() {
        return N2oSqlDataProvider.class;
    }
}
