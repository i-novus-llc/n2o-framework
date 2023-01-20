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
 * Тестирование валидатора для действия <submit>
 */
public class SubmitActionValidatorTest extends SourceValidationTestBase {

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
                new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack(), new N2oAllValidatorsPack());
    }

    @Test
    public void testMissingDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут 'datasource' действия <submit> ссылается на несуществующий источник данных");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationNoDatasource.page.xml");
    }

    @Test
    public void testMissingSubmitInDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Действие <submit> использует источник данных ds1, в котором не определен submit");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingSubmit.page.xml");
    }

    @Test
    public void testMissingDatasourceAttribute() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Для действия <submit> не задан 'datasource'");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingDatasource.page.xml");
    }

    @Test
    public void testUnsupportedDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Действие <submit> использует источник данных ds1, который не поддерживает submit");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationUnsupport.page.xml");
    }

    @Test
    public void testWidgetDatasourceUsing() {
        validate("net/n2oapp/framework/config/metadata/validation/action/testWidgetDatasourceUsing.page.xml");
    }

    @Test
    public void testParentDatasourceUsing() {
        validate("net/n2oapp/framework/config/metadata/validation/action/testParentDatasourceUsing.page.xml");
    }

}
