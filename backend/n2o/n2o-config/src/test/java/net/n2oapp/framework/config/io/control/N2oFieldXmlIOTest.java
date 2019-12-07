package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

public class N2oFieldXmlIOTest extends XmlIOTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new InputTextIOv2() {

            @Override
            public String getElementName() {
                return "input-text";
            }

            @Override
            public Class getElementClass() {
                return N2oInputText.class;
            }
        });
    }

    @Test
    public void test() {
        check("net/n2oapp/framework/config/io/control/v2/testBaseFieldControl.xml");
    }
}
