package net.n2oapp.framework.config.metadata.compile.context;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.N2oApplication;

/**
 * Контекст сборки приложения
 */
public class ApplicationContext extends BaseCompileContext<Application, N2oApplication> {

    public ApplicationContext(String sourceId) {
        super(sourceId, N2oApplication.class, Application.class);
    }
}
