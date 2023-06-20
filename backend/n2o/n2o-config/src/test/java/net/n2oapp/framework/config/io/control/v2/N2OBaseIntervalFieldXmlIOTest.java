package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.control.v2.interval.BaseIntervalFieldIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class N2OBaseIntervalFieldXmlIOTest extends XmlIOTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new BaseIntervalFieldIOv2() {

            @Override
            public String getElementName() {
                return "date-interval";
            }

            @Override
            public Class getElementClass() {
                return N2oDateInterval.class;
            }
        });
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/control/v2/testBaseIntervalControl.xml");
    }
}

