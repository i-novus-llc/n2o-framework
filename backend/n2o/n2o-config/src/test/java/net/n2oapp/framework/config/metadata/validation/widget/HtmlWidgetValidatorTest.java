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
 * Тестирование валидации html виджета
 */
class HtmlWidgetValidatorTest extends SourceValidationTestBase {

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
                new N2oActionsPack(),
                new N2oAllValidatorsPack()
        );
    }

    @Test
    void testHtmlWidgetBodyAndFileExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/html/testHtmlWidgetBodyAndFileExistence.widget.xml"));
        assertEquals("Для виджета 'testHtmlWidgetBodyAndFileExistence' в элементе \"<content>\" одновременно задано тело и атрибут 'file'",
                exception.getMessage());
    }

    @Test
    void testHtmlWidgetOnlyBody() {
        assertDoesNotThrow(() -> validate(
                "net/n2oapp/framework/config/metadata/validation/widget/html/testHtmlWidgetOnlyBody.widget.xml"));
    }

    @Test
    void testHtmlWidgetOnlyFile() {
        assertDoesNotThrow(() -> validate(
                "net/n2oapp/framework/config/metadata/validation/widget/html/testHtmlWidgetOnlyFile.widget.xml"));
    }

    /**
     * Проверяем, что наследованная проверка зависимостей виджета работает и для html
     */
    @Test
    void testHtmlWidgetVisibilityBodyAndFileExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/html/testHtmlWidgetVisibilityBodyAndFileExistence.widget.xml"));
        assertEquals("Для виджета 'testHtmlWidgetVisibilityBodyAndFileExistence' в элементе \"<visibility>\" одновременно задано тело и атрибут 'file'",
                exception.getMessage());
    }
}