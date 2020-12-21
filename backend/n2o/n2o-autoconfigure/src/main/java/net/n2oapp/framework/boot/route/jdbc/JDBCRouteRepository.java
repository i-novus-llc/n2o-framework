package net.n2oapp.framework.boot.route.jdbc;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.config.register.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.function.Predicate;

/**
 * Сохранение и загрузка данных RouteRegister в БД
 */
public class JDBCRouteRepository implements ConfigRepository<RouteInfoKey, CompileContext> {

    private static final Logger logger = LoggerFactory.getLogger(JDBCRouteRepository.class);

    @Autowired(required = false)
    private DataSource dataSource;

    @Value("${n2o.config.register.jdbc.table-name:route_repository}")
    private String tableName;
    @Value("${n2o.config.register.jdbc.create-table:false}")
    private Boolean createTable;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String jdbcUser;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @Override
    public CompileContext save(RouteInfoKey key, CompileContext value) {
        String insertSQL = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?)";
        String updateSQL = "UPDATE " + tableName + " SET context=? WHERE url=? AND class=?";

        try (Connection connection = getNewConnection()) {
            final byte[] serialValue = SerializationUtils.serialize(value);
            PreparedStatement statement = connection.prepareStatement(updateSQL);
            statement.setBytes(1, serialValue);
            statement.setString(2, key.getUrlMatching());
            statement.setString(3, key.getCompiledClass().getName());
            int cnt = statement.executeUpdate();
            statement.close();

            if (cnt < 1) {
                statement = connection.prepareStatement(insertSQL);
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, key.getUrlMatching());
                statement.setString(3, key.getCompiledClass().getName());
                statement.setBytes(4, serialValue);
                statement.executeUpdate();
                statement.close();
                logger.info(String.format("Inserted route: '%s' to [%s]", value, key.getUrlMatching()));
            } else {
                logger.info(String.format("Updated route: '%s' to [%s]", value, key.getUrlMatching()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return value;
    }

    @Override
    public void clear(Predicate<? super RouteInfoKey> filter) {
        String selectSQL = "SELECT id, url, class FROM " + tableName;
        String deleteSQL = "DELETE FROM " + tableName + " WHERE id in (?)";
        try (Connection connection = getNewConnection()) {
            PreparedStatement statement = connection.prepareStatement(selectSQL);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            StringBuilder deleteList = new StringBuilder();
            while (resultSet.next()) {
                RouteInfoKey key = getKey(resultSet.getString(2), resultSet.getString(3));
                if (filter.test(key)) deleteList.append(resultSet.getString(1)).append(",");
            }
            statement.close();
            if (deleteList.length() > 1) {
                deleteList.setLength(deleteList.length() - 1);
                statement = connection.prepareStatement(deleteSQL);
                statement.setString(1, deleteList.toString());
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<RouteInfoKey, CompileContext> getAll() {
        String selectSQL = "SELECT url, class, context FROM " + tableName;
        try (Connection connection = getNewConnection()) {
            PreparedStatement statement = connection.prepareStatement(selectSQL);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            Map<RouteInfoKey, CompileContext> result = new HashMap<>();
            while (resultSet.next()) {
                RouteInfoKey key = getKey(resultSet.getString(1), resultSet.getString(2));
                CompileContext context = (CompileContext) SerializationUtils.deserialize(resultSet.getBytes(3));
                result.put(key, context);
            }
            statement.close();
            logger.info(String.format("Returned %s routes.", result.size()));
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @PostConstruct
    public void createTable() {
        if (!createTable) return;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (id uuid PRIMARY KEY, url char(255), class char(255), context binary)";

        try (Connection connection = getNewConnection()) {
            PreparedStatement statement = connection.prepareStatement(createTableSQL);
            statement.executeUpdate();
            statement.close();
            logger.info(String.format("Created table %s.", tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRecordCount() {
        String selectSQL = "SELECT count(id) FROM " + tableName;
        try (Connection connection = getNewConnection()) {
            PreparedStatement statement = connection.prepareStatement(selectSQL);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private RouteInfoKey getKey(String url, String className) {
        try {
            Class<? extends Compiled> clazz = Class.forName(className).asSubclass(Compiled.class);
            return new RouteInfoKey(url, clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private Connection getNewConnection() throws SQLException {
        if (dataSource != null) return dataSource.getConnection();
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }
}
