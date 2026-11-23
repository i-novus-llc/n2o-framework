package net.n2oapp.framework.config.metadata.validation.button;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидатора группы кнопок тулбара
 */
class GroupValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack(),
                new N2oAllDataPack(),
                new N2oControlsPack(),
                new N2oActionsPack());
        builder.packs(new N2oAllValidatorsPack());
    }

    @Test
    void testEmptyGroup() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/group/emptyGroup.page.xml"));
        assertEquals("В группе кнопок не заданы элементы или атрибут 'generate'", exception.getMessage());
    }

    @Test
    void testEmptyGenerate() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/group/emptyGenerate.page.xml"));
        assertEquals("Атрибут 'generate' группы кнопок не может содержать пустую строку", exception.getMessage());
    }

    @Test
    void testInvokeInsideNestedButton() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/group/invokeInsideNestedButton.page.xml"));
        assertEquals("В действии \"<invoke>\" не указан идентификатор операции 'operation-id'", exception.getMessage());
    }

    @Test
    void testValidGroup() {
        validate("net/n2oapp/framework/config/metadata/validation/button/group/validGroup.page.xml");
    }
}