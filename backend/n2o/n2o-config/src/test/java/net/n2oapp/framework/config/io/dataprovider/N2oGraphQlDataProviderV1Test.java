package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class N2oGraphQlDataProviderV1Test {
    @Test
    public void io() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new GraphQlDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oGraphqlDataProviderIOTest.xml");
    }
}
