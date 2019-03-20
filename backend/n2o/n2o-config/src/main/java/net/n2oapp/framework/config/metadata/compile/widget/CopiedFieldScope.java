package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Используется для передачи copied в компиляцию N2oQueryCompiler
 */
@Getter
@Setter
public class CopiedFieldScope {
    Set<String> copiedFields;
}
