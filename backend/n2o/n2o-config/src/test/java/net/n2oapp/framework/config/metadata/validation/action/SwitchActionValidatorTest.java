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
 * Тестирование валидации действия switch
 */
public class SwitchActionValidatorTest extends SourceValidationTestBase {

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
     * Проверка наличия указанного value-field-id
     */
    @Test
    public void testValueFieldIdExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В действии <switch> не указан 'value-field-id'");
        validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testValueFieldIdExistence.page.xml");
    }

    /**
     * Проверка существования источника данных в скоупе по указанному идентификатору в атрибуте datasource
     */
    @Test
    public void testDatasourceExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Тег <switch> в атрибуте 'datasource' ссылается на несуществующий источник данных ds1");
        validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testDatasourceExistence.page.xml");
    }

    /**
     * Проверка того, что после <default> не следуют <case>
     */
    @Test
    public void testCasesSequence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В действии <switch> после <default> указан <case>");
        validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testCasesSequence.page.xml");
    }

    /**
     * Проверка указанного атрибута value у тегов <case>
     */
    @Test
    public void testCaseValueExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В <case> действия <switch> не указан атрибут 'value'");
        validate("net/n2oapp/framework/config/metadata/validation/action/switch_action/testCaseValueExistence.page.xml");
    }
}
