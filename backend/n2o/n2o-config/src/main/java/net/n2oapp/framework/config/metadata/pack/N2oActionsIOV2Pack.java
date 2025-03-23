package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.action.v2.*;
import net.n2oapp.framework.config.io.action.v2.ifelse.ElseBranchActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.ifelse.ElseIfBranchActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.ifelse.IfBranchActionElementIOV2;

/**
 * Набор считывателей действий версии 2.0
 */
public class N2oActionsIOV2Pack implements MetadataPack<XmlIOBuilder<?>> {
    @Override
    public void build(XmlIOBuilder<?> b) {
        b.ios(new InvokeActionElementIOV2(),
                new ShowModalElementIOV2(),
                new OpenPageElementIOV2(),
                new OpenDrawerElementIOV2(),
                new AnchorElementIOV2(),
                new CloseActionElementIOV2(),
                new SetValueElementIOV2(),
                new ConfirmActionElementIOV2(),
                new CopyActionElementIOV2(),
                new ClearActionElementIOV2(),
                new PrintActionElementIOV2(),
                new RefreshActionElementIOV2(),
                new AlertActionElementIOV2(),
                new SubmitActionElementIOV2(),
                new EditListActionElementIOV2(),
                new CustomActionElementIOV2(),
                new SwitchActionElementIOV2(),
                new IfBranchActionElementIOV2(),
                new ElseIfBranchActionElementIOV2(),
                new ElseBranchActionElementIOV2(),
                new OnFailActionElementIOV2());
    }
}
