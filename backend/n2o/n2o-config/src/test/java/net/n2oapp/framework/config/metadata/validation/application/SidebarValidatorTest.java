package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationValidator;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидатора боковой панели приложения
 */
public class SidebarValidatorTest extends SourceValidationTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIOv3(), new SidebarIOv3(), new StandardDatasourceIO());
        builder.validators(new ApplicationValidator(), new SidebarValidator());
    }

    @Test
    void testValidation() {
        validate("net/n2oapp/framework/config/metadata/application/sidebar/validation.application.xml");
    }

    @Test
    void testValidationFail() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/sidebar/validationFail.application.xml"),
                "Menu test doesnt exists for sidebar"
        );
    }

    @Test
    void testEmptyPathValidationFail() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/sidebar/emptyPathValidationFail.application.xml"),
                "More than one sidebar does not contain a path"
        );
    }

    @Test
    void testEqualPathsValidationFail() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/sidebar/equalPathsValidationFail.application.xml"),
                "The /persons path is already taken by one of the sidebars"
        );
    }

    @Test
    void testExistingSidebarInlineDatasource() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/sidebarDatasourceDuplicate.application.xml"),
                "Идентификатор 'person' внутреннего источника данных сайдбара уже используется другим источником данных"
        );
    }

    @Test
    void testUsingInlineDatasourceAndLinkAtTheSameTime() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/testUsingInlineDatasourceAndLinkAtTheSameTime.application.xml"),
                "Сайдбар использует внутренний источник данных и ссылку на источник данных 'ds' одновременно"
        );
    }

    @Test
    void testLinkToNonExistentDatasource() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/testLinkToNonExistentDatasource.application.xml"),
                "Сайдбар ссылается на несуществующий источник данных 'ds'"
        );
    }
}
