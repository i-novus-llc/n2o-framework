package net.n2oapp.framework.config.reader.page;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения страницы версии 1.0 из xml файла
 */
public class Page1ReaderTest {

    @Test
    public void testPage1ReaderWithRegions() {
        N2oStandardPage page = new SelectiveStandardReader().addReader(new PageXmlReaderV1()).addWidgetReaderV3().addFieldSet3Reader()
                .readByPath("net/n2oapp/framework/config/reader/page/testPageElementReaderV1WithRegions.page.xml");
        assert page.getPostfix().equals("page");
        assert page.getName().equals("pageV1");
        assert page.getObjectId().equals("stub");
        assert ((N2oRegion) page.getItems()[0]).getPlace().equals("single");
        assert ((N2oRegion) page.getItems()[0]).getSrc().equals("test");
        assert ((N2oRegion) page.getItems()[0]).getSrc().equals("test");
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getId().equals("tabBottom");
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getDependencyCondition().equals("dependencyCondTest");
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getDependsOn().equals("depOnTest");
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getIcon().equals("iconTest");
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getRefreshDependentContainer().equals(false);
        assert (((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getPreFilters()[0]).getFieldId().equals("testField");
        assert (((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getPreFilters()[0]).getValue().equals("{refId}");
        assert (((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getPreFilters()[0]).getType().equals(FilterType.eq);
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getCounter().getPreFilters()[0].getFieldId().equals("filterFiled");
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getCounter().getPreFilters()[0].getValue().equals("true");
    }

    @Test
    public void testPage1ReaderWithContainers() {
        N2oStandardPage page = new SelectiveStandardReader().addReader(new PageXmlReaderV1()).addWidgetReaderV3().addFieldSet3Reader()
                .readByPath("net/n2oapp/framework/config/reader/page/testPageElementReaderV1WithContainers.page.xml");
        assert page.getPostfix().equals("page");
        assert page.getName().equals("pageV1");
        assert page.getObjectId().equals("stub");
        assert ((N2oRegion) page.getItems()[0]).getPlace().equals("bottom");
        assert page.getWidgets().get(0).getId().equals("tabBottom");
        assert page.getWidgets().get(0).getDependencyCondition().equals("dependencyCondTest");
        assert page.getWidgets().get(0).getDependsOn().equals("depOnTest");
        assert page.getWidgets().get(0).getIcon().equals("iconTest");
        assert page.getWidgets().get(0).getRefreshDependentContainer().equals(false);
        assert (page.getWidgets().get(0).getPreFilters()[0]).getFieldId().equals("testField");
        assert (page.getWidgets().get(0).getPreFilters()[0]).getValue().equals("name");
        assert (page.getWidgets().get(0).getPreFilters()[0]).getType().equals(FilterType.eq);
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getCounter().getPreFilters()[0].getFieldId().equals("filterFiled");
        assert ((N2oWidget) ((N2oRegion) page.getItems()[0]).getContent()[0]).getCounter().getPreFilters()[0].getValue().equals("true");
        assert page.getWidgets().get(0).getCounter().getPreFilters()[0].getFieldId().equals("filterFiled");
        assert page.getWidgets().get(0).getCounter().getPreFilters()[0].getValue().equals("true");
    }
}
