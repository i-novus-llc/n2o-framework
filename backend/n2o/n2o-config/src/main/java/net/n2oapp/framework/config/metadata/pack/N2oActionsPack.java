package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.action.*;
import net.n2oapp.framework.config.metadata.compile.action.condition.ConditionActionBinder;
import net.n2oapp.framework.config.metadata.compile.action.condition.ElseBranchActionCompiler;
import net.n2oapp.framework.config.metadata.compile.action.condition.ElseIfBranchActionCompiler;
import net.n2oapp.framework.config.metadata.compile.action.condition.IfBranchActionCompiler;
import net.n2oapp.framework.config.metadata.compile.cell.SwitchCellBinder;
import net.n2oapp.framework.config.metadata.compile.cell.ToolbarCellBinder;
import net.n2oapp.framework.config.metadata.compile.control.CustomFieldBinder;

public class N2oActionsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oActionsIOV1Pack(), new N2oActionsIOV2Pack());
        b.compilers(new ShowModalCompiler(),
                new OpenDrawerCompiler(),
                new InvokeActionCompiler(),
                new MultiActionCompiler(),
                new CloseActionCompiler(),
                new RefreshActionCompiler(),
                new OpenPageCompiler(),
                new AnchorCompiler(),
                new ClearActionCompiler(),
                new ConfirmActionCompiler(),
                new CopyActionCompiler(),
                new SetValueActionCompiler(),
                new PrintActionCompiler(),
                new AlertActionCompiler(),
                new SubmitActionCompiler(),
                new CustomActionCompiler(),
                new SwitchActionCompiler(),
                new IfBranchActionCompiler(),
                new ElseIfBranchActionCompiler(),
                new ElseBranchActionCompiler(),
                new EditListActionCompiler());
        b.binders(new InvokeActionBinder(),
                new ReduxActionBinder(),
                new LinkActionBinder(),
                new EditListActionBinder(),
                new CopyActionBinder(),
                new ShowModalBinder(),
                new OpenDrawerBinder(),
                new MultiActionBinder(),
                new SwitchActionBinder(),
                new ConditionActionBinder(),
                new PerformButtonBinder(),
                new CustomFieldBinder(),
                new ToolbarCellBinder(),
                new SubMenuBinder(),
                new SwitchCellBinder(),
                new PrintActionBinder(),
                new ButtonFieldBinder(),
                new ActionComponentBinder());
    }
}
