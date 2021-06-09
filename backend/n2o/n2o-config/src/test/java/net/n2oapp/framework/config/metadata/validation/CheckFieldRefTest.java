package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;

public class CheckFieldRefTest extends SourceValidationTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new FormValidator(), new FieldSetValidator(), new FieldValidator());
    }

    @Test
    public void testCheckDefaultValueIsNotDefined() {
        assertOnException(() -> validate("net/n2oapp/framework/config/metadata/validation/fieldRef/testCheckDefaultValueIsNotDefined.widget.xml"),
                N2oMetadataValidationException.class, e -> {
                    assert e.getMessage().contains("У поля id атрибут default-value не является ссылкой или не задан: null");
                });
    }

    @Test
    public void testCheckDefaultValueIsNotLink() {
        assertOnException(() -> validate("net/n2oapp/framework/config/metadata/validation/fieldRef/testCheckDefaultValueIsNotLink.widget.xml"),
                N2oMetadataValidationException.class, e -> {
                    assert e.getMessage().contains("У поля id атрибут default-value не является ссылкой или не задан: ");
                });
    }
}
