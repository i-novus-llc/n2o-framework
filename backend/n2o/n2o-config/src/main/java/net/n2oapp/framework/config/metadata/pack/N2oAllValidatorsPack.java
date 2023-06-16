package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationValidator;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarValidator;
import net.n2oapp.framework.config.metadata.validation.standard.action.*;
import net.n2oapp.framework.config.metadata.validation.standard.control.MarkdownValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.InheritedDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.event.OnChangeEventValidator;
import net.n2oapp.framework.config.metadata.validation.standard.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.ButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.ApplicationDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.StandardDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.*;
import net.n2oapp.framework.config.metadata.validation.standard.invocation.JavaDataProviderValidator;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.*;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.metadata.validation.standard.regions.ScrollspyValidator;
import net.n2oapp.framework.config.metadata.validation.standard.regions.TabsValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.*;

/**
 * Набор стандартных валидаторов метаданных
 */
public class N2oAllValidatorsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.validators(new ObjectValidator(), new QueryValidator(), new PageValidator(),
                new ApplicationValidator(), new SimpleMenuValidator(), new SidebarValidator(),
                new WidgetValidator(), new ListFieldValidator(), new SetFieldSetValidator(),
                new FieldSetColumnValidator(), new FieldSetRowValidator(), new FormValidator(),
                new TableValidator(), new PageActionValidator(), new InvokeActionValidator(), new StandardPageValidator(),
                new LeftRightPageValidator(), new SimplePageValidator(), new BasePageValidator(), new SearchablePageValidator(),
                new StandardDatasourceValidator(), new ApplicationDatasourceValidator(), new InheritedDatasourceValidator(),
                new TopLeftRightPageValidator(), new FieldValidator(), new LineFieldSetValidator(), new MultiFieldSetValidator(),
                new JavaDataProviderValidator(), new ButtonValidator(), new SubmitActionValidator(),
                new CustomActionValidator(), new ActionsAwareValidator(), new SwitchActionValidator(),
                new EditListActionValidator(), new OnChangeEventValidator(), new MarkdownValidator(),
                new ListWidgetValidator(), new TabsValidator(), new ScrollspyValidator());
    }
}
