package net.n2oapp.framework.config.metadata.validation.widget;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TableValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.columns.FilterColumnValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack(),
                new N2oAllDataPack()
        );
        builder.validators(
                new TableValidator(),
                new FilterColumnValidator()
        );
    }

    @Test
    void testDuplicateColumnIds() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testDuplicateColumnIds.widget.xml")
        );
        assertEquals("Таблица 'testDuplicateColumnIds' содержит повторяющиеся значения id=\"id1\" в <column>", exception.getMessage());
    }

    @Test
    void testDuplicateColumnTextFieldId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testDuplicateColumnTextFieldId.widget.xml")
        );
        assertEquals("Таблица 'testDuplicateColumnTextFieldId' содержит повторяющиеся значения text-field-id=\"c1\" в <column>", exception.getMessage());
    }

    @Test
    void testOverlayToolbar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testOverlayToolbar.widget.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре в <overlay> таблицы 'testOverlayToolbar'", exception.getMessage());
    }

    @Test
    void testFilterColumnFilterExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testFilterColumnFilterExistence.page.xml")
        );
        assertEquals("В <filter-column text-field-id='test'> таблицы не задан <filter>", exception.getMessage());
    }
}