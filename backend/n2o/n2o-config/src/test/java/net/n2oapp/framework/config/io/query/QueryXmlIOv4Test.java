package net.n2oapp.framework.config.io.query;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи выборки версии 4
 */
public class QueryXmlIOv4Test {

    @Test
    public void testQueryXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addIO(new QueryElementIOv4())
                .addPack(new N2oDataProvidersIOPack());

        assert tester.check("net/n2oapp/framework/config/io/query/testQueryIOv4.query.xml",
                (N2oQuery query) -> {
                    assert query.getName().equals("query 4");
                    assert query.getObjectId().equals("blank");
                });
    }
}
