package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.control.interval.IntervalFieldIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

public class N2oIntervalFieldXmlIOTest extends XmlIOTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new IntervalFieldIOv2() {

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
    public void test() {
        check("net/n2oapp/framework/config/io/control/v2/testBaseIntervalControl.xml");
    }
}

