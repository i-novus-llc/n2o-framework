package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.metadata.validation.standard.application.ApplicationValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тест валидатора приложения
 */
public class ApplicationValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIOv3(), new SidebarIOv3(), new StandardDatasourceIO());
        builder.validators(new ApplicationValidator());

    }

    @Test
    void  testHeaderMenuRefIdExistence() {
        builder.sources(new CompileInfo("menuForHeaderValidation", N2oSimpleMenu.class));
        validate("net/n2oapp/framework/config/metadata/application/header/applicationHeaderMenuValidate.application.xml");
    }

    @Test
    void testHeaderMenuRefIdExistenceFail() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/header/applicationHeaderMenuValidateFail.application.xml"));
        assertEquals("<menu> хедера приложения 'applicationHeaderMenuValidateFail' ссылается на несуществующий 'ref-id = test'", exception.getMessage());
    }

    @Test
    void  testHeaderExtraMenuRefIdExistence() {
        builder.sources(new CompileInfo("menuForHeaderValidation", N2oSimpleMenu.class));
        validate("net/n2oapp/framework/config/metadata/application/header/applicationHeaderExtraMenuValidate.application.xml");
    }

    @Test
    void testHeaderExtraMenuRefIdExistenceFail() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/header/applicationHeaderExtraMenuValidateFail.application.xml"));
        assertEquals("<extra-menu> хедера приложения 'applicationHeaderExtraMenuValidateFail' ссылается на несуществующий 'ref-id = test'", exception.getMessage());
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
