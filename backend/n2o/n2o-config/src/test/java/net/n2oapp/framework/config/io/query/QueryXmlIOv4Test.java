package net.n2oapp.framework.config.io.query;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения и записи выборки версии 4
 */
public class QueryXmlIOv4Test {

    @Test
    public void testQueryXmlIOV4(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addReader(new QueryElementIOv4()).addDataProviders())
                .addPersister(new SelectiveStandardPersister().addPersister(new QueryElementIOv4()).addDataProviders());

        assert tester.check("net/n2oapp/framework/config/io/query/testQueryIOv4.query.xml",
                (N2oQuery query) -> {
                    assert query.getName().equals("query 4");
                    assert query.getObjectId().equals("blank");
                });
    }
}
