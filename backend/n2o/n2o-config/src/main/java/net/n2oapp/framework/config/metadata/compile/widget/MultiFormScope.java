package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Маркер компиляции внутри виджета MultiForm.
 * Передаётся через CompileProcessor как scope.
 */
@Getter
@AllArgsConstructor
public class MultiFormScope {
    private boolean inner;
    private String datasourceId;
}
