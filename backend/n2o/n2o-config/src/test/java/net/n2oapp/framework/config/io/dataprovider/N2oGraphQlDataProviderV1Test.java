package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

class N2oGraphQlDataProviderV1Test {
    
    @Test
    void io() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new GraphQlDataProviderIOv1());
        tester.check("net/n2oapp/framework/config/io/dataprovider/n2oGraphqlDataProviderIOTest.xml");
    }
}
