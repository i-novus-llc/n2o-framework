package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.control.v3.interval.BaseIntervalFieldIOv3;
import net.n2oapp.framework.config.io.control.v3.interval.DateIntervalIOv3;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование чтения, записи ввода интервала версии 3.0
 */
public class N2OBaseIntervalFieldXmlIOv3Test extends XmlIOTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new BaseIntervalFieldIOv3() {

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
        check("net/n2oapp/framework/config/io/control/v3/testBaseIntervalControlV3.xml");
    }
}

