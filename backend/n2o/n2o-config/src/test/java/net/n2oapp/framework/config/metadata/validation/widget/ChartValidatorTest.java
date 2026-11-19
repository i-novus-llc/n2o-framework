package net.n2oapp.framework.config.metadata.validation.widget;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации виджета-диаграммы
 */
class ChartValidatorTest extends SourceValidationTestBase {

    private static final String PATH = "net/n2oapp/framework/config/metadata/validation/widget/chart/";

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
                new N2oChartsPack(),
                new N2oAllDataPack(),
                new N2oActionsPack(),
                new N2oAllValidatorsPack()
        );
    }

    @Test
    void testAreaWithoutFieldId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate(PATH + "testAreaWithoutFieldId.widget.xml"));
        assertEquals("В элементе \"<area>\" диаграммы виджета 'testAreaWithoutFieldId' не задан обязательный атрибут 'field-id'",
                exception.getMessage());
    }

    @Test
    void testBarWithoutFieldId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate(PATH + "testBarWithoutFieldId.widget.xml"));
        assertEquals("В элементе \"<bar>\" диаграммы виджета 'testBarWithoutFieldId' не задан обязательный атрибут 'field-id'",
                exception.getMessage());
    }

    @Test
    void testLineWithoutFieldId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate(PATH + "testLineWithoutFieldId.widget.xml"));
        assertEquals("В элементе \"<line>\" диаграммы виджета 'testLineWithoutFieldId' не задан обязательный атрибут 'field-id'",
                exception.getMessage());
    }

    @Test
    void testChartValid() {
        assertDoesNotThrow(() -> validate(PATH + "testChartValid.widget.xml"));
    }
}
