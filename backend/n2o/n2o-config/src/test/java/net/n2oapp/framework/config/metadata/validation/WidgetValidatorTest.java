package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.validators(new WidgetValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"));
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testRequiredReferenceForPrefilters() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters.widget.xml");
    }

    @Test
    public void testRequiredReferenceForPrefiltersFound() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters2.widget.xml");
    }
}
