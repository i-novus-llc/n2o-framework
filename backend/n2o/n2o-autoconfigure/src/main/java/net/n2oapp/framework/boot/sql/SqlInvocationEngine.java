package net.n2oapp.framework.boot.sql;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oSqlQuery;
import net.n2oapp.routing.datasource.JndiRoutingDataSourceTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.boot.sql.InvocationUtil.mapAndListsToJson;

/**
 * Выполнение sql действия. На вход приходит map аргументов, на выход отправляется результат выполненения действия.
 * Это может быть один объект или список.
 */
@Deprecated
public class SqlInvocationEngine extends JdbcInvocationEngine<N2oSqlQuery> {

    public SqlInvocationEngine(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    public SqlInvocationEngine(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                               JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate) {
        super(namedParameterJdbcTemplate, jndiRoutingDataSourceTemplate);
    }

    @Override
    protected List<String> getQueries(N2oSqlQuery invocation) {
        return Arrays.asList(invocation.getQuery().split(";"));
    }

    @Override
    public Class<N2oSqlQuery> getType() {
        return N2oSqlQuery.class;
    }


    @Override
    public Object invoke(N2oSqlQuery invocation, Object data) {
        Map<String, Object> args = new HashMap<>((Map<String, Object>)data);
        mapAndListsToJson(args);
        final List<String> queries = getQueries(invocation);
        final String jndiName = invocation.getDataSource();
        return execute(jndiName, queries, args);
    }
}
