package net.n2oapp.framework.engine.sql.rowmapper;

import net.n2oapp.engine.factory.TypicalEngine;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.stereotype.Component;

@Component
public class MapRowMapper extends ColumnMapRowMapper implements TypicalEngine<String> {

    @Override
    public String getType() {
        return "map";
    }
}
