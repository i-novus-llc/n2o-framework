package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.metadata.validation.standard.application.ApplicationValidator;
import net.n2oapp.framework.config.metadata.validation.standard.application.SidebarValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидатора боковой панели приложения
 */
class SidebarValidatorTest extends SourceValidationTestBase {
    
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
    void testEmptyPathValidationFail() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/sidebar/emptyPathValidationFail.application.xml"));
        assertEquals("Приложение имеет более одного <sidebar> не содержащих 'path'", exception.getMessage());
    }

    @Test
    void testEqualPathsValidationFail() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/sidebar/equalPathsValidationFail.application.xml"));
        assertEquals("Два или более <sidebar> имеют одинаковый 'path = /persons'", exception.getMessage());
    }

    @Test
    void testExistingSidebarInlineDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/sidebarDatasourceDuplicate.application.xml"));
        assertEquals("Идентификатор 'person' внутреннего источника данных сайдбара уже используется другим источником данных", exception.getMessage());
    }

    @Test
    void testUsingInlineDatasourceAndLinkAtTheSameTime() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/testUsingInlineDatasourceAndLinkAtTheSameTime.application.xml"));
        assertEquals("Сайдбар использует внутренний источник данных и ссылку на источник данных 'ds' одновременно", exception.getMessage());
    }

    @Test
    void testLinkToNonExistentDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/testLinkToNonExistentDatasource.application.xml"));
        assertEquals("Сайдбар ссылается на несуществующий источник данных 'ds'", exception.getMessage());
    }
}
