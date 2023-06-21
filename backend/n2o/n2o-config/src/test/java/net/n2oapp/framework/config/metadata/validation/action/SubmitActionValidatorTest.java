package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationNoDatasource.page.xml"));
        assertEquals("Атрибут 'datasource' действия <submit> ссылается на несуществующий источник данных", exception.getMessage());
    }

    @Test
    void testMissingSubmitInDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingSubmit.page.xml"));
        assertEquals("Действие <submit> использует источник данных ds1, в котором не определен submit", exception.getMessage());
    }

    @Test
    void testMissingDatasourceAttribute() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingDatasource.page.xml"));
        assertEquals("Для действия <submit> не задан 'datasource'", exception.getMessage());
    }

    @Test
    void testUnsupportedDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationUnsupport.page.xml"));
        assertEquals("Действие <submit> использует источник данных ds1, который не поддерживает submit", exception.getMessage());
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
