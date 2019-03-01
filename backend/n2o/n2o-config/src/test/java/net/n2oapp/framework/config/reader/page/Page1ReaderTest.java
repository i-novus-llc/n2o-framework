package net.n2oapp.framework.config.reader.page;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения страницы версии 1.0 из xml файла
 */
public class Page1ReaderTest {

    @Test
    public void testPage1ReaderWithRegions() throws Exception{
        N2oStandardPage page = new SelectiveStandardReader().addReader(new PageXmlReaderV1()).addWidgetReaderV3().addFieldSet3Reader()
                .readByPath("net/n2oapp/framework/config/reader/page/testPageElementReaderV1WithRegions.page.xml");
        assert page.getPostfix().equals("page");
        assert page.getName().equals("pageV1");
        assert page.getObjectId().equals("stub");
        assert page.getLayout().equals("SingleLayout");
        assert page.getModalWidth().equals("500px");
        assert page.getMaxModalWidth().equals("1000px");
        assert page.getMinModalWidth().equals("200px");
        assert page.getNavigation().equals(true);
        assert page.getResultContainer().equals("test");
        assert page.getN2oRegions()[0].getPlace().equals("single");
        assert page.getN2oRegions()[0].getSrc().equals("test");
        assert page.getN2oRegions()[0].getWidth().equals("200px");
        assert page.getN2oRegions()[0].getName().equals("test");
        assert page.getN2oRegions()[0].getSrc().equals("test");
        assert page.getN2oRegions()[0].getWidgets()[0].getId().equals("tabBottom");
        assert page.getN2oRegions()[0].getWidgets()[0].getDependencyCondition().equals("dependencyCondTest");
        assert page.getN2oRegions()[0].getWidgets()[0].getDependsOn().equals("depOnTest");
        assert page.getN2oRegions()[0].getWidgets()[0].getIcon().equals("iconTest");
        assert page.getN2oRegions()[0].getWidgets()[0].getOpened().equals(false);
        assert page.getN2oRegions()[0].getWidgets()[0].getRefreshDependentContainer().equals(false);
        assert page.getN2oRegions()[0].getWidgets()[0].getQueryId().equals("stub");
        assert ((N2oPreFilter) page.getN2oRegions()[0].getWidgets()[0].getPreFilters()[0]).getFieldId().equals("testField");
        assert ((N2oPreFilter) page.getN2oRegions()[0].getWidgets()[0].getPreFilters()[0]).getValue().equals("{refId}");
        assert ((N2oPreFilter) page.getN2oRegions()[0].getWidgets()[0].getPreFilters()[0]).getType().equals(FilterType.eq);
        assert page.getN2oRegions()[0].getWidgets()[0].getCounter().getPreFilters()[0].getFieldId().equals("filterFiled");
        assert page.getN2oRegions()[0].getWidgets()[0].getCounter().getPreFilters()[0].getValue().equals("true");
    }

    @Test
    public void testPage1ReaderWithContainers() throws Exception{
        N2oStandardPage page = new SelectiveStandardReader().addReader(new PageXmlReaderV1()).addWidgetReaderV3().addFieldSet3Reader()
                .readByPath("net/n2oapp/framework/config/reader/page/testPageElementReaderV1WithContainers.page.xml");
        assert page.getPostfix().equals("page");
        assert page.getName().equals("pageV1");
        assert page.getObjectId().equals("stub");
        assert page.getLayout().equals("SingleLayout");
        assert page.getModalWidth().equals("500px");
        assert page.getMaxModalWidth().equals("1000px");
        assert page.getMinModalWidth().equals("200px");
        assert page.getNavigation().equals(true);
        assert page.getResultContainer().equals("test");
        assert page.getN2oRegions()[0].getPlace().equals("bottom");
        assert page.getContainers().get(0).getId().equals("tabBottom");
        assert page.getContainers().get(0).getDependencyCondition().equals("dependencyCondTest");
        assert page.getContainers().get(0).getDependsOn().equals("depOnTest");
        assert page.getContainers().get(0).getIcon().equals("iconTest");
        assert page.getContainers().get(0).getOpened().equals(false);
        assert page.getContainers().get(0).getRefreshDependentContainer().equals(false);
        assert ((N2oPreFilter) page.getContainers().get(0).getPreFilters()[0]).getFieldId().equals("testField");
        assert ((N2oPreFilter) page.getContainers().get(0).getPreFilters()[0]).getValue().equals("name");
        assert ((N2oPreFilter) page.getContainers().get(0).getPreFilters()[0]).getType().equals(FilterType.eq);
        assert page.getN2oRegions()[0].getWidgets()[0].getCounter().getPreFilters()[0].getFieldId().equals("filterFiled");
        assert page.getN2oRegions()[0].getWidgets()[0].getCounter().getPreFilters()[0].getValue().equals("true");
        assert page.getContainers().get(0).getCounter().getPreFilters()[0].getFieldId().equals("filterFiled");
        assert page.getContainers().get(0).getCounter().getPreFilters()[0].getValue().equals("true");

    }
}
