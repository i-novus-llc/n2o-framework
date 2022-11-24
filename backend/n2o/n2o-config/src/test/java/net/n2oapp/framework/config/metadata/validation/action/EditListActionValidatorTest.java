package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации действия <edit-list>
 */
public class EditListActionValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.packs(new N2oAllValidatorsPack());
    }

    /**
     * Проверка наличия указанного атрибута operation
     */
    @Test
    public void testOperationProvided() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для действия <edit-list> не указан тип операции");
        validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testOperationProvided.page.xml");
    }

    /**
     * Проверка того, что атрибут datasource ссылается на существующий в скоупе источник данных
     */
    @Test
    public void testDatasourceExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Действие <edit-list> ссылается на несуществующий источник данных 'ds1' в атрибуте 'datasource'");
        validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testDatasourceExistence.page.xml");
    }

    /**
     * Проверка того, что атрибут item-datasource ссылается на существующий в скоупе источник данных
     */
    @Test
    public void testItemDatasourceExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Действие <edit-list> ссылается на несуществующий источник данных 'ds1' в атрибуте 'item-datasource'");
        validate("net/n2oapp/framework/config/metadata/validation/action/edit_list/testItemDatasourceExistence.page.xml");
    }

}
