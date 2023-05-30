package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import net.n2oapp.framework.config.metadata.validation.standard.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        builder.ios(new NavMenuIOv3(), new AnchorElementIOV2());
        builder.validators(new SimpleMenuValidator());
    }

    @Test
    void testMenuItemWithoutAction() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testMenuItemWithoutAction.menu.xml"),
                "Не задано действие для 'menu-item'"
        );
    }

    @Test
    void testMenuItemWithoutName() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testMenuItemWithoutName.menu.xml"),
                "Не задан 'name' для 'menu-item'"
        );
    }

    @Test
    void testDropdownMenuWithoutName() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/application/menu/testDropdownMenuWithoutName.menu.xml"),
                "Не задан 'name' для 'dropdown-menu'"
        );
    }
}
