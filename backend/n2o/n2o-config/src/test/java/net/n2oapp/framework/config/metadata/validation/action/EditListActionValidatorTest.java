package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации действия <edit-list>
 */
public class EditListActionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
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
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testOperationProvided.page.xml"),
                "Для действия <edit-list> не указан тип операции"
        );
    }

    /**
     * Проверка того, что атрибут datasource ссылается на существующий в скоупе источник данных
     */
    @Test
    void testDatasourceExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testDatasourceExistence.page.xml"),
                "Действие <edit-list> ссылается на несуществующий источник данных 'ds1' в атрибуте 'datasource'"
        );
    }

    /**
     * Проверка того, что атрибут item-datasource ссылается на существующий в скоупе источник данных
     */
    @Test
    void testItemDatasourceExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testItemDatasourceExistence.page.xml"),
                "Действие <edit-list> ссылается на несуществующий источник данных 'ds1' в атрибуте 'item-datasource'"
        );
    }

}
