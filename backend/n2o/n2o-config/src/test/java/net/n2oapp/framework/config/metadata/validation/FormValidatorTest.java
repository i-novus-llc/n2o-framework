package net.n2oapp.framework.config.metadata.validation;


import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetColumnValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetRowValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.PropertyResolver;

public class FormValidatorTest extends SourceValidationTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        PropertyResolver props = builder.getEnvironment().getSystemProperties();
        builder.validators(new FormValidator(props), new FieldSetRowValidator(props),
                new FieldSetColumnValidator(props), new FieldSetValidator(props), new FieldValidator());
    }

    @Test
    public void case1() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/form/testFieldIdValidation.widget.xml");
    }

    @Test
    public void case2() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/form/testFieldIdValidation2.widget.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void case3() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/form/testFieldIdValidation3.widget.xml");
    }

}
