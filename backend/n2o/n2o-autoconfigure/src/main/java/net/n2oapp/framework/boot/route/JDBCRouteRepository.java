package net.n2oapp.framework.boot.route;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.config.register.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.SerializationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Сохранение и загрузка данных RouteRegister в реляционную БД
 */
@Slf4j
public class JDBCRouteRepository implements ConfigRepository<RouteInfoKey, CompileContext> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${n2o.config.register.jdbc.table-name:route_repository}")
    private String tableName;
    @Value("${n2o.config.register.jdbc.create-table:false}")
    private Boolean createTable;
    @Value("${n2o.config.register.jdbc.retry-count:5}")
    private int retryCount;
    @Value("${n2o.config.register.jdbc.retry-delay-ms:100}")
    private long retryDelayMs;

    @Override
    public CompileContext save(RouteInfoKey key, CompileContext value) {
        final byte[] serialValue = SerializationUtils.serialize(value);

        // При одновременной регистрации маршрутов несколькими экземплярами приложения,
        // конкурентные транзакции могут привести к взаимной блокировке (deadlock).
        // При ошибке блокировки повторяем её несколько раз с нарастающей задержкой.
        final int attempts = Math.max(1, retryCount);
        TransientDataAccessException lastException = null;
        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                write(key, value, serialValue);
                return value;
            } catch (TransientDataAccessException e) {
                lastException = e;
                log.warn(String.format("Failed to save route '%s' due to a lock conflict (attempt %d/%d): %s",
                        key.getUrlMatching(), attempt, attempts, e.getMessage()));
                if (attempt < attempts)
                    sleep(retryDelayMs * attempt);
            }
        }
        throw lastException;
    }

    private void write(RouteInfoKey key, CompileContext value, byte[] serialValue) {
        final String updateSQL = "UPDATE " + tableName + " SET context=? WHERE url=? AND class=?";
        int cnt = jdbcTemplate.update(updateSQL, serialValue, key.getUrlMatching(), key.getCompiledClass().getName());

        if (cnt < 1) {
            final String insertSQL = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertSQL, UUID.randomUUID(), key.getUrlMatching(),
                    key.getCompiledClass().getName(), serialValue);
            log.info(String.format("Inserted route: '%s' to [%s]", value, key.getUrlMatching()));
        } else {
            log.info(String.format("Updated route: '%s' to [%s]", value, key.getUrlMatching()));
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while retrying to save a route", e);
        }
    }

    @Override
    public void clearAll() {
        final String deleteSQL = "DELETE FROM " + tableName;
        jdbcTemplate.update(deleteSQL);
        log.info("Deleted all routes");
    }

    @Override
    public Map<RouteInfoKey, CompileContext> getAll() {
        final String selectSQL = "SELECT url, class, context FROM " + tableName;

        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(selectSQL);
        Map<RouteInfoKey, CompileContext> result = new HashMap<>();
        for (Map<String, Object> row : resultSet) {
            CompileContext<?, ?> context = (CompileContext<?, ?>) SerializationUtils.deserialize((byte[]) row.get("context"));
            result.put(new RouteInfoKey(((String) row.get("url")), context.getCompiledClass()), context);
        }

        log.info(String.format("Returned %s routes.", result.size()));
        return result;
    }

    @PostConstruct
    public void createTable() {
        if (createTable) {
            final String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
                    " (id uuid PRIMARY KEY, url varchar(255), class varchar(255), context bytea)";

            jdbcTemplate.execute(createTableSQL);
            log.info(String.format("Created table %s.", tableName));
        }
    }
}
