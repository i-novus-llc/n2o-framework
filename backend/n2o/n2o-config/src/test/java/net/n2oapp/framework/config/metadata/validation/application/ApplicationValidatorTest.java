package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.metadata.validation.standard.application.ApplicationValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тест валидатора приложения
 */
class ApplicationValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIOv3(), new SidebarIOv3(), new StandardDatasourceIO());
        builder.validators(new ApplicationValidator());

    }

    @Test
    void testCheckDatasourceIdsUnique() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/checkDatasourceIdsUnique.application.xml"));
        assertEquals("Источник данных 'ds1' встречается более чем один раз в метаданной приложения 'checkDatasourceIdsUnique'", exception.getMessage());
    }

    @Test
    void testHeaderLinkTitleNotExistDS() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/header/applicationHeaderTitleValidate.application.xml"));
        assertEquals("Заголовок компонента <header> имеет плейсхолдер, но при этом не указан источник данных", exception.getMessage());
    }

    @Test
    void testHeaderDatasourceExistent() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/header/applicationHeaderDatasourceExistentValidate.application.xml"));
        assertEquals("<header> ссылается на несуществующий источник данных 'ds2'", exception.getMessage());
    }
}
