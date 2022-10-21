package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * Исходная модель действия switch
 */
@Getter
@Setter
public class N2oSwitchAction extends N2oAbstractAction {
    private String valueFieldId;
    private String datasourceId;
    private ReduxModel model;
    private AbstractCase[] cases;

    public List<Case> getValueCases() {
        if (isEmpty(this.cases))
            return new ArrayList<>();
        return Arrays.stream(this.cases).filter(Case.class::isInstance).map(Case.class::cast).collect(Collectors.toList());
    }

    public DefaultCase getDefaultCase() {
        if (isEmpty(this.cases))
            return null;
        return Arrays.stream(this.cases).filter(DefaultCase.class::isInstance).map(DefaultCase.class::cast)
                .findFirst()
                .orElse(null);
    }

    @Getter
    @Setter
    public static class Case extends AbstractCase {
        private String value;
    }

    @Getter
    @Setter
    public static class DefaultCase extends AbstractCase {
    }

    @Getter
    @Setter
    public static abstract class AbstractCase implements ActionsAware, IdAware {
        private String id;
        private String actionId;
        private N2oAction[] actions;
    }
}
