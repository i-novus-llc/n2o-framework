package net.n2oapp.framework.config.metadata.menu;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv2;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing validation of simple menu
 */
public class SimpleMenuValidateTest extends SourceValidationTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new SimpleMenuIOv2());
        builder.validators(new SimpleMenuValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml"));
    }

    @Test
    public void testMenuValidation() {
        validate("net/n2oapp/framework/config/metadata/menu/menuValidator.menu.xml");
    }
}
