package net.n2oapp.framework.config.metadata.validation.widget;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.DatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FormValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(),
                new DatasourceValidator(), new FormValidator(), new FieldValidator());
    }

    /**
     * Проверяется наличие источника данных для формы с полями c white-list валидацией
     */
    @Test
    public void testWhiteListValidationWithoutDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции формы main необходимо указать атрибут datasource или ввести внутренний источник данных");
        validate("net/n2oapp/framework/config/metadata/validation/widget/form/testWhiteListValidationWithoutDatasource.page.xml");
    }

    /**
     * Проверяется наличие объекта источника данных для формы с полями c white-list валидацией
     */
    @Test
    public void testWhiteListValidationWithDatasourceWithoutObject() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции формы main необходимо указать объект источника данных ds1");
        validate("net/n2oapp/framework/config/metadata/validation/widget/form/testWhiteListValidationWithDatasourceWithoutObject.page.xml");
    }

    /**
     * Проверяется наличие объекта внутреннего источника данных для формы с полями c white-list валидацией
     */
    @Test
    public void testWhiteListValidationWithInlineDatasourceWithoutObject() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции формы main необходимо указать объект источника данных ");
        validate("net/n2oapp/framework/config/metadata/validation/widget/form/testWhiteListValidationWithInlineDatasourceWithoutObject.page.xml");
    }
}
