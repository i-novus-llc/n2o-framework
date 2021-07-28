package net.n2oapp.framework.boot;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.boot.route.jdbc.JDBCRouteRepository;
import net.n2oapp.framework.boot.route.jdbc.RedisRouteRepository;
import net.n2oapp.framework.config.register.ConfigRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@AutoConfigureBefore(N2oEnvironmentConfiguration.class)
public class N2oRouteRepositoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(JdbcTemplate.class)
    @ConditionalOnProperty(name = "n2o.config.register.store-type", havingValue = "jdbc")
    public ConfigRepository<RouteInfoKey, CompileContext> jdbcRouteRepository() {
        return new JDBCRouteRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(name = "n2o.config.register.store-type", havingValue = "hash")
    public ConfigRepository<RouteInfoKey, CompileContext> redisRouteRepository() {
        return new RedisRouteRepository();
    }
}
