package net.n2oapp.framework.autotest.cases;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Тестирование отображение ошибок
 */
class ErrorDisplayAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
        builder.transformers(new ErrorDisplayPageTransformer());
    }

    @ParameterizedTest
    @CsvSource({
            "route_not_found, 404",
            "internal_server_error, 500",
            "bad_gateway, 502",
            "forbidden, 403"
    })
    void testErrorDisplay(String errorName, int statusCode) {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/error/" + errorName + "/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        page.shouldHaveError(statusCode);
    }
}
