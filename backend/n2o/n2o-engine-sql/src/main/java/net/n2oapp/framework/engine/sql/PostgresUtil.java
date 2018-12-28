package net.n2oapp.framework.engine.sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.JsonUtil;
import org.postgresql.util.PGobject;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.criteria.dataset.DataSet;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.List;

/**
 * User: operehod
 * Date: 10.02.13
 * Time: 12:55
 */
public class PostgresUtil {



    public static Object resolveValue(Object object) {
        if (object instanceof PGobject) {
            ObjectMapper mapper = JsonUtil.getMapper();
            PGobject pGobject = (PGobject) object;
            try {
                String body = pGobject.getValue();
                if (body.isEmpty()) return null;

                if (body.startsWith("[{")) {
                    object = mapper.<List<DataSet>>readValue(body,
                            new TypeReference<List<DataSet>>() {
                            });
                } else if (body.startsWith("[")) {
                    object = mapper.<List>readValue(body,
                            new TypeReference<List>() {
                            });
                } else {
                    object = mapper.readValue(body, DataSet.class);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else if (object instanceof Array) {
            try {
                return ((Array) object).getArray();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
        return object;
    }
}
