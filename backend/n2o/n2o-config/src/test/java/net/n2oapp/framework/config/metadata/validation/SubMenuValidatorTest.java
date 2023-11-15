package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.button.ButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.SubMenuValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubMenuValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(  new N2oPagesPack(), new N2oRegionsPack(),
                new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new BasePageValidator(), new SubMenuValidator(), new ButtonValidator());
    }

    @Test
    void testCheckDatasourceInMenuItem() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/testButtonDatasourceExistent.page.xml"));
        assertEquals("Кнопка  ссылается на несуществующий источник данных 'ds2'", exception.getMessage());
    }

    @Test
    void testCheckValidateDatasourceInMenuItem() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/testButtonValidateDatasourceExistent.page.xml"));
        assertEquals("Атрибут 'validate-datasources' кнопки  содержит несуществующий источник данных 'ds2'", exception.getMessage());
    }

    @Test
    void testConfirmNonDatasourceInMenuItem() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/testConfirmNonDatasource.page.xml"));
        assertEquals("Кнопка  имеет ссылки в 'confirm' атрибутах, но не ссылается на какой-либо источник данных", exception.getMessage());
    }

    @Test
    void testConfirmDatasourceInMenuItem() {
        validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/testConfirmDatasource.page.xml");
    }

    @Test
    void generateHasOneMoreType() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/menuItemGenerateHasOneMoreType.page.xml"));
        assertEquals("Атрибут 'generate' кнопки 'oneMoreGenerate' не может содержать более одного типа генерации", exception.getMessage());
    }

    @Test
    void menuItemGenerateHasEmptyValue() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/menuItemGenerateHasEmptyValue.page.xml"));
        assertEquals("Атрибут 'generate' кнопки 'oneMoreGenerate' не может содержать пустую строку", exception.getMessage());
    }

    @Test
    void generateHasCrudType() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/menuItemGenerateHasCrudType.page.xml"));
        assertEquals("Атрибут 'generate' кнопки 'crudGenerate' не может реализовывать 'crud' генерацию", exception.getMessage());
    }

    @Test
    void generateHasEmptyValue() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/sub_menu/generateHasEmptyValue.page.xml"));
        assertEquals("Атрибут 'generate' выпадающего меню 'empty' не может содержать пустую строку", exception.getMessage());
    }
}
