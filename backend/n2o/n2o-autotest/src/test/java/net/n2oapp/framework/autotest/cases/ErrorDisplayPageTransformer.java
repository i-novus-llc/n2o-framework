package net.n2oapp.framework.autotest.cases;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;

public class ErrorDisplayPageTransformer implements SourceTransformer<N2oPage>, SourceClassAware {

    ErrorController controller = new ErrorController();

    @Override
    public N2oPage transform(N2oPage source, SourceProcessor p) {
        switch (source.getName()) {
            case "BadGateway" -> controller.sendBadGateway();
            case "Forbidden" -> controller.sendForbidden();
            case "RouteNotFoundException" -> controller.sendNotFound();
            case "InternalServerError" -> controller.sendInternalServerError();
            default -> {
                return source;
            }
        }
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPage.class;
    }
}
