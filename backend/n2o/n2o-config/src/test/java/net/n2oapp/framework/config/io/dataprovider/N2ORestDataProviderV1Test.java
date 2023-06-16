package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

public class N2ORestDataProviderV1Test {

    @Test
    void io() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new RestDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oRestDataProviderIOTest.xml");
    }
}
