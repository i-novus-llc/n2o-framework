package net.n2oapp.framework.config.io.query;

import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи выборки версии 5
 */
class QueryXmlIOv5Test {

    @Test
    void testQueryXmlIOV5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new QueryElementIOv5())
                .addPack(new N2oDataProvidersIOPack());
        assert tester.check("net/n2oapp/framework/config/io/query/testQueryXmlIOV5.query.xml");
    }
}
