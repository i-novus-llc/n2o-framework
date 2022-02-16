package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.SearchablePageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации страницы с поисковой строкой
 */
public class SearchablePageValidatorTest extends SourceValidationTestBase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.validators(new PageValidator(), new BasePageValidator(), new SearchablePageValidator());
    }

    /**
     * Проверяется наличие источника данных в <search-bar>
     */
    @Test
    public void testNonExistentDatasourceInSearchBar() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции страницы с поисковой строкой необходимо указать источник данных в <search-bar>");
        validate("net/n2oapp/framework/config/metadata/validation/page/searchable/testNonExistentDatasourceInSearchBar.page.xml");
    }

    /**
     * Проверяется наличие источника данных, указанного в <search-bar>, на странице
     */
    @Test
    public void testNonExistentDatasourceLinkInSearchBar() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("<search-bar> страницы с поисковой строкой ссылается на несуществующий источник данных ds1");
        validate("net/n2oapp/framework/config/metadata/validation/page/searchable/testNonExistentDatasourceLinkInSearchBar.page.xml");
    }
}
