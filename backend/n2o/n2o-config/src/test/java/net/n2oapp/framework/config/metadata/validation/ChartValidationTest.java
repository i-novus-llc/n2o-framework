package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.metadata.validation.standard.widget.ChartValidation;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;


/**
 * @author V. Alexeev.
 * @date 03.03.2016
 */
public class ChartValidationTest extends SourceValidationTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oQueriesPack());
        builder.validators(new ChartValidation())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/chart/chartValidation.query.xml"));
    }


    @Test(expected = N2oMetadataValidationException.class)
    public void testDisplayValidation() {
        validate("net/n2oapp/framework/config/metadata/validation/chart/chartDisplayValidation.widget.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testLabelFieldValidation() {
        validate("net/n2oapp/framework/config/metadata/validation/chart/chartLabelValidation.widget.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testValuesValidation() {
        validate("net/n2oapp/framework/config/metadata/validation/chart/chartValuesValidation.widget.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testValueValidation() {
        validate("net/n2oapp/framework/config/metadata/validation/chart/chartValueValidation.widget.xml");
    }

    @Test(expected = ReferentialIntegrityViolationException.class)
    public void testQueryValidation() {
        validate("net/n2oapp/framework/config/metadata/validation/chart/chartQueryValidation.widget.xml");
    }

}
