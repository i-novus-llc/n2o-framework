package net.n2oapp.framework.access.metadata;

import net.n2oapp.framework.access.metadata.pack.AccessPointsV1PersistersPack;
import net.n2oapp.framework.access.metadata.pack.AccessPointsV1ReadersPack;
import net.n2oapp.framework.access.metadata.schema.simple.N2oSimpleAccessSchema;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaPersister;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaReaderV1;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class SimpleAccessSchemaIOTest {

    final static ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(new SimpleAccessSchemaReaderV1())
            .addPersister(new SimpleAccessSchemaPersister())
            .addPack(new AccessPointsV1ReadersPack())
            .addPack(new AccessPointsV1PersistersPack());

    @Test
    public void simpleAccessSchemaXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/simple.access.xml",
                (N2oSimpleAccessSchema access) -> {
                    assert access.getN2oRoles().length == 1;
                    assert access.getN2oPermissions().length == 1;
                    assert access.getPermitAllPoints().length == 2;
                });
    }

}
