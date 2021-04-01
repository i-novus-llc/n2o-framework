package net.n2oapp.framework.boot.sql.rowmapper;

import net.n2oapp.framework.boot.sql.PostgresUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Преобразует столбцы, вернувшиеся в ответ из postgresql, в мапу объектов
 */
public class PostgresMapRowMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        int columnCount = rs.getMetaData().getColumnCount();
        Map<String, Object> result = new LinkedCaseInsensitiveMap<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String name = JdbcUtils.lookupColumnName(rs.getMetaData(), i);
            result.putIfAbsent(name, PostgresUtil.resolveValue(rs.getObject(i)));
        }
        return result;
    }

}
