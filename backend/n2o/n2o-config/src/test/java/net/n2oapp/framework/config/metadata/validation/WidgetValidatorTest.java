package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.ListWidgetValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации виджета
 */
public class WidgetValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new BasePageValidator(), new WidgetValidator(), new ListWidgetValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank2.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/validation/widget/testWidgetValidator.query.xml"));
    }

    /**
     * Проверяется, уникальность кнопок тулбара виджета
     */
    @Test
    void testUniqueMenuItemsId() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testUniqueMenuItemsId.widget.xml"),
                "Кнопка id2 встречается более чем один раз в виджете testUniqueMenuItemsId!"
        );
    }

    /**
     *  Проверяется, что префильтр ссылается через ref-widget на существующий виджет
     */
    @Test
    void testRequiredReferenceForPrefiltersFound() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters2.widget.xml");
    }

    /**
     * Проверяется, что учитываются параметры выборки
     */
    @Test
    void testQueryParams() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters8.widget.xml");
    }

    /**
     * Проверяется уникальность идентификаторов действий виджета
     */
    @Test
    void testActionIdsUnique() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testActionIdsUnique.widget.xml"),
                "Действие test встречается более чем один раз в метаданной виджета testActionIdsUnique"
        );
    }

    /**
     * Проверяется уникальность идентификаторов действий страницы и виджета
     */
    @Test
    void testPageAndWidgetActionIdsUnique() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testPageAndWidgetActionIdsUnique.page.xml"),
                "Идентификатор действия 'test' дублируется на странице и в виджете 'main'"
        );
    }

    @Test
    void testPaginationShowLastAndCount() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testPaginationShowLastAndCount.widget.xml"),
                "Используется некорректная комбинация атрибутов 'show-last=\"false\"' и 'show-count=\"always\"' пагинации виджета 'testPaginationShowLastAndCount'"
        );
    }
}
