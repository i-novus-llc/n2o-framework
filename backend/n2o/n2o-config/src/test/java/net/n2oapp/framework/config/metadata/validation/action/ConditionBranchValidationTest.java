package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации оператора if-else
 */
public class ConditionBranchValidationTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
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
    void testStartsWithIf() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/condition/testStartsWithIf.page.xml"),
                "Условный оператор if-else начинается не с тега <if>"
        );
    }

    /**
     * Проверка существования источника данных в скоупе по указанному идентификатору в атрибуте datasource
     */
    @Test
    void testDatasourceExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/condition/testDatasourceExistence.page.xml"),
                "Тег <if> в атрибуте 'datasource' ссылается на несуществующий источник данных ds1"
        );
    }

    /**
     * Проверка того, что else-if не следует после else в одном операторе if-else
     */
    @Test
    void testWrongIfElseAndElseSequence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/condition/testWrongIfElseAndElseSequence.page.xml"),
                "Неверный порядок тегов <else-if> и <else> в условном операторе if-else"
        );
    }

    /**
     * Проверка наличия атрибута test у элемента if
     */
    @Test
    void testIfTestRequirement() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/condition/testIfTestRequirement.page.xml"),
                "В теге <if> условного операторе if-else не задано условие 'test'"
        );
    }

    /**
     * Проверка наличия атрибута test у элемента else-if
     */
    @Test
    void testElseIfTestRequirement() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/condition/testElseIfTestRequirement.page.xml"),
                "В теге <else-if> условного операторе if-else не задано условие 'test'"
        );
    }

    /**
     * Проверка нескольких операторов if-else
     */
    @Test
    void testAllRight() {
        validate("net/n2oapp/framework/config/metadata/validation/action/condition/testAllRight.page.xml");
    }
}
