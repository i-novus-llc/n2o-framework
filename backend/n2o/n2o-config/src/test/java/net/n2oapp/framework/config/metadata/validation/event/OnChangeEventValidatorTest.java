package net.n2oapp.framework.config.metadata.validation.event;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.validation.standard.event.OnChangeEventValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации события изменения модели данных
 */
public class OnChangeEventValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack());
        builder.validators(new PageValidator(), new BasePageValidator(), new OnChangeEventValidator());
    }

    /**
     * Проверка того, что атрибут datasource указан
     */
    @Test
    public void testDatasourceIdExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В событии <on-change> не задан атрибут 'datasource'");
        validate("net/n2oapp/framework/config/metadata/validation/event/testDatasourceIdExistence.page.xml");
    }
}
