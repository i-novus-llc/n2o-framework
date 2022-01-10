package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.interval.N2oSimpleIntervalField;
import net.n2oapp.framework.api.metadata.control.list.N2oSingleListFieldAbstract;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.N2oShowModalForm;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;

import java.util.Map;

/**
 * Тест чтения контролов
 */
public abstract class N2oStandardControlReaderTestBase {

    protected void assertCountField(N2oFieldSet fieldSet, int expectedCount) {
        assert fieldSet.getItems().length == expectedCount;
    }

    protected void assertStandardAttribute(N2oField field) {
        assertBaseAttributes(field);
        assert field.getDescription().equals("test");
        assertValidations(field);
        assertActions(field);
        assertConditions(field);
    }

    protected void assertBaseAttributes(N2oField field) {
        String test = "test";
        assert field.getId().equals("id");
        assert field.getLabel().equals(test);
        if (field instanceof N2oStandardField) {
            assert !((N2oStandardField) field).getCopied();
        }
        assert field.getDomain().equals(test);
        assert field.getDependsOn()[0].equals(test);
        assert field.getCssClass().equals(test);
        assert field.getLabelStyle().equals(test);
        assert field.getDependencies()[0].getValue().equals(test);
    }

    protected void assertStandardListAttribute(N2oListField field) {
        assertStandardAttribute(field);
        assertQuery(field);
        assertOption(field);
        assertDefault(field);
    }

    protected void assertStandardSingleListAttribute(N2oSingleListFieldAbstract field, boolean isQueryPresent) {
        assertStandardAttribute(field);
        if (isQueryPresent) assertQuery(field);

        assertDefault(field);
    }

    protected void assertStandardIntervalAttribute(N2oSimpleIntervalField field) {
        assertStandardAttribute(field);
        assert field.getBegin().equals("test");
        assert field.getEnd().equals("test");
    }

    private void assertQuery(N2oListField field) {
        assert field.getQueryId().equals("query");
        assert field.getDetailFieldId().equals("id");
        assert field.getLabelFieldId().equals("id");
        assert field.getMasterFieldId().equals("id");
        assert field.getSearchFilterId().equals("id");
        assert field.getValueFieldId().equals("id");
        assert field.getPreFilters().length == 1;
        assertPreFilter(field.getPreFilters()[0], true);
    }

    private void assertOption(N2oListField field) {
        Object[] options = field.getOptions();
//        assert options.size() == 3;
//        for (int i = 0; i < options.size(); i++)
//            assert options.get(i).toString().contains(String.valueOf(i+1));
    }

    private void assertValidations(N2oField field) {
//        assert field.getValidationReferences().get("key").getSide().toLowerCase().equals("client");
    }

    private void assertDefault(N2oListField field) {
        assert field.getCache().equals(false);
        Map<String, String> defaultModel = field.getDefValue();
        assert defaultModel.get("key").equals("value");
    }

    private void assertActions(N2oField field) {
        //todo action button  теперь считывается toolbar
        /*List<List> links = Arrays.asList(field.getActionButtons());
        for (List<N2oControlActionLink> link : links) {
            assert link.size() == 5;
            for (int i = 0; i < link.size(); i++) {
                N2oControlActionLink testLink = link.get(i);
                assert testLink.getLabel().equals("test");
                if (testLink.getEvent() instanceof N2oAnchor) {
                    N2oAnchor anchor = (N2oAnchor) link.get(i).getEvent();
                    assert anchor.getHref().equals("test");
                    assert anchor.getTarget().name().equals("newWindow");
                } else if (testLink.getEvent() instanceof N2oOpenPage)
                    assertShowModal((N2oAbstractPageAction) link.get(i).getEvent());
                else if (testLink.getEvent() instanceof N2oShowModal)
                    assertShowModal((N2oAbstractPageAction) link.get(i).getEvent());
                else if (testLink.getEvent() instanceof N2oShowModalForm)
                    assertShowModalForm((N2oShowModalForm) link.get(i).getEvent());
                else if (testLink.getEvent() instanceof UpdateModelAction)
                    assertUpdateModel((UpdateModelAction) testLink.getEvent());
                else if (testLink.getEvent() instanceof OnClick) {
                    assert ((OnClick) testLink.getEvent()).getFunctionName().equals("test");
                    assert ((OnClick) testLink.getEvent()).getSourceSrc().equals("test");
                } else if (testLink.getEvent() instanceof SetValueExpressionAction) {
                    assert ((SetValueExpressionAction) testLink.getEvent()).getExpression().equals("value");
                } else Assert.fail(String.valueOf(i));
            }
        }*/
    }

    private void assertShowModal(N2oAbstractPageAction showModal) {
        assert showModal.getPageId().equals("test");
        assert showModal.getOperationId().toLowerCase().equals("create");
        assert showModal.getContainerId().equals("test");
        assert showModal.getDetailFieldId().equals("id");
        assert showModal.getMasterFieldId().equals("id");
        assert showModal.getMaxWidth().equals("100");
        assert showModal.getMinWidth().equals("100");
        assert showModal.getPageName().equals("test");
        assert !showModal.getRefreshOnClose();
        assert showModal.getTargetFieldId().equals("id");
        assert showModal.getValueFieldId().equals("id");
        assert showModal.getWidth().equals("100");
        assert showModal.getPreFilters().length == 1;
        assertPreFilter(showModal.getPreFilters()[0], true);

    }

    private void assertShowModalForm(N2oShowModalForm showModal) {
        assert showModal.getFormId().equals("test");
        assert showModal.getOperationId().toLowerCase().equals("create");
        assert showModal.getContainerId().equals("test");
        assert showModal.getDetailFieldId().equals("id");
        assert showModal.getMasterFieldId().equals("id");
        assert showModal.getMaxWidth().equals("100");
        assert showModal.getMinWidth().equals("100");
        assert showModal.getPageName().equals("test");
        assert !showModal.getRefreshOnClose();
        assert showModal.getTargetFieldId().equals("id");
        assert showModal.getValueFieldId().equals("id");
        assert showModal.getWidth().equals("100");
        assert showModal.getPreFilters().length == 1;
        assertPreFilter(showModal.getPreFilters()[0], true);
    }

    private void assertConditions(N2oField field) {
        assert field.getDependencies()[0].getOn()[0].equals("test");
        assert field.getDependencies()[0].getValue().equals("test");
        assert field.getDependencies()[1].getOn()[0].equals("id");
        assert field.getDependencies()[1].getValue().equals("test");
        assert field.getDependencies()[2].getOn()[0].equals("id");
        assert field.getDependencies()[2].getValue().equals("test");
    }

    protected void assertPreFilter(N2oPreFilter preFilter, boolean isRefPresent) {
        assert preFilter.getFieldId().equals("id");
        if (isRefPresent) {
            assert preFilter.getValue().equals("{test}");
        } else {
            assert preFilter.getValue().equals("test");
        }
        assert preFilter.getType().name().equals("eq");
        assert preFilter.getValues()[0].equals("value");
    }
}
