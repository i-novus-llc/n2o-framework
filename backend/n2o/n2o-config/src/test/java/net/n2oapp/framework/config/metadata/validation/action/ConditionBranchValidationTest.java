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
 * Тестирование валидации оператора if-else
 */
public class ConditionBranchValidationTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack(), new N2oAllValidatorsPack());
    }

    /**
     * Проверка, что оператор начинается с if
     */
    @Test
    public void testStartsWithIf() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Условный оператор if-else начинается не с тега <if>");
        validate("net/n2oapp/framework/config/metadata/validation/action/condition/testStartsWithIf.page.xml");
    }

    /**
     * Проверка существования источника данных в скоупе по указанному идентификатору в атрибуте datasource
     */
    @Test
    public void testDatasourceExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Тег <if> в атрибуте 'datasource' ссылается на несуществующий источник данных ds1");
        validate("net/n2oapp/framework/config/metadata/validation/action/condition/testDatasourceExistence.page.xml");
    }

    /**
     * Проверка того, что else-if не следует после else в одном операторе if-else
     */
    @Test
    public void testWrongIfElseAndElseSequence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Неверный порядок тегов <else-if> и <else> в условном операторе if-else");
        validate("net/n2oapp/framework/config/metadata/validation/action/condition/testWrongIfElseAndElseSequence.page.xml");
    }

    /**
     * Проверка наличия атрибута test у элемента if
     */
    @Test
    public void testIfTestRequirement() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В теге <if> условного операторе if-else не задано условие 'test'");
        validate("net/n2oapp/framework/config/metadata/validation/action/condition/testIfTestRequirement.page.xml");
    }

    /**
     * Проверка наличия атрибута test у элемента else-if
     */
    @Test
    public void testElseIfTestRequirement() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В теге <else-if> условного операторе if-else не задано условие 'test'");
        validate("net/n2oapp/framework/config/metadata/validation/action/condition/testElseIfTestRequirement.page.xml");
    }

    /**
     * Проверка нескольких операторов if-else
     */
    @Test
    public void testAllRight() {
        validate("net/n2oapp/framework/config/metadata/validation/action/condition/testAllRight.page.xml");
    }
}
