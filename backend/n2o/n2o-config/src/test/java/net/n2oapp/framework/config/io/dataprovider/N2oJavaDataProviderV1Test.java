package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class N2oJavaDataProviderV1Test {

    @Test
    public void ioEjb() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new JavaDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oEjbDataProviderIOTest.xml");
    }

    @Test
    public void ioSpring() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new JavaDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oSpringDataProviderIOTest.xml");
    }

    @Test
    public void ioStatic() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new JavaDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oStaticDataProviderIOTest.xml");
    }
}
