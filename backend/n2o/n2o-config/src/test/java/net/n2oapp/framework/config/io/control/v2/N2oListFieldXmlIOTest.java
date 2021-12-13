package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.control.v2.list.ListFieldIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

public class N2oListFieldXmlIOTest extends XmlIOTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new ListFieldIOv2() {

            @Override
            public String getElementName() {
                return "input-select";
            }

            @Override
            public Class getElementClass() {
                return N2oInputSelect.class;
            }
        });
    }

    @Test
    public void test() {
        check("net/n2oapp/framework/config/io/control/v2/testBaseListControl.xml");
    }
}

