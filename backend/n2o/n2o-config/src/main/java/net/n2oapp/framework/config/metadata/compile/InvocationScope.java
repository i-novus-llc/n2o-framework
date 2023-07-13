package net.n2oapp.framework.config.metadata.compile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvocationScope {

    private String queryId;

    private String objectId;

    private String operationId;

    private String validationId;
}
