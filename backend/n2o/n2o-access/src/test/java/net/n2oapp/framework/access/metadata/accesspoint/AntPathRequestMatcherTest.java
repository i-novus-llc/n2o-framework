package net.n2oapp.framework.access.metadata.accesspoint;

import net.n2oapp.framework.access.metadata.accesspoint.util.AntPathRequestMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование сопоставления URL с шаблоном AntPath
 */
class AntPathRequestMatcherTest {

    @ParameterizedTest
    @CsvSource({
            // шаблон на все URL
            "/**, /, true",
            "/**, /api/1, true",
            "**, /api/1, true",
            "**, api, true",
            // ведущий слэш в шаблоне и URL необязателен
            "testUrl, testUrl, true",
            "testUrl, /testUrl, true",
            "/testUrl, testUrl, true",
            "api/**, /api/1, true",
            // точное совпадение
            "/otherTest, /otherTest, true",
            "/otherTest, /otherTest/1, false",
            // суффикс /**
            "/api/**, /api, true",
            "/api/**, /api/, true",
            "/api/**, /api/1, true",
            "/api/**, /api/1/2, true",
            "/api/**, /apix, false",
            "/api/**, /API/1, false",
            // переменная пути перед /**
            "/api/{id}/**, /api/{id}/z, true",
            "/api/{id}/**, /api/7, true",
            "/api/{id}/**, /api/7/z, true",
            "/api/{id}/**, /api/:id/z, true",
            "/api/{id}/**, /api, false",
            "/api/{id}/**, /other/7/z, false",
            // одиночные символы подстановки
            "/test/*, /test/a, true",
            "/test/*, /test/a/b, false",
            "/path/create/*, /path/create/, true",
            "/path/create*, /path/create1, true",
            "/path/create*, /path/create/1, false",
            "/api/**/x, /api/a/b/x, true",
            "/api/?, /api/1, true",
            "/api/?, /api/12, false"
    })
    void testMatches(String pattern, String url, boolean expected) {
        assertThat(new AntPathRequestMatcher(pattern).matches(url), is(expected));
    }

    @Test
    void testEmptyPattern() {
        assertThrows(IllegalArgumentException.class, () -> new AntPathRequestMatcher(""));
        assertThrows(IllegalArgumentException.class, () -> new AntPathRequestMatcher(null));
    }
}
