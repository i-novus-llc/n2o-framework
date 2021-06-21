package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.springframework.core.io.Resource;


/**
 * Базовый класс для тестирования json сборки метаданных
 */
public abstract class JsonMetadataTestBase extends N2oTestBase {

    protected JsonMetadataTester tester;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        tester = new JsonMetadataTester(builder);
    }

    public JsonChecker check(String xmlUri, String jsonUri) {
        return check(xmlUri, new FrontendFileSystemResource(jsonUri));
    }

    public JsonChecker check(String xmlUri, Resource json) {
        CompileInfo info = new CompileInfo(xmlUri);
        builder.sources(info);
        return new JsonChecker(getContext(info), json, tester);
    }

    public JsonChecker check(String jsonUri) {
        return new JsonChecker(new FrontendFileSystemResource(jsonUri), tester);
    }

    public JsonChecker check(Resource json) {
        return new JsonChecker(json, tester);
    }

    public JsonChecker check() {
        return new JsonChecker(tester);
    }

    private CompileContext<?,?> getContext(CompileInfo info) {
        if (N2oPage.class.isAssignableFrom(info.getBaseSourceClass()))
            return new PageContext(info.getId());
        else if (N2oWidget.class.isAssignableFrom(info.getBaseSourceClass()))
            return new WidgetContext(info.getId());
        else if (N2oApplication.class.isAssignableFrom(info.getBaseSourceClass()))
            return new ApplicationContext(info.getId());
        else
            throw new IllegalArgumentException("Unsupported class [" + info.getBaseSourceClass() + "]. Please use assertEquals(context)");
    }

}
