package net.n2oapp.framework.autotest.cases;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void testRouteNotFoundException() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/error/route_not_found/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/error/route_not_found/person.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.shouldHaveError(404);
    }

    @Test
    void testInternalServerError() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/error/internal_server_error/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/error/internal_server_error/person.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.shouldHaveError(500);
    }

    @Test
    void testBadGateway() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/error/bad_gateway/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/error/bad_gateway/person.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.shouldHaveError(502);
    }

    @Test
    void testForbidden() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/error/forbidden/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/error/forbidden/person.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.shouldHaveError(403);
    }
}
