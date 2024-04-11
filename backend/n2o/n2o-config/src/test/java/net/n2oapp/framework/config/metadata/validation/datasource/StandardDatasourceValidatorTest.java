package net.n2oapp.framework.config.metadata.validation.datasource;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.StandardDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирования валидации стандартного источника данных
 */
class StandardDatasourceValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new BasePageValidator(), new StandardDatasourceValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/utDsBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/utDs.query.xml"));
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующий объект
     */
    @Test
    void testNonExistentObject() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentObject.page.xml"));
        assertEquals("Источник данных 'ds1' ссылается на несуществующий объект 'object'", exception.getMessage());
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующую выборку
     */
    @Test
    void testNonExistentQuery() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentQuery.page.xml"));
        assertEquals("Источник данных 'ds1' ссылается на несуществующую выборку 'nonExistent'", exception.getMessage());
    }

    /**
     * Проверяется, что атрибут on в зависимости источника задан
     */
    @Test
    void testFetchOnExistenceDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testDependencyOnExistenceDatasource.page.xml"));
        assertEquals("В зависимости источника данных 'ds1' не указан атрибут 'on'", exception.getMessage());
    }

    /**
     * Проверяется, что атрибут on в зависимости источника ссылается на несуществующий источник данных
     */
    @Test
    void testFetchOnNonExistentDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testDependencyOnNonExistentDatasource.page.xml"));
        assertEquals("Атрибут 'on' в зависимости источника данных 'ds1' ссылается на несуществующий источник данных 'ds2'", exception.getMessage());
    }

    /**
     * Проверяется, что тег <submit> on содержит несуществующий источник данных в атрибуте refresh-datasource
     */
    @Test
    void testSubmitWithNonExistentRefreshDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testSubmitWithNonExistentRefreshDatasource.page.xml"));
        assertEquals("Тег <submit> источника данных 'ds1' содержит несуществующий источник данных 'ds2' в атрибуте 'refresh-datasources'", exception.getMessage());
    }

    /**
     * Проверяется, что для префильтра в источнике данных указана выборка
     */
    @Test
    void testRequiredReferenceForPrefiltersQuery() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQuery.page.xml"));
        assertEquals("Источник данных 'ds1' имеет префильтры, но не задана выборка", exception.getMessage());
    }

    /**
     * Проверяется, в выборке есть поля при указании фильтров в источнике данных
     */
    @Test
    void testReferenceQueryFieldExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testReferenceQueryFieldExistence.page.xml"));
        assertEquals("Источник данных 'ds1' имеет префильтры, но в выборке 'utDsBlank' нет filters!", exception.getMessage());
    }

    /**
     * Проверяется, что источник данных, на который ссылается префильтр, существует
     */
    @Test
    void testNonExistentDatasourceInPrefilter() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentDatasourceInPrefilter.page.xml"));
        assertEquals("В префильтре по полю 'id' указан несуществующий источник данных 'ds2'", exception.getMessage());
    }

    /**
     * Проверяется, что для префильтра в выборке есть соответствующее поле
     */
    @Test
    void testRequiredReferenceForPrefiltersQueryField() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQueryField.page.xml"));
        assertEquals("В выборке ''utDs'' нет поля 'notExist'!", exception.getMessage());
    }

    /**
     * Проверяется, что для префильтра в выборке есть фильтр
     */
    @Test
    void testQueryFieldsFilterExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testQueryFieldsFilterExistence.page.xml"));
        assertEquals("В выборке ''utDs'' поле 'id' не содержит фильтров!", exception.getMessage());
    }

    /**
     * Проверяется, что для префильтра в выборке есть фильтр соответствующего типа
     */
    @Test
    void testRequiredReferenceForPrefiltersQueryFieldFilterType() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQueryFieldFilterType.page.xml"));
        assertEquals("В выборке ''utDs'' поле 'name' не содержит фильтр типа 'notEq'!", exception.getMessage());
    }

    /**
     * Проверяется наличие обязательного атрибута field-id для префильтра
     */
    @Test
    void testRequiredPrefilterFieldId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredPrefilterFieldId.page.xml"));
        assertEquals("Источник данных 'ds1' содержит префильтр без указанного field-id!", exception.getMessage());
    }

    /**
     * Проверка случая корректного использования ссылок на фильтры выборки
     */
    @Test
    void testRightUseNestedQueryFields() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/testRightUseNestedQueryFields.query.xml"));
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testRightUseNestedQueryFields.page.xml");
    }

    /**
     * Проверяется наличие обязательного атрибута id для источника данных вне виджета
     */
    @Test
    void testDatasourceIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testDatasourceIdExistence.page.xml"));
        assertEquals("В одном из источников данных страницы 'testDatasourceIdExistence' не задан 'id'", exception.getMessage());
    }

    /**
     * Проверка случая корректного отсутствия id у источника данных
     */
    @Test
    void testDatasourceIdExistenceInWidget() {
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testDatasourceIdExistenceInWidget.page.xml");
    }
}
