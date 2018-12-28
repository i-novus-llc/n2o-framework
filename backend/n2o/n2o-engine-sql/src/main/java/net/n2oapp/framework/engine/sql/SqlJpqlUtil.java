package net.n2oapp.framework.engine.sql;

import net.n2oapp.criteria.api.util.QueryBlank;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static net.n2oapp.criteria.api.util.NamedParameterUtils.prepareQuery;

/**
 * User: iryabov
 * Date: 21.02.13
 * Time: 12:20
 */
@Deprecated
public class SqlJpqlUtil {

    public static Object[] executeQueries(NamedParameterJdbcTemplate template, List<String> queries, Map<String, Object> args) {

        queries = queries.stream().map(String::trim).filter(q -> !q.isEmpty()).collect(toList());

        Object[] res = new Object[queries.size()];

        for (int i = 0; i < queries.size(); i++) {
            try {
                res[i] = executeQuery(template, args, queries.get(i));
            } catch (Exception e) {
                String summary = InvocationUtil.findSqlSummary(e);
                if (summary != null) {
                    throw new N2oUserException(summary);
                }
                throw new N2oException("SQL:" + queries.get(i)+ " Args:" + args, e);
            }
        }
        return res;
    }

    private static Object executeQuery(NamedParameterJdbcTemplate template, Map<String, Object> args, String query) {
        QueryBlank queryBlank = prepareQuery(query, args);
        query = queryBlank.getQuery();
        args = queryBlank.getArgs();
        if (isSelect(query)) {
            List<Object[]> list = template.query(query, args, (rs, rowNum) -> {
                return retrieveDataSet(rs);
            });
            return list.toArray();
        } else {
            MapSqlParameterSource paramSource = new MapSqlParameterSource(args);
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            template.update(query, paramSource, generatedKeyHolder);
            List<Map<String, Object>> keyList = generatedKeyHolder.getKeyList();
            if (keyList != null) {
                if (keyList.size() > 1)  {
                    List<Object> rows = new ArrayList<>(keyList.size());
                    for (Map<String, Object> row : keyList) {
                        rows.add(row.values().toArray());
                    }
                    return rows.toArray();
                } else if (keyList.size() == 1) {
                    return keyList.get(0).values().toArray();
                }
            }
        }
        return null;
    }

    private static boolean isSelect(String q) {
        q = q.toLowerCase();
        return (q.startsWith("select"));
    }


    public static Object[] retrieveDataSet(ResultSet resultSet) throws SQLException {
        int count = resultSet.getMetaData().getColumnCount();
        Object[] res = new Object[count];
        for (int i = 1; i <= count; i++) {
            res[i - 1] = resultSet.getObject(i);
        }
        return res;
    }


}
