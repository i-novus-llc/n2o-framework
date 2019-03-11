package net.n2oapp.framework.access.metadata.accesspoint;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.access.metadata.pack.AccessPointsV1PersistersPack;
import net.n2oapp.framework.access.metadata.pack.AccessPointsV1ReadersPack;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class AccessPointXmlIOTest {

    final static ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new AccessPointsV1ReadersPack())
            .addPack(new AccessPointsV1PersistersPack());


    @Test
    public void objectAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/object.accesspoint.xml",
                (N2oObjectAccessPoint access) -> {
                    assert access.getObjectId().equals("test");
                    assert access.getAction().equals("create");
                });
    }

    @Test
    public void containerAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/container.accesspoint.xml",
                (N2oContainerAccessPoint access) -> {
                    assert access.getContainer().equals("test");
                    assert access.getPage().equals("test");
                });
    }

    @Test
    public void menuAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/menu.accesspoint.xml",
                (N2oMenuItemAccessPoint access) -> {
                    assert access.getContainer().equals("test");
                    assert access.getPage().equals("test");
                    assert access.getMenuItem().equals("test");
                });
    }

    @Test
    public void moduleAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/module.accesspoint.xml",
                (N2oModuleAccessPoint access) -> {
                    assert access.getModule().equals("test");
                });
    }

    @Test
    public void pageAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/page.accesspoint.xml",
                (N2oPageAccessPoint access) -> {
                    assert access.getPage().equals("test");
                });
    }

    @Test
    public void referenceAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/reference.accesspoint.xml",
                (N2oReferenceAccessPoint access) -> {
                    assert access.getObjectId().equals("test");
                });
    }

    @Test
    public void urlAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/url.accesspoint.xml",
                (N2oUrlAccessPoint access) -> {
                    assert access.getPattern().equals("test");
                });
    }

    @Test
    public void columnAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/column.accesspoint.xml",
                (N2oColumnAccessPoint access) -> {
                    assert access.getPageId().equals("test");
                    assert access.getContainerId().equals("test");
                    assert access.getColumnId().equals("test");
                });
    }

    @Test
    public void filterAccessPointXmlIOTest() {
        tester.check("net/n2oapp/framework/access/metadata/accesspoint/filter.accesspoint.xml",
                (N2oFilterAccessPoint access) -> {
                    assert access.getQueryId().equals("test");
                    assert access.getFilterId().equals("test");
                });
    }

}
