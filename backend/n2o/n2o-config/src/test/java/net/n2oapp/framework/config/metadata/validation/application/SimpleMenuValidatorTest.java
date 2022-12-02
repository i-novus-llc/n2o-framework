package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import net.n2oapp.framework.config.metadata.validation.standard.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидатора простого меню
 */
public class SimpleMenuValidatorTest extends SourceValidationTestBase {

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
        builder.ios(new NavMenuIOv3(), new AnchorElementIOV2());
        builder.validators(new SimpleMenuValidator());
    }

    @Test
    public void testMenuItemWithoutAction() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Не задано действие для 'menu-item'");
        validate("net/n2oapp/framework/config/metadata/validation/application/menu/testMenuItemWithoutAction.menu.xml");
    }

    @Test
    public void testMenuItemWithoutName() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Не задан 'name' для 'menu-item'");
        validate("net/n2oapp/framework/config/metadata/validation/application/menu/testMenuItemWithoutName.menu.xml");
    }

    @Test
    public void testDropdownMenuWithoutName() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Не задан 'name' для 'dropdown-menu'");
        validate("net/n2oapp/framework/config/metadata/validation/application/menu/testDropdownMenuWithoutName.menu.xml");
    }
}
