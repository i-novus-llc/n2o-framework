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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации страницы с поисковой строкой
 */
public class SearchablePageValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
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
    void testNonExistentDatasourceInSearchBar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/searchable/testNonExistentDatasourceInSearchBar.page.xml"));
        assertEquals(exception.getMessage(), "Для компиляции страницы 'testNonExistentDatasourceInSearchBar' с поисковой строкой необходимо указать источник данных в <search-bar>");
    }

    /**
     * Проверяется наличие источника данных, указанного в <search-bar>, на странице
     */
    @Test
    void testNonExistentDatasourceLinkInSearchBar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/searchable/testNonExistentDatasourceLinkInSearchBar.page.xml"));
        assertEquals(exception.getMessage(), "<search-bar> страницы 'testNonExistentDatasourceLinkInSearchBar' с поисковой строкой ссылается на несуществующий источник данных ds1");
    }

    @Test
    void testExistentSearchFilterIdInSearchBar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/searchable/testNonExistentSearchFilterIdSearchBar.page.xml"));
        assertEquals(exception.getMessage(), "Для компиляции страницы 'testNonExistentSearchFilterIdSearchBar' с поисковой строкой необходимо указать идентификатор фильтра 'search-filter-id' в <search-bar>");
    }
}
