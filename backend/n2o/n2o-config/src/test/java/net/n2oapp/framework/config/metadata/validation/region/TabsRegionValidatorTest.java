package net.n2oapp.framework.config.metadata.validation.region;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.StandardDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.regions.ScrollspyValidator;
import net.n2oapp.framework.config.metadata.validation.standard.regions.TabsValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TabsRegionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.validators(new StandardPageValidator(), new ScrollspyValidator(),
                new TabsValidator(), new WidgetValidator(), new StandardDatasourceValidator());
    }

    @Test
    void testTabsEmpty() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/testEmptyTabs.page.xml"));
        assertEquals("В регионе <tabs> отсутствуют вкладки <tab>", exception.getMessage());
    }

    @Test
    void testTabsDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/testTabsDatasourceExistence.page.xml"));
        assertEquals("Регион <tabs> ссылается на несуществующий источник данных 'test'", exception.getMessage());
    }

    @Test
    void testContentInTab() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/testContentInTab.page.xml"));
        assertEquals("Источник данных  ссылается на несуществующую выборку 'testQ'", exception.getMessage());
    }

    @Test
    void testTabDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/testTabDatasourceExistence.page.xml")
        );
        assertEquals("Вкладка 'tab1' ссылается на несуществующий источник данных 'test'", exception.getMessage());
    }
}
