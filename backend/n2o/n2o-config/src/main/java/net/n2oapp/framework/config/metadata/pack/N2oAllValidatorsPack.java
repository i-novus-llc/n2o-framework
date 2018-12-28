package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderValidator;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuValidator;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;

/**
 * Набор стандартных валидаторов метаданных
 */
public class N2oAllValidatorsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.validators(new ObjectValidator(), new QueryValidator(), new PageValidator(),
                new SimpleHeaderValidator(), new SimpleMenuValidator(), new WidgetValidator());
    }
}
