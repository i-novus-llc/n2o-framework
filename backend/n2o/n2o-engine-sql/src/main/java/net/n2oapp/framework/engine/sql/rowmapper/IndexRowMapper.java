package net.n2oapp.framework.engine.sql.rowmapper;

import net.n2oapp.engine.factory.TypicalEngine;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class IndexRowMapper implements RowMapper<Object[]>, TypicalEngine<String> {
    @Override
    public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object[] result = new Object[rs.getMetaData().getColumnCount()];
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            result[i-1] = rs.getObject(i);
        }
        return result;
    }

    @Override
    public String getType() {
        return "index";
    }
}
