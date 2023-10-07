package net.n2oapp.framework.config.metadata.validation.button;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ButtonValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oAllDataPack(), new N2oControlsPack());
        builder.packs(new N2oAllValidatorsPack());
    }

    @Test
    void testCheckDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testButtonDatasourceExistent.page.xml"));
        assertEquals("Кнопка  ссылается на несуществующий источник данных 'ds2'", exception.getMessage());
    }

    @Test
    void testCheckValidateDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testButtonValidateDatasourceExistent.page.xml"));
        assertEquals("Атрибут 'validate-datasources' кнопки  содержит несуществующий источник данных 'ds2'", exception.getMessage());
    }

    @Test
    void testConfirmNonDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testConfirmNonDatasource.page.xml"));
        assertEquals("Кнопка  имеет ссылки в 'confirm' атрибутах, но не ссылается на какой-либо источник данных", exception.getMessage());
    }

    @Test
    void testConfirmDatasource() {
        validate("net/n2oapp/framework/config/metadata/validation/button/testConfirmDatasource.page.xml");
    }

    @Test
    void testColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testButtonColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута color=\"red\"", exception.getMessage());
    }

    @Test
    void testConfirmOkColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testButtonConfirmOkColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута confirm-ok-color=\"red\"", exception.getMessage());
    }

    @Test
    void tesConfirmCancelColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testButtonConfirmCancelColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута confirm-cancel-color=\"red\"", exception.getMessage());
    }

    @Test
    void testFieldColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/field/testButtonFieldColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута color=\"red\"", exception.getMessage());
    }

    @Test
    void testFieldConfirmOkColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/field/testButtonFieldConfirmOkColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута confirm-ok-color=\"red\"", exception.getMessage());
    }

    @Test
    void testFieldConfirmCancelColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/field/testButtonFieldConfirmCancelColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута confirm-cancel-color=\"red\"", exception.getMessage());
    }

    @Test
    void testBadgeColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testButtonBadgeColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута badge-color=\"red\"", exception.getMessage());
    }

    @Test
    void testFieldBadgeColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/field/testButtonFieldBadgeColor.page.xml"));
        assertEquals("Кнопка использует недопустимое значение атрибута badge-color=\"red\"", exception.getMessage());
    }
}