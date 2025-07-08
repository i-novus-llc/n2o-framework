package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.ModalSizeEnum;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyModeEnum;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.clear.ClearAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModalPayload;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshPayload;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethodEnum;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции действия открытия модального окна
 */
class ShowModalCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(), new N2oAllDataPack(),
                new N2oActionsPack(), new N2oCellsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPageSecondFlow.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage3.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage4.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPageByParams.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyActionWithTwoWidgetPage.page.xml")
        );
    }

    @Test
    void create() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getRegions().get("single").get(0).getContent().get(0);
        ShowModalPayload payload = ((ShowModal) table.getToolbar().getButton("create").getAction()).getPayload();
        checkModalPayload(payload);

        PageContext modalContext = (PageContext) route("/p/create", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPage"));
        assertThat(modalContext.getDatasources().get(0).getId(),
                is("parent_second"));
        assertThat(((N2oStandardDatasource) modalContext.getDatasources().get(1)).getDefaultValuesMode(),
                is(DefaultValuesModeEnum.DEFAULTS));
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_create"));
        assertThat(modalPage.getBreadcrumb(), nullValue());

        assertThat(modalPage.getToolbar().getButton("submit"), notNullValue());
        assertThat(modalPage.getToolbar().getButton("close"), notNullValue());

        Widget modalWidget = modalPage.getWidget();
        assertThat(((StandardDatasource) modalPage.getDatasources().get(modalWidget.getDatasource())).getDefaultValuesMode(),
                is(DefaultValuesModeEnum.DEFAULTS));
        assertThat(((StandardDatasource) modalPage.getDatasources().get(modalWidget.getDatasource())).getProvider(), nullValue());

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());

        InvokeAction submit = (InvokeAction) ((MultiAction) modalPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/p/create/multi1"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethodEnum.POST));
        assertThat(submitPayload.getDatasource(), is("p_create_modal"));
        assertThat(submitPayload.getModel(), is(ReduxModelEnum.EDIT));
        RefreshAction refresh = (RefreshAction) ((MultiAction) modalPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_second"));
        CloseAction close = (CloseAction) ((MultiAction) modalPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getModalsToClose(), is(1));
        assertThat(((CloseActionPayload) close.getPayload()).getPrompt(), is(false));

        ActionContext submitContext = (ActionContext) route("/p/create/multi1", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("create"));
    }

    private void checkModalPayload(ShowModalPayload payload) {
        //create
        assertThat(payload.getPageUrl(), is("/p/create"));
        assertThat(payload.getSize(), is(ModalSizeEnum.SM));
        assertThat(payload.getScrollable(), is(true));
        assertThat(payload.getPageId(), is("p_create"));
        assertThat(payload.getPrompt(), is(false));

        assertThat(payload.getHasHeader(), is(false));
        assertThat(payload.getBackdrop(), is(true));
        assertThat(payload.getClassName(), is("n2o-custom-modal-dialog"));
        assertThat(payload.getStyle().get("background"), is("red"));
    }

    @Test
    void update() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getRegions().get("single").get(0).getContent().get(0);
        ShowModalPayload payload = ((ShowModal) table.getToolbar().getButton("update").getAction()).getPayload();

        //update
        assertThat(payload.getPageUrl(), is("/p/:id/update"));
        assertThat(payload.getSize(), is(ModalSizeEnum.LG));
        assertThat(payload.getPrompt(), is(false));
        assertThat(payload.getHasHeader(), is(true));
        assertThat(payload.getBackdrop(), is("static"));

        PageContext modalContext = (PageContext) route("/p/123/update", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPageSecondFlow"));
        assertThat(modalContext.getDatasources().get(0).getId(),
                is("parent_main"));
        assertThat(((N2oStandardDatasource) modalContext.getDatasources().get(1)).getDefaultValuesMode(),
                is(DefaultValuesModeEnum.QUERY));
        N2oPreFilter[] filters = ((N2oStandardDatasource) modalContext.getDatasources().get(1)).getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getDatasourceId(), is("main"));
        assertThat(filters[0].getRefPageId(), is("p"));
        assertThat(filters[0].getFieldId(), is(QuerySimpleField.PK));
        assertThat(filters[0].getType(), is(FilterTypeEnum.EQ));
        assertThat(filters[0].getModel(), is(ReduxModelEnum.RESOLVE));

        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_update"));
        assertThat(modalPage.getBreadcrumb(), nullValue());
        Widget modalWidget = modalPage.getWidget();
        StandardDatasource ds = (StandardDatasource) modalPage.getDatasources().get(modalWidget.getDatasource());
        assertThat(ds.getProvider().getQueryMapping().size(), is(0));
        assertThat(ds.getProvider().getPathMapping().get("id").getParam(), is("id"));
        assertThat(ds.getProvider().getPathMapping().get("id").normalizeLink(), is("models.resolve['p_main'].id"));
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesModeEnum.QUERY));
        DataSet data = new DataSet();
        data.put("id", 222);
        modalPage = (SimplePage) read().compile().bind().get(modalContext, data);
        ShowModal showModal = (ShowModal) modalPage.getWidget().getToolbar().getButton("p_update_mi0").getAction();
        assertThat(showModal.getPayload().getPageUrl(), is("/p/222/update/p_update_mi0"));
        assertThat(((StandardDatasource) modalPage.getDatasources().get(modalPage.getWidget().getDatasource())).getProvider().getUrl(), is("n2o/data/p/222/update/w1"));
    }

    @Test
    void createFocus() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        SimplePage showModal = (SimplePage) routeAndGet("/p/createFocus", Page.class);
        RefreshAction refresh = (RefreshAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        CloseAction close = (CloseAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getModalsToClose(), is(1));
        assertThat(((CloseActionPayload) close.getPayload()).getPrompt(), is(false));
        close = (CloseAction) showModal.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    void updateFocus() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        StandardPage showModal = (StandardPage) routeAndGet("/p/123/updateFocus", Page.class);
        RefreshAction refresh = (RefreshAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        CloseAction close = (CloseAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getModalsToClose(), is(1));
        assertThat(((CloseActionPayload) close.getPayload()).getPrompt(), is(false));
        close = (CloseAction) showModal.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
        Widget modalWidget = (Widget) showModal.getRegions().get("single").get(0).getContent().get(0);
        assertThat(((StandardDatasource) showModal.getDatasources().get(modalWidget.getDatasource())).getProvider().getPathMapping().size(), is(0));
        assertThat(((StandardDatasource) showModal.getDatasources().get(modalWidget.getDatasource())).getProvider().getQueryMapping().size(), is(0));
    }

    @Test
    void updateWithoutMasterDetail() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        StandardPage showModal = (StandardPage) routeAndGet("/p/123/updateByPathParams", Page.class);
        RefreshAction refresh = (RefreshAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        CloseAction close = (CloseAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getModalsToClose(), is(1));
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
        Widget modalWidget = (Widget) showModal.getRegions().get("single").get(0).getContent().get(0);
        assertThat(((StandardDatasource) showModal.getDatasources().get(modalWidget.getDatasource())).getProvider().getPathMapping().size(), is(1));
        assertThat(((StandardDatasource) showModal.getDatasources().get(modalWidget.getDatasource())).getProvider().getQueryMapping().size(), is(0));
    }

    @Test
    void createUpdate() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        SimplePage showModal = (SimplePage) routeAndGet("/p/createUpdate", Page.class);
        InvokeAction submit = (InvokeAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), is(1));
        assertThat(submit.getMeta().getSuccess().getRedirect().getPath(), is("/p/:id/update"));
        //Есть обновление, потому что по умолчанию true. Обновится родительский виджет, потому что close-after-submit=true
        RefreshAction refresh = (RefreshAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        //Есть уведомление, потому что по умолчанию true. Уведомление будет на родительском виджете, потому что close-after-submit=true

        CloseAction close = (CloseAction) ((MultiAction) showModal.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    void dynamicPage() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalDynamicPage.page.xml")
                .get(new PageContext("testShowModalDynamicPage", "/page"));
        PageContext context = (PageContext) route("/page/testOpenPageSimplePageAction1/id1", Page.class);
        DataSet data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction1");
        SimplePage showModal = (SimplePage) read().compile().bind().get(context, data);
        assertThat(showModal.getId(), is("page_id1"));
        assertThat(showModal.getWidget(), instanceOf(Form.class));

        context = (PageContext) route("/page/testOpenPageSimplePageAction2/id1", Page.class);
        data = new DataSet();
        data.put("page_test_id", "testOpenPageSimplePageAction2");
        showModal = (SimplePage) read().compile().bind().get(context, data);
        assertThat(showModal.getId(), is("page_id1"));
        assertThat(showModal.getWidget(), instanceOf(Form.class));
    }

    @Test
    void updateWithPreFilters() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        PageContext modalContext = (PageContext) route("/p/123/updateWithPrefilters", Page.class);
        checkModalContextAndFilters(modalContext);

        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_updateWithPrefilters"));
        assertThat(modalPage.getBreadcrumb(), nullValue());
        Widget modalWidget = modalPage.getWidget();
        StandardDatasource ds = (StandardDatasource) modalPage.getDatasources().get(modalWidget.getDatasource());
        assertThat(ds.getProvider().getPathMapping().get("id").getLink(), is("models.resolve['p_main']"));
        assertThat(ds.getProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(ds.getProvider().getQueryMapping().get("name").getLink(), is("models.filter['p_second']"));
        assertThat(ds.getProvider().getQueryMapping().get("name").getValue(), is("`name`"));
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesModeEnum.QUERY));
        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());
        InvokeAction submit = (InvokeAction) ((MultiAction) modalPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/:id/updateWithPrefilters/multi1"));
        ActionContext submitContext = (ActionContext) route("/p/:id/updateWithPrefilters/multi1", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("update"));
        assertThat(submitContext.getOperationId(), is("update"));
        RefreshAction refresh = (RefreshAction) ((MultiAction) modalPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(1);
        assertThat(((RefreshPayload) refresh.getPayload()).getDatasource(), is("p_main"));
        CloseAction close = (CloseAction) ((MultiAction) modalPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(2);
        assertThat(close.getMeta().getModalsToClose(), is(1));
        assertThat(((CloseActionPayload) close.getPayload()).getPrompt(), is(false));

        DataSet data = new DataSet();
        data.put("id", 222);
        modalPage = (SimplePage) read().compile().bind().get(modalContext, data);
        assertThat(((StandardDatasource) modalPage.getDatasources().get(modalPage.getWidget().getDatasource())).getProvider().getUrl(), is("n2o/data/p/222/updateWithPrefilters/modal"));
        submit = (InvokeAction) ((MultiAction) modalPage.getToolbar().getButton("submit").getAction()).getPayload().getActions().get(0);
        assertThat(submit.getPayload().getDataProvider().getPathMapping(), not(hasKey("p_main_id")));
    }

    private void checkModalContextAndFilters(PageContext modalContext) {
        assertThat(modalContext.getSourceId(null), is("testShowModalPage"));
        assertThat(modalContext.getDatasources().size(), is(2));
        assertThat(modalContext.getDatasources().get(0).getId(), is("parent_main"));

        N2oStandardDatasource datasource = (N2oStandardDatasource) modalContext.getDatasources().get(1);
        assertThat(datasource.getDefaultValuesMode(), is(DefaultValuesModeEnum.QUERY));
        assertThat(datasource.getFilters().length, is(1));
        N2oPreFilter filter = datasource.getFilters()[0];
        assertThat(filter.getDatasourceId(), is("main"));
        assertThat(filter.getRefPageId(), is("p"));
        assertThat(filter.getFieldId(), is(QuerySimpleField.PK));
        assertThat(filter.getType(), is(FilterTypeEnum.EQ));
        assertThat(filter.getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(filter.getValue(), is("{id}"));
    }

    @Test
    void updateModelEditWithPreFilters() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        ShowModal showModal = (ShowModal) ((Widget) rootPage.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().getButton("updateEditWithPrefilters").getAction();
        assertThat(showModal.getPayload().getQueryMapping().get("id").getLink(), is("models.edit['p_main']"));

        Page showModalPage = routeAndGet("/p/updateEditWithPrefilters", Page.class);
        assertThat(showModalPage.getId(), is("p_updateEditWithPrefilters"));
    }

    @Test
    void copyAction() {
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyAction.page.xml")
                .get(new PageContext("testShowModalCopyAction"));

        PageContext modalContext = (PageContext) route("/testShowModalCopyAction/123/update", Page.class);
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);

        CopyAction submit = (CopyAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getType(), is("n2o/models/COPY"));
        assertThat(submit.getPayload().getSource().getPrefix(), is(ReduxModelEnum.RESOLVE.getId()));
        assertThat(submit.getPayload().getSource().getKey(), is("testShowModalCopyAction_update_modal"));
        assertThat(submit.getPayload().getSource().getField(), nullValue());
        assertThat(submit.getPayload().getTarget().getPrefix(), is(ReduxModelEnum.EDIT.getId()));
        assertThat(submit.getPayload().getTarget().getKey(), is("testShowModalCopyAction_table1"));
        assertThat(submit.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(submit.getPayload().getMode(), is(CopyModeEnum.REPLACE));
        assertThat(submit.getMeta().getModalsToClose(), is(1));

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), is(submit));
    }

    @Test
    void copyActionWithTwoWidget() {
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyActionWithTwoWidget.page.xml")
                .get(new PageContext("testShowModalCopyActionWithTwoWidget"));

        PageContext modalContext = (PageContext) route("/testShowModalCopyActionWithTwoWidget/123/update", Page.class);
        StandardPage modalPage = (StandardPage) read().compile().get(modalContext);

        CopyAction submit = (CopyAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getType(), is("n2o/models/COPY"));
        assertThat(submit.getPayload().getSource().getPrefix(), is(ReduxModelEnum.MULTI.getId()));
        assertThat(submit.getPayload().getSource().getKey(), is("testShowModalCopyActionWithTwoWidget_update_table2"));
        assertThat(submit.getPayload().getSource().getField(), is("id"));
        assertThat(submit.getPayload().getTarget().getPrefix(), is(ReduxModelEnum.MULTI.getId()));
        assertThat(submit.getPayload().getTarget().getKey(), is("testShowModalCopyActionWithTwoWidget_table1"));
        assertThat(submit.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(submit.getPayload().getMode(), is(CopyModeEnum.REPLACE));
        assertThat(submit.getMeta().getModalsToClose(), is(1));

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), is(submit));
    }

    @Test
    void copyActionWithTwoWidgetWithoutCopyAttributes() {
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyActionWithTwoWidgetDefault.page.xml")
                .get(new PageContext("testShowModalCopyActionWithTwoWidgetDefault"));

        PageContext modalContext = (PageContext) route("/testShowModalCopyActionWithTwoWidgetDefault/123/update", Page.class);
        StandardPage modalPage = (StandardPage) read().compile().get(modalContext);

        CopyAction submit = (CopyAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getType(), is("n2o/models/COPY"));
        assertThat(submit.getPayload().getSource().getPrefix(), is(ReduxModelEnum.RESOLVE.getId()));
        assertThat(submit.getPayload().getSource().getKey(), is("testShowModalCopyActionWithTwoWidgetDefault_update_master"));
        assertThat(submit.getPayload().getSource().getField(), nullValue());
        assertThat(submit.getPayload().getTarget().getPrefix(), is(ReduxModelEnum.EDIT.getId()));
        assertThat(submit.getPayload().getTarget().getKey(), is("testShowModalCopyActionWithTwoWidgetDefault_ignore_table"));
        assertThat(submit.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(submit.getPayload().getMode(), is(CopyModeEnum.REPLACE));
        assertThat(submit.getMeta().getModalsToClose(), is(1));

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), is(submit));
        assertThat(buttons.get(0).getLabel(), is("Выбрать"));
    }

    @Test
    void testShowModalOnClose() {
        PageContext pageContext = new PageContext("testShowModalOnClose", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalOnClose.page.xml")
                .get(pageContext);

        MetaSaga meta = ((ShowModal) ((Form) rootPage.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().getButton("modal1").getAction()).getMeta();
        assertThat(meta.getOnClose(), notNullValue());
        assertThat(meta.getOnClose().getRefresh().getDatasources(), hasItem("p_form"));

        meta = ((ShowModal) ((Form) rootPage.getRegions().get("single").get(0).getContent().get(1))
                .getToolbar().getButton("modal2").getAction()).getMeta();
        assertThat(meta.getOnClose(), notNullValue());
        assertThat(meta.getOnClose().getRefresh().getDatasources(), hasItem("p_form"));
    }

    /**
     * Проверяет, что show-modal upload=defaults пробрасывается на модальную страницу и убирает создание dataProvider у datasource
     */
    @Test
    void createUploadDefaults() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalDefaults.page.xml")
                .get(new PageContext("testShowModalDefaults", "/p"));
        PageContext modalCtx = (PageContext) route("/p/update", Page.class);
        assertThat(modalCtx.getDatasources().size(), is(1));
        assertThat(((N2oStandardDatasource) modalCtx.getDatasources().get(0)).getDefaultValuesMode(), is(DefaultValuesModeEnum.DEFAULTS));
        SimplePage modalPage = (SimplePage) routeAndGet("/p/update", Page.class);
        StandardDatasource datasource = (StandardDatasource) modalPage.getDatasources().get("p_update_modal");
        assertThat(datasource.getProvider(), nullValue());

        modalCtx = (PageContext) route("/p/update2", Page.class);
        assertThat(modalCtx.getDatasources().size(), is(1));
        assertThat(((N2oStandardDatasource) modalCtx.getDatasources().get(0)).getDefaultValuesMode(), is(DefaultValuesModeEnum.DEFAULTS));
        StandardPage modalPage2 = (StandardPage) routeAndGet("/p/update2", Page.class);
        StandardDatasource datasource1 = (StandardDatasource) modalPage2.getDatasources().get("p_update2_main");
        assertThat(datasource1.getProvider(), nullValue());
    }

    /**
     * Проверяет, что show-modal pre-filters пробрасываются на модальную страницу
     */
    @Test
    void testShowModalPreFilters() {
        PageContext pageContext = new PageContext("testShowModalPreFilters", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalPreFilters.page.xml")
                .get(pageContext);

        SimplePage page = (SimplePage) routeAndGet("/p/create", Page.class);
        ClientDataProvider provider = ((StandardDatasource) page.getDatasources().get("p_create_modal")).getProvider();
        assertThat(provider, notNullValue());
        assertThat(provider.getQueryMapping().get("modal_id").normalizeLink(), is("models.resolve['p_form'].id"));
        assertThat(provider.getQueryMapping().get("modal_name").getValue(), is(123));
    }

    /**
     * Проверяет переопределение toolbar и action
     */
    @Test
    void testShowModalToolbarAndActions() {
        PageContext pageContext = new PageContext("testShowModalToolbarAndAction", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalToolbarAndAction.page.xml")
                .get(pageContext);

        StandardPage page = (StandardPage) routeAndGet("/p/create", Page.class);
        List<Group> toolbar = page.getToolbar().get("bottomCenter");
        assertThat(toolbar.get(0).getButtons().size(), is(1));
        assertThat(toolbar.get(0).getButtons().get(0).getLabel(), is("Button in center"));
        assertThat(toolbar.get(0).getButtons().get(0).getAction(), instanceOf(LinkAction.class));
        assertThat(((LinkAction) toolbar.get(0).getButtons().get(0).getAction()).getUrl(), is("http://i-novus.ru"));

        page = (StandardPage) routeAndGet("/p/update", Page.class);
        toolbar = page.getToolbar().get("bottomCenter");
        assertThat(toolbar.get(0).getButtons().size(), is(1));
        assertThat(toolbar.get(0).getButtons().get(0).getLabel(), is("Button in center"));
        assertThat(toolbar.get(0).getButtons().get(0).getAction(), instanceOf(LinkAction.class));
        assertThat(((LinkAction) toolbar.get(0).getButtons().get(0).getAction()).getUrl(), is("http://i-novus.ru"));
        toolbar = page.getToolbar().get("bottomRight");
        assertThat(toolbar.get(0).getButtons().size(), is(1));
        assertThat(toolbar.get(0).getButtons().get(0).getLabel(), is("Button2"));
        assertThat(toolbar.get(0).getButtons().get(0).getAction(), instanceOf(ClearAction.class));
    }
}
