package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageActionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack(),
                new N2oAllValidatorsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/action/page/blankObject.object.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/action/page/blankWidget.widget.xml"));
    }

    @Test
    void testPageActionValidationShowModalInToolbar() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationShowModalInToolbar.page.xml")
        );
    }

    @Test
    void testPageActionValidationInToolbar() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInToolbar.page.xml")
        );
    }

    @Test
    void testPageActionValidationInRowClick() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInRowClick.page.xml")
        );
    }

    @Test
    void testPageActionValidationInColumnLink() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInColumnLink.page.xml")
        );
    }

    @Test
    void testPageActionValidationInWidgetsToolbar() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInWidgetsToolbar.page.xml")
        );
    }

    @Test
    void testRefreshDatasourceNonExistent() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationRefreshDatasourcesNonExistent.page.xml")
        );
    }

    @Test
    void testPageActionValidationShowModalInToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationShowModalInToolbar2.page.xml");
    }

    @Test
    void testPageActionValidationInToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInToolbar2.page.xml");
    }

    @Test
    void testPageActionValidationInRowClick2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInRowClick2.page.xml");
    }

    @Test
    void testPageActionValidationInColumnLink2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInColumnLink2.page.xml");
    }

    @Test
    void testPageActionValidationInWidgetsToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInWidgetsToolbar2.page.xml");
    }

    @Test
    void testPageActionValidationPageExists() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationPageExists.page.xml")
        );
    }

    @Test
    void testPageActionValidationOperationExists() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationOperationExists.page.xml")
        );
    }

    @Test
    void testPageActionValidationOperationExists2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationOperationExists2.page.xml");
    }

    @Test
    void testPageActionValidationRefreshNonexistentWidget() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationRefreshNonexistentWidget.page.xml")
        );
    }

    @Test
    void testPageActionValidationRefreshExistentWidget() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationRefreshExistentWidget.page.xml");
    }

    @Test
    void testParamNameExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testParamNameExistence.page.xml")
        );
        assertEquals("В параметре действия открытия страницы 'utBlank' не задан атрибут 'name'", exception.getMessage());
    }

    @Test
    void testParamDatasourceIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testParamDatasourceIdExistence.page.xml")
        );
        assertEquals("Параметр 'test' открытия страницы ссылается на несуществующий источник данных 'ds1'", exception.getMessage());
    }

    @Test
    void testEmptyToolbar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/page/testEmptyToolbar.page.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре действия открытия страницы 'utBlank'", exception.getMessage());
    }
}
