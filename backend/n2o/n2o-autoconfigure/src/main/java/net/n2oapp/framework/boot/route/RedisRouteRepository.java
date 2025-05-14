package net.n2oapp.framework.boot.route;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.config.register.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сохранение и загрузка данных RouteRegister в Redis (key-value) БД
 */
@Slf4j
public class RedisRouteRepository implements ConfigRepository<RouteInfoKey, CompileContext> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${n2o.config.register.redis.key:routes}")
    private String hashKey;

    private HashOperations<String, String, CompileContext> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public CompileContext save(RouteInfoKey key, CompileContext value) {
        CompileContext contextInHash = hashOperations.get(hashKey, key.getUrlMatching());
        hashOperations.put(hashKey, key.getUrlMatching(), value);

        String action = contextInHash == null ? "Inserted" : "Updated";
        log.info(String.format("%s route: '%s' to [%s]", action, value, key.getUrlMatching()));

        return value;
    }

    @Override
    public Map<RouteInfoKey, CompileContext> getAll() {
        Map<RouteInfoKey, CompileContext> result = hashOperations.entries(hashKey).entrySet().stream()
                .collect(Collectors.toMap(
                        row -> new RouteInfoKey(row.getKey(), row.getValue().getCompiledClass()),
                        Map.Entry::getValue, (a, b) -> b)
                );
        log.info(String.format("Returned %s routes.", result.size()));
        return result;
    }

    @Override
    public void clearAll() {
        redisTemplate.delete(hashKey);
    }
}
