package net.n2oapp.framework.config.metadata.validation.region;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.regions.SubPageRegionValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubPageRegionValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack());
        builder.validators(new StandardPageValidator(), new SubPageRegionValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/region/subpage/page1.page.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/region/subpage/page2.page.xml"));
    }

    @Test
    void testPageId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/subpage/testPageId.page.xml"));
        assertEquals("В одном из элементов <sub-page> не указан обязательный атрибут 'page-id'", exception.getMessage());
    }

    @Test
    void testRoute() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/subpage/testRoute.page.xml"));
        assertEquals("В одном из элементов <sub-page> не указан обязательный атрибут 'route'", exception.getMessage());
    }

    @Test
    void testPagesExist() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/subpage/testPagesExist.page.xml"));
        assertEquals("Один из элементов <sub-page> ссылается на несуществующую страницу 'notExistingPage'", exception.getMessage());
    }

    @Test
    void testDefaultPageId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/subpage/testDefaultPageId.page.xml"));
        assertEquals("В атрибуте 'default-page-id' элемента <sub-page> указана страница, которая не используется ни в одном из внутренних элементов <page>", exception.getMessage());
    }

    @Test
    void testNonUniqueRoutes() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/subpage/testNonUniqueRoutes.page.xml"));
        assertEquals("В элементах <sub-page> указаны повторяющиеся маршруты \"/route\"", exception.getMessage());
    }

    @Test
    void testEmptyToolbar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/subpage/testEmptyToolbar.page.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре элемента <sub-page> 'page1'", exception.getMessage());
    }
}