package net.n2oapp.framework.boot.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Проверка повтора {@link JDBCRouteRepository#save} при взаимных блокировках (deadlock),
 * возникающих во время конкурентной регистрации маршрутов несколькими экземплярами приложения.
 */
class JDBCRouteRepositoryRetryTest {

    private JdbcTemplate jdbcTemplate;
    private JDBCRouteRepository repository;

    private final RouteInfoKey key = new RouteInfoKey("/test", TestCompiled.class);
    private final CompileContext<?, ?> value = mock(CompileContext.class, withSettings().serializable());

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new JDBCRouteRepository();
        ReflectionTestUtils.setField(repository, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(repository, "tableName", "route_repository");
        ReflectionTestUtils.setField(repository, "retryCount", 5);
        ReflectionTestUtils.setField(repository, "retryDelayMs", 0L);   // без задержки, чтобы тест был быстрым
    }

    @Test
    void shouldRetryOnLockConflictAndSucceed() {
        // UPDATE дважды падает с ошибкой блокировки, на третьей попытке успешно обновляет строку
        when(jdbcTemplate.update(anyString(), any(), any(), any()))
                .thenThrow(new PessimisticLockingFailureException("deadlock detected"))
                .thenThrow(new PessimisticLockingFailureException("deadlock detected"))
                .thenReturn(1);

        assertSame(value, repository.save(key, value));

        verify(jdbcTemplate, times(3)).update(anyString(), any(), any(), any());
    }

    @Test
    void shouldRethrowAfterExhaustingRetries() {
        ReflectionTestUtils.setField(repository, "retryCount", 3);
        when(jdbcTemplate.update(anyString(), any(), any(), any()))
                .thenThrow(new PessimisticLockingFailureException("deadlock detected"));

        assertThrows(PessimisticLockingFailureException.class, () -> repository.save(key, value));

        verify(jdbcTemplate, times(3)).update(anyString(), any(), any(), any());
    }

    @Test
    void shouldNotRetryOnNonTransientError() {
        // Не транзиентная ошибка (например, нарушение целостности) не должна повторяться
        when(jdbcTemplate.update(anyString(), any(), any(), any()))
                .thenThrow(new DataIntegrityViolationException("constraint violation"));

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(key, value));

        verify(jdbcTemplate, times(1)).update(anyString(), any(), any(), any());
    }

    private static class TestCompiled implements Compiled {
    }
}