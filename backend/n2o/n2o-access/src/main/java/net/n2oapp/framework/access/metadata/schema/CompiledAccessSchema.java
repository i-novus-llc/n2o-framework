package net.n2oapp.framework.access.metadata.schema;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

@Getter
@Setter
public abstract class CompiledAccessSchema implements Compiled {

    private String id;
}
