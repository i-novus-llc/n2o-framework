package net.n2oapp.framework.boot.sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.List;

public class PostgresUtil {
    private static final ObjectMapper mapper = new ObjectMapper();


    public static Object resolveValue(Object object) {
        if (object instanceof PGobject pGobject) {
            try {
                String body = pGobject.getValue();
                if (body.isEmpty())
                    return null;
                if (body.startsWith("[{")) {
                    object = mapper.readValue(body, new TypeReference<List<DataSet>>() {});
                } else if (body.startsWith("[")) {
                    object = mapper.readValue(body, new TypeReference<List>() {});
                } else {
                    object = mapper.readValue(body, DataSet.class);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else if (object instanceof Array array) {
            try {
                return array.getArray();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
        return object;
    }
}
