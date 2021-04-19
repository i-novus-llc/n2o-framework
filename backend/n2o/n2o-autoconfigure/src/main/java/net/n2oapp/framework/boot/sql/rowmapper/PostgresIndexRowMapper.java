package net.n2oapp.framework.boot.sql.rowmapper;

import net.n2oapp.framework.boot.sql.PostgresUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Преобразует столбцы, вернувшиеся в ответ из postgresql, в массив объектов
 */
public class PostgresIndexRowMapper implements RowMapper<Object[]> {

    @Override
    public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object[] result = new Object[rs.getMetaData().getColumnCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = PostgresUtil.resolveValue(rs.getObject(i + 1));
        }
        return result;
    }

}
