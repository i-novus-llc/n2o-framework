package net.n2oapp.framework.access.metadata;

import net.n2oapp.framework.access.metadata.accesspoint.io.ObjectAccessPointIOv2;
import net.n2oapp.framework.access.metadata.accesspoint.io.UrlAccessPointIOv2;
import net.n2oapp.framework.access.metadata.schema.io.SimpleAccessIOv2;
import net.n2oapp.framework.access.metadata.schema.simple.N2oSimpleAccessSchema;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class SimpleAccessSchemaIOTest {

    final static ION2oMetadataTester tester = new ION2oMetadataTester()
            .addIO(new ObjectAccessPointIOv2())
            .addIO(new SimpleAccessIOv2())
            .addIO(new UrlAccessPointIOv2());

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
