package net.n2oapp.framework.config.metadata.validation.cells;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SwitchCellValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oCellsV3IOPack());
        builder.packs(new N2oAllValidatorsPack());
    }

    @Test
    void testNoneSwitchValue() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/cells/switch/testSwitchNoneValue.page.xml"));
        assertEquals("Для ячейки <switch> виджета  не указано значение 'value-field-id'", exception.getMessage());
    }

    @Test
    void testNoneCaseValue() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/cells/switch/testSwitchNoneCaseValue.page.xml"));
        assertEquals("Для <case> ячейки <switch> виджета  не указано значение 'value'", exception.getMessage());
    }
}
