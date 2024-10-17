package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailAction;

@Component
public class ActionCellValidator implements SourceValidator<N2oActionCell>, SourceClassAware {

    @Override
    public void validate(N2oActionCell source, SourceProcessor p) {
        checkOnFailAction(source.getActions());
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oActionCell.class;
    }
}
