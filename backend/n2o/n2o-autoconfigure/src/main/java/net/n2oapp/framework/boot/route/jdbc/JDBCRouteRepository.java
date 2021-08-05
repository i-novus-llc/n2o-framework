package net.n2oapp.framework.boot.route.jdbc;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.config.register.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.SerializationUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сохранение и загрузка данных RouteRegister в реляционную БД
 */
public class JDBCRouteRepository implements ConfigRepository<RouteInfoKey, CompileContext> {

    private static final Logger logger = LoggerFactory.getLogger(JDBCRouteRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${n2o.config.register.jdbc.table-name:route_repository}")
    private String tableName;
    @Value("${n2o.config.register.jdbc.create-table:false}")
    private Boolean createTable;

    @Override
    public CompileContext save(RouteInfoKey key, CompileContext value) {
        final String insertSQL = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?)";
        final String updateSQL = "UPDATE " + tableName + " SET context=? WHERE url=? AND class=?";

        final byte[] serialValue = SerializationUtils.serialize(value);
        int cnt = jdbcTemplate.update(updateSQL, serialValue, key.getUrlMatching(), key.getCompiledClass().getName());

        if (cnt < 1) {
            jdbcTemplate.update(insertSQL, UUID.randomUUID().toString(), key.getUrlMatching(),
                    key.getCompiledClass().getName(), serialValue);
            logger.info(String.format("Inserted route: '%s' to [%s]", value, key.getUrlMatching()));
        } else {
            logger.info(String.format("Updated route: '%s' to [%s]", value, key.getUrlMatching()));
        }

        return value;
    }

    @Override
    public void clearAll() {
        final String deleteSQL = "DELETE FROM " + tableName;
        jdbcTemplate.update(deleteSQL);
    }

    @Override
    public Map<RouteInfoKey, CompileContext> getAll() {
        final String selectSQL = "SELECT url, class, context FROM " + tableName;

        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(selectSQL);
        Map<RouteInfoKey, CompileContext> result = resultSet.stream().collect(Collectors.toMap(this::getKey, row ->
                (CompileContext) SerializationUtils.deserialize((byte[]) row.get("context")), (a, b) -> b));

        logger.info(String.format("Returned %s routes.", result.size()));
        return result;
    }

    @PostConstruct
    public void createTable() {
        if (createTable) {
            final String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
                    " (id uuid PRIMARY KEY, url char(255), class char(255), context binary)";

            jdbcTemplate.execute(createTableSQL);
            logger.info(String.format("Created table %s.", tableName));
        }
    }

    private RouteInfoKey getKey(Map<String, Object> map) {
        try {
            Class<? extends Compiled> clazz = Class.forName("" + map.get("class")).asSubclass(Compiled.class);
            return new RouteInfoKey("" + map.get("url"), clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
