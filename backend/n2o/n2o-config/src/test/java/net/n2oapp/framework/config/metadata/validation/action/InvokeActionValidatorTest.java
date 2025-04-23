package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.action.InvokeActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации действия вызова операции
 */
class InvokeActionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.validators(new BasePageValidator(), new InvokeActionValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/action/page/blankWidget.widget.xml"));
    }

    @Test
    void testRefreshExistentWidget() {
        validate("/net/n2oapp/framework/config/metadata/validation/action/invoke/testInvokeActionValidationRefreshExistentWidget.page.xml");
    }

    @Test
    void testRefreshNonexistentWidget() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/invoke/testInvokeActionValidationRefreshNonexistentWidget.page.xml"));
        assertEquals("Атрибут \"refresh-datasources\" действия 'referesh' ссылается на несуществующий источник данных 'nonexistentWidget'", exception.getMessage());
    }

    @Test
    void testRefreshNonExistentDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/invoke/testRefreshNonExistentDatasource.page.xml"));
        assertEquals("Атрибут \"refresh-datasources\" действия 'refresh' ссылается на несуществующий источник данных 'ds1'", exception.getMessage());
    }

    @Test
    void testEmptyOperationId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/invoke/testEmptyOperationId.page.xml"));
        assertEquals("В действии <invoke> не указан идентификатор операции 'operation-id'", exception.getMessage());
    }

    @Test
    void testObjectIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/invoke/testObjectIdExistence.page.xml"));
        assertEquals("Действие <invoke operation-id='test'> ссылается на несуществующий объект 'test'", exception.getMessage());
    }
}
