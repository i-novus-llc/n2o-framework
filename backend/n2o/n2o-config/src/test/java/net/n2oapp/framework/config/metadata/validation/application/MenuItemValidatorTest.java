package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllValidatorsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации элементов меню
 */
class MenuItemValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oActionsPack(), new N2oAllValidatorsPack());
    }

    @Test
    void testNonDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/nav/testDatasourceExistenceFail.page.xml"));
        assertEquals("<menu-item label='test'> ссылается на несуществующий источник данных 'test'", exception.getMessage());
    }

    @Test
    void testBadgeColor() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/nav/testMenuItemBadgeColor.page.xml"));
        assertEquals("<menu-item label='test'> использует недопустимое значение атрибута badge-color=\"red\"", exception.getMessage());
    }

    @Test
    void testMissedPageActions() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/nav/testMissedPageActions.page.xml"));
        assertEquals("Для компонента с action-id=\"test\" не найдены действия <actions>", exception.getMessage());
    }

    @Test
    void testActionExistenceByActionId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/nav/testActionExistenceByActionId.page.xml"));
        assertEquals("Компонент с action-id=\"test\" ссылается на несуществующее действие test", exception.getMessage());
    }

    @Test
    void testUsingActionIdAndActionAtTheSameTime() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/nav/testUsingActionIdAndActionAtTheSameTime.page.xml"));
        assertEquals("Компонент с action-id=\"test\" содержит действия и использует ссылку action-id одновременно", exception.getMessage());
    }

    @Test
    void testMissedHref() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/nav/testMissedHref.page.xml"));
        assertEquals("Для элемента меню <link> не задан `href`", exception.getMessage());
    }

    @Test
    void testWrongTarget() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/region/nav/testWrongTarget.page.xml"));
        assertEquals("В элементе меню <link> при абсолютном пути (http\\https) не может быть задан target=\"application\"", exception.getMessage());
    }
}
