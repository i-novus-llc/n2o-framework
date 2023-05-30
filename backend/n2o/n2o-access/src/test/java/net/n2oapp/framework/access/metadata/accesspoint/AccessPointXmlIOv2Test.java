package net.n2oapp.framework.access.metadata.accesspoint;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFiltersAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oUrlAccessPoint;
import net.n2oapp.framework.access.metadata.pack.AccessPointsIOV2Pack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

public class AccessPointXmlIOv2Test {

    final static ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new AccessPointsIOV2Pack());

    @Test
    void objectAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/object2.accesspoint.xml",
                (N2oObjectAccessPoint access) -> {
                    assert access.getObjectId().equals("test");
                    assert access.getAction().equals("create");
                });
    }

    @Test
    void objectFiltersAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/objectFilters.accesspoint.xml",
                (N2oObjectFiltersAccessPoint access) -> {
                    assert access.getObjectId().equals("test");
                    assert access.getFilters().length == 3;
                });
    }

    @Test
    void pageAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/page2.accesspoint.xml",
                (N2oPageAccessPoint access) -> {
                    assert access.getPage().equals("test");
                });
    }

    @Test
    void urlAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/url2.accesspoint.xml",
                (N2oUrlAccessPoint access) -> {
                    assert access.getPattern().equals("test");
                });
    }
}
