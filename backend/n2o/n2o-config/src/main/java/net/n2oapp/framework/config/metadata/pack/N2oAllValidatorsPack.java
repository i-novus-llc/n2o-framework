package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.validation.standard.action.*;
import net.n2oapp.framework.config.metadata.validation.standard.application.ApplicationValidator;
import net.n2oapp.framework.config.metadata.validation.standard.application.SidebarValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.BaseButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.ButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.ClipboardButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.SubMenuValidator;
import net.n2oapp.framework.config.metadata.validation.standard.cell.*;
import net.n2oapp.framework.config.metadata.validation.standard.control.*;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.*;
import net.n2oapp.framework.config.metadata.validation.standard.event.OnChangeEventValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.*;
import net.n2oapp.framework.config.metadata.validation.standard.invocation.JavaDataProviderValidator;
import net.n2oapp.framework.config.metadata.validation.standard.menu.MenuItemValidator;
import net.n2oapp.framework.config.metadata.validation.standard.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.*;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.metadata.validation.standard.regions.*;
import net.n2oapp.framework.config.metadata.validation.standard.widget.CardsValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TableValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TilesValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.columns.BlockValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.columns.FilterColumnValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.columns.MultiColumnValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.columns.SimpleColumnValidator;

/**
 * Набор стандартных валидаторов метаданных
 */
public class N2oAllValidatorsPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.validators(new ObjectValidator(), new QueryValidator(), new PageValidator(),
                new ApplicationValidator(), new SimpleMenuValidator(), new SidebarValidator(),
                new ListFieldValidator(), new SetFieldSetValidator(),
                new FieldSetColValidator(), new FieldSetRowValidator(), new FormValidator(),
                new TableValidator(), new PageActionValidator(), new InvokeActionValidator(), new SetValueValidator(),
                new AlertActionValidator(), new ClearActionValidator(), new CopyActionValidator(), new ConfirmActionValidator(),
                new PrintActionValidator(), new AnchorActionValidator(), new StandardPageValidator(),
                new LeftRightPageValidator(), new SimplePageValidator(), new BasePageValidator(), new SearchablePageValidator(),
                new StandardDatasourceValidator(), new ApplicationDatasourceValidator(), new InheritedDatasourceValidator(),
                new BrowserStorageDatasourceValidator(), new ParentDatasourceValidator(), new CachedDatasourceValidator(),
                new TopLeftRightPageValidator(), new SubPageRegionValidator(),
                new FieldValidator(), new InputTextValidator(), new DateTimeValidator(), new LineFieldSetValidator(),
                new MultiFieldSetValidator(), new JavaDataProviderValidator(), new BaseButtonValidator(), new SubMenuValidator(),
                new ButtonValidator(), new ClipboardButtonValidator(),
                new SubmitActionValidator(), new CustomActionValidator(), new ActionsAwareValidator(), new SwitchActionValidator(),
                new ConditionBranchActionValidator(), new ActionFieldValidator(), new OnFailActionValidator(),
                new EditListActionValidator(), new OnChangeEventValidator(), new MarkdownValidator(), new TilesValidator(),
                new CardsValidator(), new TabsValidator(), new ScrollspyValidator(), new SwitchCellValidator(),
                new LinkCellValidator(), new BadgeCellValidator(), new IconCellValidator(), new EditCellValidator(),
                new ActionCellValidator(),
                new ToolbarCellValidator(), new ListCellValidator(), new MultiColumnValidator(), new SimpleColumnValidator(),
                new FilterColumnValidator(), new BlockValidator(), new DateIntervalValidator(),
                new MenuItemValidator(),
                new NavRegionValidator(), new PanelRegionValidator(), new CustomRegionValidator(), new LineRegionValidator(),
                new ProgressBarCellValidator(), new ProgressValidator(), new StatusValidator(), new AlertFieldValidator());
    }
}
