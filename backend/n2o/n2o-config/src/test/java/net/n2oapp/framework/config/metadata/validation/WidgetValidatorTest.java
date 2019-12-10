package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

public class WidgetValidatorTest extends SourceValidationTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oQueriesPack());
        builder.validators(new WidgetValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"));
    }

    /**
     * Проверяется, что в виджете указана выборка
     */
    @Test(expected = N2oMetadataValidationException.class)
    public void testRequiredReferenceForPrefilters() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters.widget.xml");
    }

    @Test
    public void testRequiredReferenceForPrefiltersFound() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters2.widget.xml");
    }

    /**
     * Проверяется, что в виджете указана выборка
     */
    @Test(expected = N2oMetadataValidationException.class)
    public void testRequiredReferenceForPrefiltersQuery() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters3.widget.xml");
    }

    /**
     * Проверяется, что в выборке есть соответсвующий field
     */
    @Test(expected = N2oMetadataValidationException.class)
    public void testRequiredReferenceForPrefiltersQueryField() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters5.widget.xml");
    }

    /**
     * Проверяется, что в выборке есть фильтр соответсвующего типа
     */
    @Test(expected = N2oMetadataValidationException.class)
    public void testRequiredReferenceForPrefiltersQueryFieldFilterType() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters6.widget.xml");
    }
}
