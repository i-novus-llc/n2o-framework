package net.n2oapp.framework.config.metadata.validation.event;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.validation.standard.event.OnChangeEventValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации события изменения модели данных
 */
public class OnChangeEventValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack());
        builder.validators(new PageValidator(), new BasePageValidator(), new OnChangeEventValidator());
    }

    /**
     * Проверка того, что атрибут datasource указан
     */
    @Test
    void testDatasourceIdExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/event/testDatasourceIdExistence.page.xml"),
                "В событии <on-change> не задан атрибут 'datasource'"
        );
    }
}
