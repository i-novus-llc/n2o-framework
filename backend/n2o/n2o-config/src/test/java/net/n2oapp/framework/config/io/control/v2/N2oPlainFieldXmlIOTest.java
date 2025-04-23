package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.action.ShowModalElementIOV1;
import net.n2oapp.framework.config.io.control.v2.plain.PlainFieldIOv2;
import net.n2oapp.framework.config.io.dataprovider.SqlDataProviderIOv1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.test.XmlIOTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class N2oPlainFieldXmlIOTest extends XmlIOTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new PlainFieldIOv2() {

            @Override
            public String getElementName() {
                return "input-text";
            }

            @Override
            public Class getElementClass() {
                return N2oInputText.class;
            }
        }, new ShowModalElementIOV1(), new SqlDataProviderIOv1(), new ButtonIO());
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/control/v2/testBaseSimpleControl.xml");
    }
}

