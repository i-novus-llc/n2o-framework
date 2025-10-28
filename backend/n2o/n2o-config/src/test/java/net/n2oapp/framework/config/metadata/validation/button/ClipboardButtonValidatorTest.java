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
 * Тестирование валидации кнопки {@code <clipboard-button>}
 */
class ClipboardButtonValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oAllDataPack(), new N2oControlsPack(), new N2oActionsPack());
        builder.packs(new N2oAllValidatorsPack());
    }

    @Test
    void testCheckDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testClipboardButtonDatasourceExistent.page.xml"));
        assertEquals("Кнопка копирования  ссылается на несуществующий источник данных 'ds2'", exception.getMessage());
    }

    @Test
    void testCheckData() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/button/testClipboardButtonData.page.xml"));
        assertEquals("В кнопке копирования 'Копировать' не задан обязательный атрибут 'data'", exception.getMessage());
    }
}