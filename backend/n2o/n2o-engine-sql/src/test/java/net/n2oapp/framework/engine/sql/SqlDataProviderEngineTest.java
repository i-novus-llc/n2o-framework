package net.n2oapp.framework.engine.sql;

import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.engine.sql.rowmapper.IndexRowMapper;
import net.n2oapp.framework.engine.sql.rowmapper.MapRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Тесты Sql провайдера данных
 */
public class SqlDataProviderEngineTest {

    private SqlDataProviderEngine engine;
    private IndexRowMapper defaultRowMapper;
    private NamedParameterJdbcTemplate jdbc;


    @Before
    public void setUp() throws Exception {
        engine = new SqlDataProviderEngine();
        defaultRowMapper = new IndexRowMapper();
        engine.setRowMapperFactory(type -> defaultRowMapper);
        jdbc = Mockito.mock(NamedParameterJdbcTemplate.class);
        engine.setNamedParameterJdbcTemplate(jdbc);
        engine.setResourceLoader(new DefaultResourceLoader());
    }

    /**
     * Простой тест на замену плейсхолдеров :select :filters и т.д.
     */
    @Test
    public void replacePlaceholders() {
        N2oSqlDataProvider provider = new N2oSqlDataProvider();
        provider.setQuery("select :count, :select " +
                "from table :join " +
                "where :filters " +
                "order by :sorting " +
                "limit :limit offset :offset");
        Map<String, Object> args = new HashMap<>();
        args.put("select", Arrays.asList("id", "name"));
        args.put("join", Arrays.asList("join table2", "join table3"));
        args.put("filters", Arrays.asList("id=:id", "name=:name"));
        args.put("sorting", Arrays.asList("id :idDirection", "name :nameDirection"));
        args.put("limit", 1);
        args.put("offset", 2);
        args.put("count", 3);
        args.put("id", 123);
        args.put("name", "test");
        args.put("idDirection", "ASC");
        args.put("nameDirection", "DESC");

        engine.invoke(provider, args);
        Mockito.verify(jdbc).query(Matchers.eq("select 3, id, name " +
                        "from table join table2 join table3 " +
                        "where id=:id AND name=:name " +
                        "order by id ASC, name DESC " +
                        "limit 1 offset 2"),
                anyArgs(), Matchers.eq(defaultRowMapper));
    }

    /**
     * Тест на замену плейсхолдеров :select, :filters и т.д., когда не сказали чем их заменять
     */
    @Test
    public void replacePlaceholdersDefault() {
        N2oSqlDataProvider provider = new N2oSqlDataProvider();
        provider.setQuery("select :count, :select " +
                "from table :join " +
                "where :filters " +
                "order by :sorting " +
                "limit :limit offset :offset");
        Map<String, Object> args = new HashMap<>();

        engine.invoke(provider, args);
        Mockito.verify(jdbc).query(Matchers.eq("select -1, * " +
                        "from table  " +
                        "where 1=1 " +
                        "order by 1 " +
                        "limit 10 offset 0"),
                anyArgs(), Matchers.eq(defaultRowMapper));
    }

    /**
     * Тест на замену переменных вида :var на null, если не указали их значение в args.
     * Если этого не сделать, бросится исключение уровня jdbcTemplate.
     */
    @Test
    public void replaceMissingArgs() {
        N2oSqlDataProvider provider = new N2oSqlDataProvider();
        provider.setQuery("select * where :filters");
        Map<String, Object> args = new HashMap<>();
        args.put("filters", Arrays.asList("id=:id", "name=:name"));
        args.put("name", "test");
        engine.invoke(provider, args);
        Mockito.verify(jdbc).query(Matchers.eq("select * where id=null AND name=:name"), anyArgs(), Matchers.eq(defaultRowMapper));
    }

    /**
     * Тест на вызов query или update команд jdbcTemplate, в зависимости от типа запроса
     */
    @Test
    public void invokeQueryOrUpdate() {
        N2oSqlDataProvider provider = new N2oSqlDataProvider();
        provider.setQuery(" Select * from table");
        Map<String, Object> args = new HashMap<>();
        engine.invoke(provider, args);
        Mockito.verify(jdbc).query(Matchers.eq(" Select * from table"), anyArgs(), Matchers.eq(defaultRowMapper));

        provider = new N2oSqlDataProvider();
        provider.setQuery(" Insert select");
        args = new HashMap<>();
        engine.invoke(provider, args);
        Mockito.verify(jdbc).update(Matchers.eq(" Insert select"),
                Matchers.any(SqlParameterSource.class), Matchers.any(KeyHolder.class));
    }

    /**
     * Тест на проверку использования указанного RowMapper
     */
    @Test
    public void changeRowMapper() {
        SingleColumnRowMapper<String> singleColumnRowMapper = new SingleColumnRowMapper<>();
        MapRowMapper mapRowMapper = new MapRowMapper();
        engine.setRowMapperFactory((f) -> f.equals("test") ? singleColumnRowMapper : mapRowMapper);

        N2oSqlDataProvider provider = new N2oSqlDataProvider();
        provider.setRowMapper("test");
        provider.setQuery("select 1");
        Map<String, Object> args = new HashMap<>();
        engine.invoke(provider, args);
        Mockito.verify(jdbc).query(Matchers.eq("select 1"), anyArgs(), Matchers.eq(singleColumnRowMapper));

        provider = new N2oSqlDataProvider();
        provider.setRowMapper("map");
        provider.setQuery("select 1");
        args = new HashMap<>();
        engine.invoke(provider, args);
        Mockito.verify(jdbc).query(Matchers.eq("select 1"), anyArgs(), Matchers.eq(mapRowMapper));
    }

    @Test
    public void loadFromFile() {
        N2oSqlDataProvider provider = new N2oSqlDataProvider();
        provider.setFilePath("net/n2oapp/framework/engine/sql/sqldataprovider.sql");
        Map<String, Object> args = new HashMap<>();
        engine.invoke(provider, args);
        Mockito.verify(jdbc).query(Matchers.eq("select 123"), anyArgs(), Matchers.eq(defaultRowMapper));
    }

    private static Map<String, Object> anyArgs() {
        return Matchers.anyMapOf(String.class, Object.class);
    }
}