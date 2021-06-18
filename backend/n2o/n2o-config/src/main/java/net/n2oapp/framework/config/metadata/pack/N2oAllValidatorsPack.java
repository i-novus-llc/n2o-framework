package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderValidator;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.metadata.validation.standard.action.InvokeActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.action.PageActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.*;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.ListFieldQueryValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TableValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;

/**
 * Набор стандартных валидаторов метаданных
 */
public class N2oAllValidatorsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.validators(new ObjectValidator(), new QueryValidator(), new PageValidator(),
                new SimpleHeaderValidator(), new SimpleMenuValidator(), new WidgetValidator(),
                new ListFieldQueryValidator(), new SetFieldSetValidator(), new FieldSetColumnValidator(),
                new FieldSetRowValidator(), new FormValidator(), new TableValidator(),
                new PageActionValidator(), new StandardPageValidator(), new InvokeActionValidator(),
                new BasePageValidator(), new FieldValidator(), new LineFieldSetValidator(),
                new MultiFieldSetValidator());
    }
}
