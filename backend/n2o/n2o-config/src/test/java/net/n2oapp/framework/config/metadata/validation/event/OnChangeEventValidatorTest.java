package net.n2oapp.framework.config.metadata.validation.event;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.validation.standard.event.OnChangeEventValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации события изменения модели данных
 */
class OnChangeEventValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oActionsPack());
        builder.validators(new PageValidator(), new BasePageValidator(), new OnChangeEventValidator());
    }

    /**
     * Проверка того, что атрибут datasource указан
     */
    @Test
    void testDatasourceIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/event/testDatasourceIdExistence.page.xml"));
        assertEquals("В событии <on-change> не задан атрибут 'datasource'", exception.getMessage());
    }

    /**
     * Проверка того, что указанный datasource существует
     */
    @Test
    void testDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/event/testDatasourceExistence.page.xml"));
        assertEquals("Событие <on-change> ссылается на несуществующий источник данных 'test'", exception.getMessage());
    }

    @Test
    void actionsAreNotExist() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/event/actionsAreNotExist.page.xml"));
        assertEquals("В событии <on-change> 'withOutActions' не заданы действия", exception.getMessage());
    }

    @Test
    void checkOnFailAction() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/event/checkOnFailAction.page.xml"));
        assertEquals("Задано действие <on-fail> при отсутствующем действии <invoke>", exception.getMessage());
    }
}
