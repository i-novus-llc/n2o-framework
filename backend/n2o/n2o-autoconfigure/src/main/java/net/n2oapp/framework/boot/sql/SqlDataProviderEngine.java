package net.n2oapp.framework.boot.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.engine.factory.EngineFactory;
import net.n2oapp.engine.factory.TypicalEngine;
import net.n2oapp.engine.factory.integration.spring.SpringEngineFactory;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.data.exception.N2oQueryExecutionException;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.engine.data.QueryUtil;
import net.n2oapp.framework.engine.util.NamedParameterUtils;
import net.n2oapp.framework.engine.util.QueryBlank;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.engine.data.QueryUtil.*;

/**
 * Выполнение sql действия. На вход приходит map аргументов, на выход отправляется
 * List<Object[]> если mapByIndex = true, иначе List<Map<String, Object>>.
 */
@Slf4j
public class SqlDataProviderEngine implements MapInvocationEngine<N2oSqlDataProvider>,
        ApplicationContextAware, ResourceLoaderAware {

    private static final Pattern SQL_ERROR_PATTERN = Pattern.compile("(\\A|\n)[A-Z][a-z](.|\n)+?; SQL statement:");
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private String defaultJdbcDriver;

    private EngineFactory<String, RowMapper> rowMapperFactory;
    private ResourceLoader resourceLoader;

    @Override
    public Object invoke(N2oSqlDataProvider invocation, Map<String, Object> data) {
        Map<String, Object> args = new HashMap<>(data);
        String query = loadQuery(invocation);
        query = replaceListPlaceholder(query, ":select", args.remove("select"), "*", QueryUtil::reduceComma);
        query = replaceListPlaceholder(query, ":join", args.remove("join"), "", QueryUtil::reduceSpace);
        query = replaceListPlaceholder(query, ":filters", args.remove("filters"), "1=1", QueryUtil::reduceAnd);
        query = replaceListPlaceholder(query, ":sorting", args.remove("sorting"), "1",
                s -> replaceSortDirection(s, data), QueryUtil::reduceComma);
        query = replacePlaceholder(query, ":limit", args.remove("limit"), "10");
        query = replacePlaceholder(query, ":offset", args.remove("offset"), "0");
        query = replacePlaceholder(query, ":count", args.remove("count"), "-1");
        log.debug("Execute SQL query: " + query);

        try {
            if (invocation.getConnectionUrl() == null)
                return executeQuery(args,
                        query,
                        rowMapperFactory.produce(castDefault(invocation.getRowMapper(), "map")),
                        namedParameterJdbcTemplate);
            NamedParameterJdbcTemplate jdbcTemplate = createJdbcTemplate(invocation);
            return executeQuery(args, query,
                    rowMapperFactory.produce(castDefault(invocation.getRowMapper(), "map")), jdbcTemplate);
        } catch (DataAccessException e) {
            log.error("Execution error with SQL query: " + query);
            throw new N2oQueryExecutionException(constructSqlMessage(e), query, e);
        }
    }

    private String replaceSortDirection(String sortCause, Map<String, Object> data) {
        return replacePlaceholders(sortCause, t -> t.startsWith(":"), p -> data.get(p.substring(1)));
    }

    private String loadQuery(N2oSqlDataProvider invocation) {
        if (invocation.getFilePath() != null) {
            if (resourceLoader == null)
                throw new IllegalStateException("Resource Loader should not be null");
            try (InputStream is = resourceLoader.getResource(invocation.getFilePath()).getInputStream()) {
                return IOUtils.toString(is, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new N2oException(e);
            }
        }
        return invocation.getQuery();
    }

    @Override
    public Class<? extends N2oSqlDataProvider> getType() {
        return N2oSqlDataProvider.class;
    }

    private Object executeQuery(Map<String, Object> args, String query, RowMapper<?> mapper,
                                NamedParameterJdbcTemplate jdbcTemplate) {
        QueryBlank queryBlank = NamedParameterUtils.prepareQuery(query, args);
        query = queryBlank.getQuery();
        args = queryBlank.getArgs();
        if (isSelect(query)) {
            return jdbcTemplate.query(query, args, mapper);
        } else {
            MapSqlParameterSource paramSource = new MapSqlParameterSource(args);
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(query, paramSource, generatedKeyHolder);
            return getResult(generatedKeyHolder);
        }
    }

    private Object getResult(GeneratedKeyHolder generatedKeyHolder) {
        List<Map<String, Object>> keyList = generatedKeyHolder.getKeyList();
        if (keyList != null) {
            if (keyList.size() > 1) {
                List<Object> rows = new ArrayList<>(keyList.size());
                for (Map<String, Object> row : keyList) {
                    rows.add(row.values().toArray());
                }
                return rows.toArray();
            } else if (keyList.size() == 1) {
                return keyList.get(0).values().toArray();
            }
        }
        return null;
    }

    private boolean isSelect(String query) {
        return query.trim().toLowerCase().startsWith("select");
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.rowMapperFactory = new SpringEngineFactory<String, RowMapper>(applicationContext) {
            @Override
            public Class<RowMapper> getEngineClass() {
                return RowMapper.class;
            }

            @Override
            public String getType(RowMapper engine) {
                if (engine instanceof TypicalEngine)
                    return ((TypicalEngine<String>) engine).getType();
                else
                    return engine.getClass().getSimpleName();
            }
        };
    }

    public void setRowMapperFactory(EngineFactory<String, RowMapper> rowMapperFactory) {
        this.rowMapperFactory = rowMapperFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setDefaultJdbcDriver(String defaultJdbcDriver) {
        this.defaultJdbcDriver = defaultJdbcDriver;
    }

    private NamedParameterJdbcTemplate createJdbcTemplate(N2oSqlDataProvider invocation) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(invocation.getJdbcDriver() == null ? defaultJdbcDriver : invocation.getJdbcDriver());
        config.setJdbcUrl(invocation.getConnectionUrl());
        config.setUsername(invocation.getUsername());
        config.setPassword(invocation.getPassword());
        return new NamedParameterJdbcTemplate(new HikariDataSource(config));
    }

    private String constructSqlMessage(DataAccessException e) {
        String sqlMessage = e.getMessage();
        if (e instanceof BadSqlGrammarException badSqlGE)
            sqlMessage = badSqlGE.getSQLException().getMessage();
        Matcher matcher = SQL_ERROR_PATTERN.matcher(sqlMessage);
        if (matcher.find())
            return "Bad SQL grammar: " + (matcher.group().startsWith("\n") ?
                    StringUtils.substringBetween(matcher.group(), "\n", "; SQL statement:")
                    : StringUtils.substringBefore(matcher.group(), "; SQL statement:"));
        return sqlMessage;
    }
}
