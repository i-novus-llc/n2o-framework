package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации действия switch
 */
public class SwitchActionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack(), new N2oAllValidatorsPack());
    }

    /**
     * Проверка наличия указанного value-field-id
     */
    @Test
    void testValueFieldIdExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testValueFieldIdExistence.page.xml"),
                "В действии <switch> не указан 'value-field-id'"
        );
    }

    /**
     * Проверка существования источника данных в скоупе по указанному идентификатору в атрибуте datasource
     */
    @Test
    void testDatasourceExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testDatasourceExistence.page.xml"),
                "Тег <switch> в атрибуте 'datasource' ссылается на несуществующий источник данных ds1"
        );
    }

    /**
     * Проверка того, что после <default> не следуют <case>
     */
    @Test
    void testCasesSequence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testCasesSequence.page.xml"),
                "В действии <switch> после <default> указан <case>"
        );
    }

    /**
     * Проверка указанного атрибута value у тегов <case>
     */
    @Test
    void testCaseValueExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testCaseValueExistence.page.xml"),
                "В <case> действия <switch> не указан атрибут 'value'"
        );
    }
}
