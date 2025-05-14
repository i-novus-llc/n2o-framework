package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации действия <edit-list>
 */
class EditListActionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.packs(new N2oAllValidatorsPack());
    }

    /**
     * Проверка наличия указанного атрибута operation
     */
    @Test
    void testOperationProvided() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testOperationProvided.page.xml"));
        assertEquals("Для действия <edit-list> не указан тип операции", exception.getMessage());
    }

    /**
     * Проверка того, что атрибут datasource ссылается на существующий в скоупе источник данных
     */
    @Test
    void testDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testDatasourceExistence.page.xml"));
        assertEquals("Действие <edit-list> ссылается на несуществующий источник данных 'ds1' в атрибуте 'datasource'", exception.getMessage());
    }

    /**
     * Проверка того, что атрибут item-datasource ссылается на существующий в скоупе источник данных
     */
    @Test
    void testItemDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testItemDatasourceExistence.page.xml"));
        assertEquals("Действие <edit-list> ссылается на несуществующий источник данных 'ds1' в атрибуте 'item-datasource'", exception.getMessage());
    }

}
