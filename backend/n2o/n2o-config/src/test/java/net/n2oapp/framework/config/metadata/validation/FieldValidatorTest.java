package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
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

/**
 * Тестирование валидации страницы
 */
public class FieldValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(),
                new DatasourceValidator(), new FormValidator(), new FieldValidator());
    }

    @Test
    public void testUniqueDependenciesType() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В поле field2 повторяются зависимости одного типа");
        validate("net/n2oapp/framework/config/metadata/validation/field/testUniqueDependenciesType.widget.xml");
    }

    @Test
    public void testDefaultValueNotNull() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("У поля field1 атрибут default-value не является ссылкой или не задан: null");
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueNotNull.widget.xml");
    }

    @Test
    public void testDefaultValueNotNullForInterval() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("У поля field1 default-value не задан");
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueNotNullForInterval.widget.xml");
    }

    @Test
    public void testDefaultValueIsLinkForList() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("У поля field1 default-value не является ссылкой");
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueIsLinkForList.widget.xml");
    }

    @Test
    public void testDefaultValueIsLinkForInterval() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("У поля field1 default-value не является ссылкой");
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueIsLinkForInterval.widget.xml");
    }

    @Test
    public void testDefaultValue() {
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValue.widget.xml");
    }

    /**
     * Проверяется наличие идентификатора у поля
     */
    @Test
    public void testIdExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции виджета testIdExistence необходимо задать идентификаторы для всех полей");
        validate("net/n2oapp/framework/config/metadata/validation/field/testIdExistence.widget.xml");
    }

    /**
     * Проверяется наличие источника данных виджета для поля c white-list валидацией
     */
    @Test
    public void testWhiteListValidationWithoutDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции поля name необходимо указать атрибут datasource или ввести внутренний источник данных виджета main");
        validate("net/n2oapp/framework/config/metadata/validation/field/testWhiteListValidationWithoutDatasource.page.xml");
    }

    /**
     * Проверяется наличие объекта источника данных виджета для поля c white-list валидацией
     */
    @Test
    public void testWhiteListValidationWithDatasourceWithoutObject() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции поля name виджета main необходимо указать объект источника данных ds1");
        validate("net/n2oapp/framework/config/metadata/validation/field/testWhiteListValidationWithDatasourceWithoutObject.page.xml");
    }

    /**
     * Проверяется наличие объекта внутреннего источника данных виджета для поля c white-list валидацией
     */
    @Test
    public void testWhiteListValidationWithInlineDatasourceWithoutObject() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для компиляции поля name виджета main необходимо указать объект источника данных ");
        validate("net/n2oapp/framework/config/metadata/validation/field/testWhiteListValidationWithInlineDatasourceWithoutObject.page.xml");
    }
}
