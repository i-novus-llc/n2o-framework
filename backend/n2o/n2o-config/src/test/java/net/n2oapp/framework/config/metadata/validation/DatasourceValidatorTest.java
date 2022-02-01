package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.DatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирования валидации источника данных
 */
public class DatasourceValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(), new DatasourceValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/utDsBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/utDs.query.xml"));
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующий объект
     */
    @Test
    public void testNonExistentObject() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Источник данных ds1 ссылается на несуществующий объект object");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentObject.page.xml");
    }

    /**
     * Проверяется, что источник данных ссылается на несуществующую выборку
     */
    @Test
    public void testNonExistentQuery() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Источник данных ds1 ссылается на несуществующую выборку nonExistent");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentQuery.page.xml");
    }

    /**
     * Проверяется, что атрибут on в зависимости источника ссылается на несуществующий источник данных
     */
    @Test
    public void testFetchOnNonExistentDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут \"on\" в зависимости источника данных 'ds1' ссылается на несуществующий источник данных 'ds2'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testFetchOnNonExistentDatasource.page.xml");
    }

    /**
     * Проверяется, что тег <submit> on содержит несуществующий источник данных в атрибуте refresh-datasource
     */
    @Test
    public void testSubmitWithNonExistentRefreshDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Тег <submit> источника данных 'ds1' содержит несуществующий источник данных 'ds2' в атрибуте \"refresh-datasources\"");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testSubmitWithNonExistentRefreshDatasource.page.xml");
    }

    /**
     * Проверяется, что для префильтра в источнике данных указана выборка
     */
    @Test
    public void testRequiredReferenceForPrefiltersQuery() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Источник данных 'ds1' имеет префильтры, но не задана выборка");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQuery.page.xml");
    }

    /**
     * Проверяется, в выборке есть поля при указании фильтров в источнике данных
     */
    @Test
    public void testReferenceQueryFieldExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Источник данных 'ds1' имеет префильтры, но в выборке 'utDsBlank' нет fields");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testReferenceQueryFieldExistence.page.xml");
    }

    /**
     * Проверяется, что источник данных, на который ссылается префильтр, существует
     */
    @Test
    public void testNonExistentDatasourceInPrefilter() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В префильтре по полю id указан несуществующий источник данных 'ds2'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistentDatasourceInPrefilter.page.xml");
    }

    /**
     * Проверяется, что для префильтра в выборке есть соответствующее поле
     */
    @Test
    public void testRequiredReferenceForPrefiltersQueryField() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В выборке utDs нет field 'notExist'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQueryField.page.xml");
    }

    /**
     * Проверяется, что для префильтра в выборке есть фильтр
     */
    @Test
    public void testQueryFieldsFilterExistence() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В выборке utDs field 'id' не содержит фильтров!");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testQueryFieldsFilterExistence.page.xml");
    }

    /**
     * Проверяется, что для префильтра в выборке есть фильтр соответствующего типа
     */
    @Test
    public void testRequiredReferenceForPrefiltersQueryFieldFilterType() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В выборке utDs field 'name' не содержит фильтр типа 'notEq'!");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testRequiredReferenceForPrefiltersQueryFieldFilterType.page.xml");
    }
}
