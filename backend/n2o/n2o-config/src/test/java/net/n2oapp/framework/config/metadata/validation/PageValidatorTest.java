package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации страницы
 */
public class PageValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.validators(new PageValidator(), new StandardPageValidator(), new BasePageValidator());
    }

    @Test
    public void testObjectNotExists() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Страница testObjectNotExists ссылается на несуществующий объект nonExistantObjectId");
        validate("net/n2oapp/framework/config/metadata/validation/page/testObjectNotExists.page.xml");
    }

    @Test
    public void testObjectNotExistsOnSimplePage() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Страница testObjectNotExistsOnSimplePage ссылается на несуществующий объект nonExistantObjectId");
        validate("net/n2oapp/framework/config/metadata/validation/page/testObjectNotExistsOnSimplePage.page.xml");
    }

    @Test
    public void testDependsWidgetFind() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут depends-on ссылается на несуществующий виджет main");
        validate("net/n2oapp/framework/config/metadata/validation/page/testDependsWidgetFind.page.xml");
    }

    @Test
    public void testWidgetDatasource() {
        validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetSameDatasource.page.xml");
    }

    @Test
    public void testWidgetDatasourceNotSameQueryId() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("2 виджета с одинаковым datasource ds1 имеют разные query-id или object-id");
        validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetDatasourceNotSameQueryId.page.xml");
    }

    @Test
    public void testWidgetDatasourceNotSameObjectId() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("2 виджета с одинаковым datasource ds1 имеют разные query-id или object-id");
        validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetDatasourceNotSameObjectId.page.xml");
    }

    @Test
    public void testWidgetDatasourceQueryIdIsNull() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("2 виджета с одинаковым datasource ds1 имеют разные query-id или object-id");
        validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetDatasourceQueryIdIsNull.page.xml");
    }

    @Test
    public void testWidgetDatasourceObjectIdIsNull() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("2 виджета с одинаковым datasource ds1 имеют разные query-id или object-id");
        validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetDatasourceObjectIdIsNull.page.xml");
    }
}
