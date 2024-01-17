package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.v2.OpenPageElementIOV2;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import net.n2oapp.framework.config.metadata.validation.standard.application.ApplicationValidator;
import net.n2oapp.framework.config.metadata.validation.standard.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование валидатора простого меню
 */
public class SimpleMenuValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIOv3(), new StandardDatasourceIO(),  new NavMenuIOv3(), new AnchorElementIOV2(), new OpenPageElementIOV2());
        builder.validators(new ApplicationValidator(), new SimpleMenuValidator());
    }

    @Test
    void testMenuItemNameWithOpenPage() {
        assertDoesNotThrow(() -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testMenuItemOpenPage.application.xml"));
    }

    @Test
    void testMenuItemWithoutName() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testMenuItemWithoutName.menu.xml"));
        assertEquals("Не задан 'name' для <menu-item>", exception.getMessage());
    }

    @Test
    void testDropdownMenuWithoutName() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testDropdownMenuWithoutName.menu.xml"));
        assertEquals("Не задан 'name' для <dropdown-menu>", exception.getMessage());
    }

    @Test
    void testColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testMenuItemBadgeColor.menu.xml"));
        assertEquals("<menu-item name='item'> использует недопустимое значение атрибута badge-color=\"red\"", exception.getMessage());
    }

    @Test
    void testDatasourceExistence() {
        assertDoesNotThrow(()-> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testDatasourceExistence.application.xml"));
    }

    @Test
    void testNonDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testDatasourceExistenceFail.application.xml"));
        assertEquals("<menu-item name='test'> ссылается на несуществующий источник данных 'test'", exception.getMessage());
    }

    @Test
    void testDatasourceExistenceFailDropdown() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testDatasourceExistenceFailDropdown.application.xml"));
        assertEquals("<dropdown-menu name='test'> ссылается на несуществующий источник данных 'test'", exception.getMessage());
    }
}
