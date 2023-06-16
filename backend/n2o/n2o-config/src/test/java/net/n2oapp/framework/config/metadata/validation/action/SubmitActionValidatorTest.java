package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидатора для действия <submit>
 */
public class SubmitActionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
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
    void testMissingDatasource() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationNoDatasource.page.xml"),
                "Атрибут 'datasource' действия <submit> ссылается на несуществующий источник данных"
        );
    }

    @Test
    void testMissingSubmitInDatasource() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingSubmit.page.xml"),
                "Действие <submit> использует источник данных ds1, в котором не определен submit"
        );
    }

    @Test
    void testMissingDatasourceAttribute() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingDatasource.page.xml"),
                "Для действия <submit> не задан 'datasource'"
        );
    }

    @Test
    void testUnsupportedDatasource() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationUnsupport.page.xml"),
                "Действие <submit> использует источник данных ds1, который не поддерживает submit"
        );
    }

    @Test
    void testWidgetDatasourceUsing() {
        validate("net/n2oapp/framework/config/metadata/validation/action/testWidgetDatasourceUsing.page.xml");
    }

    @Test
    void testParentDatasourceUsing() {
        validate("net/n2oapp/framework/config/metadata/validation/action/testParentDatasourceUsing.page.xml");
    }

}
