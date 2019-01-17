package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;


/**
 * User: iryabov
 * Date: 05.02.13
 * Time: 18:30
 */

@Getter
@Setter
public class N2oTable extends N2oAbstractTable<N2oTable> {
    private FilterPosition filterPosition;
    private Boolean filterOpened;
    private NamespaceUriAware[] filters;
    private Boolean searchButtons;


    @Override
    public boolean isNavSupport() {
        return true;
    }

    public static enum FilterPosition {
        top, left;

        public String getName() {
            return this.name();
        }
    }
}
