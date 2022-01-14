package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModalPayload;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции действия открытия модального окна
 */
public class ShowModalCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(), new N2oAllDataPack(),
                new N2oActionsPack(), new N2oCellsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPageSecondFlow.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPage3.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalPageByParams.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageSimplePageAction2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyActionWithTwoWidgetPage.page.xml")
        );
    }

    @Test
    public void create() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getRegions().get("left").get(0).getContent().get(0);
        ShowModalPayload payload = ((ShowModal) table.getToolbar().getButton("create").getAction()).getPayload();
        //create
        assertThat(payload.getPageUrl(), is("/p/create"));
        assertThat(payload.getSize(), is("sm"));
        assertThat(payload.getScrollable(), is(true));
        assertThat(payload.getPageId(), is("p_create"));
        assertThat(payload.getPrompt(), is(true));

        assertThat(payload.getHasHeader(), is(false));
        assertThat(payload.getBackdrop(), is(true));
        assertThat(payload.getClassName(), is("n2o-custom-modal-dialog"));
        assertThat(payload.getStyle().get("background"), is("red"));

        PageContext modalContext = (PageContext) route("/p/create", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPage"));
        assertThat(modalContext.getDatasources().get(0).getDefaultValuesMode(), is(DefaultValuesMode.defaults));
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_create"));
        assertThat(modalPage.getBreadcrumb(), nullValue());

        assertThat(modalPage.getToolbar().getButton("submit"), notNullValue());
        assertThat(modalPage.getToolbar().getButton("close"), notNullValue());

        Widget modalWidget = modalPage.getWidget();
        assertThat(modalPage.getDatasources().get(modalWidget.getDatasource()).getDefaultValuesMode(), is(DefaultValuesMode.defaults));
        assertThat(modalPage.getDatasources().get(modalWidget.getDatasource()).getProvider(), nullValue());

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());

        InvokeAction submit = (InvokeAction) modalPage.getToolbar().getButton("submit").getAction();
        InvokeActionPayload submitPayload = submit.getPayload();
        assertThat(submitPayload.getDataProvider().getUrl(), is("n2o/data/p/create/submit"));
        assertThat(submitPayload.getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(submitPayload.getDatasource(), is("p_create_modal"));
        assertThat(submitPayload.getModel(), is(ReduxModel.edit));
        AsyncMetaSaga meta = submit.getMeta();
        assertThat(meta.getSuccess().getRefresh().getDatasources(), hasItem("p_second"));
        assertThat(meta.getSuccess().getModalsToClose(), is(1));
        assertThat(meta.getFail().getMessageWidgetId(), is("p_create_modal"));
        assertThat(meta.getSuccess().getMessageWidgetId(), is("p_main"));
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/create/submit"));

        ActionContext submitContext = (ActionContext) route("/p/create/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("create"));
    }

    @Test
    public void update() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        Table table = (Table) rootPage.getRegions().get("left").get(0).getContent().get(0);
        ShowModalPayload payload = ((ShowModal) table.getToolbar().getButton("update").getAction()).getPayload();

        //update
        assertThat(payload.getPageUrl(), is("/p/:id/update"));
        assertThat(payload.getSize(), is("lg"));
        assertThat(payload.getPrompt(), is(false));
        assertThat(payload.getHasHeader(), is(true));
        assertThat(payload.getBackdrop(), is("static"));

        PageContext modalContext = (PageContext) route("/p/123/update", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPageSecondFlow"));
        assertThat(modalContext.getDatasources().get(0).getDefaultValuesMode(), is(DefaultValuesMode.query));
        N2oPreFilter[] filters = modalContext.getDatasources().get(0).getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getDatasource(), is("main"));
        assertThat(filters[0].getRefPageId(), is("p"));
        assertThat(filters[0].getFieldId(), is(N2oQuery.Field.PK));
        assertThat(filters[0].getType(), is(FilterType.eq));
        assertThat(filters[0].getModel(), is(ReduxModel.resolve));

        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_update"));
        assertThat(modalPage.getBreadcrumb(), nullValue());
        Widget modalWidget = modalPage.getWidget();
        Datasource ds = modalPage.getDatasources().get(modalWidget.getDatasource());
        assertThat(ds.getProvider().getQueryMapping().size(), is(0));
        assertThat(ds.getProvider().getPathMapping().get("id").getParam(), is("id"));
        assertThat(ds.getProvider().getPathMapping().get("id").normalizeLink(), is("models.resolve['p_main'].id"));
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesMode.query));
        DataSet data = new DataSet();
        data.put("id", 222);
        modalPage = (SimplePage) read().compile().bind().get(modalContext, data);
        ShowModal showModal = (ShowModal) modalPage.getWidget().getToolbar().getButton("mi0").getAction();
        assertThat(showModal.getPayload().getPageUrl(), is("/p/222/update/mi0"));
        assertThat(modalPage.getDatasources().get(modalPage.getWidget().getDatasource()).getProvider().getUrl(), is("n2o/data/p/222/update/main"));

        QueryContext queryContext = (QueryContext) route("/p/123/update", CompiledQuery.class);
        assertThat(queryContext.getValidations().size(), is(1));
    }

    @Test
    public void createFocus() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        SimplePage showModal = (SimplePage) routeAndGet("/p/createFocus", Page.class);
        InvokeAction submit = (InvokeAction) showModal.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), is(1));
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));

        CloseAction close = (CloseAction) showModal.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    public void updateFocus() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        StandardPage showModal = (StandardPage) routeAndGet("/p/123/updateFocus", Page.class);
        InvokeAction submit = (InvokeAction) showModal.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), is(1));
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));

        CloseAction close = (CloseAction) showModal.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
        Widget modalWidget = (Widget) showModal.getRegions().get("left").get(0).getContent().get(0);
        assertThat(showModal.getDatasources().get(modalWidget.getDatasource()).getProvider().getPathMapping().size(), is(0));
        assertThat(showModal.getDatasources().get(modalWidget.getDatasource()).getProvider().getQueryMapping().size(), is(0));
    }

    @Test
    public void updateWithoutMasterDetail() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        StandardPage showModal = (StandardPage) routeAndGet("/p/123/updateByPathParams", Page.class);
        InvokeAction submit = (InvokeAction) showModal.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), is(1));
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));

        CloseAction close = (CloseAction) showModal.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
        Widget modalWidget = (Widget) showModal.getRegions().get("left").get(0).getContent().get(0);
        assertThat(showModal.getDatasources().get(modalWidget.getDatasource()).getProvider().getPathMapping().size(), is(1));
        assertThat(showModal.getDatasources().get(modalWidget.getDatasource()).getProvider().getQueryMapping().size(), is(0));
    }

    @Test
    public void createUpdate() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        SimplePage showModal = (SimplePage) routeAndGet("/p/createUpdate", Page.class);
        InvokeAction submit = (InvokeAction) showModal.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), is(1));
        assertThat(submit.getMeta().getSuccess().getRedirect().getPath(), is("/p/:id/update"));
        //Есть обновление, потому что по умолчанию true. Обновится родительский виджет, потому что close-after-submit=true
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));
        //Есть уведомление, потому что по умолчанию true. Уведомление будет на родительском виджете, потому что close-after-submit=true

        CloseAction close = (CloseAction) showModal.getToolbar().getButton("close").getAction();
        assertThat(close.getMeta().getRedirect(), nullValue());
        assertThat(close.getMeta().getRefresh(), nullValue());
    }

    @Test
    public void dynamicPage() {
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
    public void updateWithPreFilters() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);

        PageContext modalContext = (PageContext) route("/p/123/updateWithPrefilters", Page.class);
        assertThat(modalContext.getSourceId(null), is("testShowModalPage"));
        assertThat(modalContext.getDatasources().size(), is(1));
        assertThat(modalContext.getDatasources().get(0).getDefaultValuesMode(), is(DefaultValuesMode.query));
        N2oPreFilter[] filters = modalContext.getDatasources().get(0).getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getDatasource(), is("main"));
        assertThat(filters[0].getRefPageId(), is("p"));
        assertThat(filters[0].getFieldId(), is(N2oQuery.Field.PK));
        assertThat(filters[0].getType(), is(FilterType.eq));
        assertThat(filters[0].getModel(), is(ReduxModel.resolve));
        assertThat(filters[0].getValue(), is("{id}"));

        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        assertThat(modalPage.getId(), is("p_updateWithPrefilters"));
        assertThat(modalPage.getBreadcrumb(), nullValue());
        Widget modalWidget = modalPage.getWidget();
        Datasource ds = modalPage.getDatasources().get(modalWidget.getDatasource());
        assertThat(ds.getProvider().getPathMapping().get("id").getBindLink(), is("models.resolve['p_main']"));
        assertThat(ds.getProvider().getPathMapping().get("id").getValue(), is("`id`"));
        assertThat(ds.getProvider().getQueryMapping().get("name").getBindLink(), is("models.filter['p_second']"));
        assertThat(ds.getProvider().getQueryMapping().get("name").getValue(), is("`name`"));
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesMode.query));
        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.size(), is(2));
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), notNullValue());
        assertThat(buttons.get(1).getId(), is("close"));
        assertThat(buttons.get(1).getAction(), notNullValue());
        InvokeAction submit = (InvokeAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("p_main"));
        assertThat(submit.getMeta().getSuccess().getModalsToClose(), is(1));
        assertThat(submit.getPayload().getDataProvider().getUrl(), is("n2o/data/p/:id/updateWithPrefilters/submit"));
        ActionContext submitContext = (ActionContext) route("/p/:id/updateWithPrefilters/submit", CompiledObject.class);
        assertThat(submitContext.getSourceId(null), is("testShowModal"));
        assertThat(submitContext.getOperationId(), is("update"));
        assertThat(submitContext.getOperationId(), is("update"));

        DataSet data = new DataSet();
        data.put("id", 222);
        modalPage = (SimplePage) read().compile().bind().get(modalContext, data);
        assertThat(modalPage.getDatasources().get(modalPage.getWidget().getDatasource()).getProvider().getUrl(), is("n2o/data/p/222/updateWithPrefilters/modal"));
        submit = (InvokeAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getDataProvider().getPathMapping(), not(hasKey("p_main_id")));
    }

    @Test
    public void updateModelEditWithPreFilters() {
        PageContext pageContext = new PageContext("testShowModalRootPage", "/p");
        StandardPage rootPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalRootPage.page.xml")
                .get(pageContext);
        ShowModal showModal = (ShowModal) ((Widget) rootPage.getRegions().get("left").get(0).getContent().get(0))
                .getToolbar().getButton("updateEditWithPrefilters").getAction();
        assertThat(showModal.getPayload().getQueryMapping().get("id").getBindLink(), is("models.edit['p_main']"));

        Page showModalPage = routeAndGet("/p/updateEditWithPrefilters", Page.class);
        assertThat(showModalPage.getId(), is("p_updateEditWithPrefilters"));
    }

    @Test
    public void copyAction() {
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyAction.page.xml")
                .get(new PageContext("testShowModalCopyAction"));

        PageContext modalContext = (PageContext) route("/testShowModalCopyAction/123/update", Page.class);
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);

        CopyAction submit = (CopyAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getType(), is("n2o/models/COPY"));
        assertThat(submit.getPayload().getSource().getPrefix(), is(ReduxModel.resolve.getId()));
        assertThat(submit.getPayload().getSource().getKey(), is("testShowModalCopyAction_update_modal"));
        assertThat(submit.getPayload().getSource().getField(), nullValue());
        assertThat(submit.getPayload().getTarget().getPrefix(), is(ReduxModel.edit.getId()));
        assertThat(submit.getPayload().getTarget().getKey(), is("testShowModalCopyAction_table1"));
        assertThat(submit.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(submit.getPayload().getMode(), is(CopyMode.replace));
        assertThat(submit.getMeta().getModalsToClose(), is(1));

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), is(submit));
    }

    @Test
    public void copyActionWithTwoWidget() {
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyActionWithTwoWidget.page.xml")
                .get(new PageContext("testShowModalCopyActionWithTwoWidget"));

        PageContext modalContext = (PageContext) route("/testShowModalCopyActionWithTwoWidget/123/update", Page.class);
        StandardPage modalPage = (StandardPage) read().compile().get(modalContext);

        CopyAction submit = (CopyAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getType(), is("n2o/models/COPY"));
        assertThat(submit.getPayload().getSource().getPrefix(), is(ReduxModel.multi.getId()));
        assertThat(submit.getPayload().getSource().getKey(), is("testShowModalCopyActionWithTwoWidget_update_table2"));
        assertThat(submit.getPayload().getSource().getField(), is("id"));
        assertThat(submit.getPayload().getTarget().getPrefix(), is(ReduxModel.multi.getId()));
        assertThat(submit.getPayload().getTarget().getKey(), is("testShowModalCopyActionWithTwoWidget_table1"));
        assertThat(submit.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(submit.getPayload().getMode(), is(CopyMode.replace));
        assertThat(submit.getMeta().getModalsToClose(), is(1));

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), is(submit));
    }

    @Test
    public void copyActionWithTwoWidgetWithoutCopyAttributes() {
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalCopyActionWithTwoWidgetDefault.page.xml")
                .get(new PageContext("testShowModalCopyActionWithTwoWidgetDefault"));

        PageContext modalContext = (PageContext) route("/testShowModalCopyActionWithTwoWidgetDefault/123/update", Page.class);
        StandardPage modalPage = (StandardPage) read().compile().get(modalContext);

        CopyAction submit = (CopyAction) modalPage.getToolbar().getButton("submit").getAction();
        assertThat(submit.getType(), is("n2o/models/COPY"));
        assertThat(submit.getPayload().getSource().getPrefix(), is(ReduxModel.resolve.getId()));
        assertThat(submit.getPayload().getSource().getKey(), is("testShowModalCopyActionWithTwoWidgetDefault_update_master"));
        assertThat(submit.getPayload().getSource().getField(), nullValue());
        assertThat(submit.getPayload().getTarget().getPrefix(), is(ReduxModel.edit.getId()));
        assertThat(submit.getPayload().getTarget().getKey(), is("testShowModalCopyActionWithTwoWidgetDefault_ignore_table"));
        assertThat(submit.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(submit.getPayload().getMode(), is(CopyMode.replace));
        assertThat(submit.getMeta().getModalsToClose(), is(1));

        List<AbstractButton> buttons = modalPage.getToolbar().get("bottomRight").get(0).getButtons();
        assertThat(buttons.get(0).getId(), is("submit"));
        assertThat(buttons.get(0).getAction(), is(submit));
        assertThat(buttons.get(0).getLabel(), is("Выбрать"));
    }

    @Test
    public void testShowModalOnClose() {
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
    public void createUploadDefaults() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalDefaults.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testShowModalOnClose.page.xml")
                .get(new PageContext("testShowModalDefaults", "/p"));
        PageContext modalCtx = (PageContext) route("/p/create", Page.class);
        assertThat(modalCtx.getDatasources().size(), is(1));
        assertThat(modalCtx.getDatasources().get(0).getDefaultValuesMode(), is(DefaultValuesMode.defaults));
        SimplePage createPage = (SimplePage) routeAndGet("/p/create", Page.class);
        Datasource datasource = createPage.getDatasources().get("p_create_modal");
        assertThat(datasource.getProvider(), nullValue());
    }

    /**
     * Проверяет, что show-modal pre-filters пробрасываются на модальную страницу
     */
    @Test
    public void testShowModalPreFilters() {
        PageContext pageContext = new PageContext("testShowModalPreFilters", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/testShowModalPreFilters.page.xml")
                .get(pageContext);

        SimplePage page = (SimplePage) routeAndGet("/p/create", Page.class);
        ClientDataProvider provider = page.getDatasources().get("p_create_modal").getProvider();
        assertThat(provider, notNullValue());
        assertThat(provider.getQueryMapping().get("modal_id").normalizeLink(), is("models.resolve['p_form'].id"));
        assertThat(provider.getQueryMapping().get("modal_name").getValue(), is(123));
    }
}