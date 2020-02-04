package net.n2oapp.framework.boot.sql.rowmapper;

import net.n2oapp.engine.factory.TypicalEngine;
import org.springframework.jdbc.core.ColumnMapRowMapper;

public class MapRowMapper extends ColumnMapRowMapper implements TypicalEngine<String> {

    @Override
    public String getType() {
        return "map";
    }
}
