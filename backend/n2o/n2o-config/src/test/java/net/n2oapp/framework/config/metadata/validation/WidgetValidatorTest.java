package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации виджета
 */
public class WidgetValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new WidgetValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank2.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/validation/widget/testWidgetValidator.query.xml"));
    }

    /**
     * Проверяется, уникальность кнопок тулбара виджета
     */
    @Test
    public void testUniqueMenuItemsId() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Кнопка id2 встречается более чем один раз в виджете testUniqueMenuItemsId!");
        validate("net/n2oapp/framework/config/metadata/validation/widget/testUniqueMenuItemsId.widget.xml");
    }

    /**
     *  Проверяется, что префильтр ссылается через ref-widget на существующий виджет
     */
    @Test
    public void testRequiredReferenceForPrefiltersFound() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters2.widget.xml");
    }

    /**
     * Проверяется, что учитываются параметры выборки
     */
    @Test
    public void testQueryParams() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters8.widget.xml");
    }
}
