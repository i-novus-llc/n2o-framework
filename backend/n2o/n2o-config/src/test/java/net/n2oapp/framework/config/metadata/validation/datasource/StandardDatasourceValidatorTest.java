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
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирования валидации стандартного источника данных
 */
public class StandardDatasourceValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(), new StandardDatasourceValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/utDsBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/utDs.query.xml"));
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующий объект
     */
    @Test
    void testNonExistentObject() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentObject.page.xml"),
                "Источник данных ds1 ссылается на несуществующий объект object"
        );
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующую выборку
     */
    @Test
    void testNonExistentQuery() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentQuery.page.xml"),
                "Источник данных ds1 ссылается на несуществующую выборку nonExistent"
        );
    }

    /**
     * Проверяется, что атрибут on в зависимости источника ссылается на несуществующий источник данных
     */
    @Test
    void testFetchOnNonExistentDatasource() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testDependencyOnNonExistentDatasource.page.xml"),
                "Атрибут \"on\" в зависимости источника данных 'ds1' ссылается на несуществующий источник данных 'ds2'"
        );
    }

    /**
     * Проверяется, что тег <submit> on содержит несуществующий источник данных в атрибуте refresh-datasource
     */
    @Test
    void testSubmitWithNonExistentRefreshDatasource() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testSubmitWithNonExistentRefreshDatasource.page.xml"),
                "Тег <submit> источника данных 'ds1' содержит несуществующий источник данных 'ds2' в атрибуте \"refresh-datasources\""
        );
    }

    /**
     * Проверяется, что для префильтра в источнике данных указана выборка
     */
    @Test
    void testRequiredReferenceForPrefiltersQuery() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQuery.page.xml"),
                "Источник данных 'ds1' имеет префильтры, но не задана выборка"
        );
    }

    /**
     * Проверяется, в выборке есть поля при указании фильтров в источнике данных
     */
    @Test
    void testReferenceQueryFieldExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testReferenceQueryFieldExistence.page.xml"),
                "Источник данных 'ds1' имеет префильтры, но в выборке 'utDsBlank' нет filters!"
        );
    }

    /**
     * Проверяется, что источник данных, на который ссылается префильтр, существует
     */
    @Test
    void testNonExistentDatasourceInPrefilter() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentDatasourceInPrefilter.page.xml"),
                "В префильтре по полю 'id' указан несуществующий источник данных 'ds2'"
        );
    }

    /**
     * Проверяется, что для префильтра в выборке есть соответствующее поле
     */
    @Test
    void testRequiredReferenceForPrefiltersQueryField() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQueryField.page.xml"),
                "В выборке 'utDs' нет поля 'notExist'"
        );
    }

    /**
     * Проверяется, что для префильтра в выборке есть фильтр
     */
    @Test
    void testQueryFieldsFilterExistence() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testQueryFieldsFilterExistence.page.xml"),
                "В выборке 'utDs' поле 'id' не содержит фильтров!"
        );
    }

    /**
     * Проверяется, что для префильтра в выборке есть фильтр соответствующего типа
     */
    @Test
    void testRequiredReferenceForPrefiltersQueryFieldFilterType() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQueryFieldFilterType.page.xml"),
                "В выборке 'utDs' поле 'name' не содержит фильтр типа 'notEq'!"
        );
    }

    /**
     * Проверяется наличие обязательного атрибута field-id для префильтра
     */
    @Test
    void testRequiredPrefilterFieldId() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredPrefilterFieldId.page.xml"),
                "Источник данных 'ds1' содержит префильтр без указанного field-id!"
        );
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
}
