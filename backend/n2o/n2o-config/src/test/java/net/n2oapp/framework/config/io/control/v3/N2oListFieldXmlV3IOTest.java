package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.control.v3.list.ListFieldIOv3;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

public class N2oListFieldXmlV3IOTest extends XmlIOTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new ListFieldIOv3() {

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
        check("net/n2oapp/framework/config/io/control/v3/testBaseListControlV3.xml");
    }
}

