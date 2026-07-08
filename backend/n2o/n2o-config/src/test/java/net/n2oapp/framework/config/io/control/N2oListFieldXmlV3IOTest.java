package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.control.list.ListFieldIOv3;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class N2oListFieldXmlV3IOTest extends XmlIOTestBase {
    
    @Override
    @BeforeEach
    public void setUp() {
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
    void test() {
        check("net/n2oapp/framework/config/io/control/testBaseListControlV3.xml");
    }

    @Test
    void testMultipleDefaultValues() {
        check("net/n2oapp/framework/config/io/control/testMultipleDefaultValuesBaseListControlV3.xml");
    }
}

