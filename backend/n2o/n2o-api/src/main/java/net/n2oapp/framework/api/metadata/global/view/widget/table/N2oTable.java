package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;


@Getter
@Setter
public class N2oTable extends N2oAbstractTable {
    private FilterPosition filterPosition;
    private Boolean filterOpened;
    private NamespaceUriAware[] filters;
    private Boolean searchButtons;


    public enum FilterPosition {
        top, left;

        public String getName() {
            return this.name();
        }
    }
}
