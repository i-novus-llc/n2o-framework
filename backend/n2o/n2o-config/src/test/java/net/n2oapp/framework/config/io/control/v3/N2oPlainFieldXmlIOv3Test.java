package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.action.v2.ShowModalElementIOV2;
import net.n2oapp.framework.config.io.control.v3.plain.PlainFieldIOv3;
import net.n2oapp.framework.config.io.dataprovider.SqlDataProviderIOv1;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class N2oPlainFieldXmlIOv3Test extends XmlIOTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new PlainFieldIOv3() {

            @Override
            public String getElementName() {
                return "input-text";
            }

            @Override
            public Class getElementClass() {
                return N2oInputText.class;
            }
        }, new ShowModalElementIOV2(), new SqlDataProviderIOv1(), new ButtonIOv2());
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/control/v3/testBaseSimpleControlV3.xml");
    }
}

