package net.n2oapp.framework.ui.servlet.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import net.n2oapp.framework.mvc.n2o.N2oServlet;

import java.io.IOException;

/**
 * Сервлет возвращающий страницу по запросу /n2o/page/*
 */
public class PageServlet extends N2oServlet {
    private final MetadataRouter router;
    private final ReadCompileBindTerminalPipeline pipeline;
    private final SubModelsProcessor subModelsProcessor;

    public PageServlet(ObjectMapper objectMapper,
                       ClientCacheTemplate clientCacheTemplate,
                       AlertMessagesConstructor messagesConstructor,
                       MetadataRouter router,
                       ReadCompileBindTerminalPipeline pipeline,
                       SubModelsProcessor subModelsProcessor) {
        super(objectMapper, clientCacheTemplate, messagesConstructor);
        this.router = router;
        this.pipeline = pipeline;
        this.subModelsProcessor = subModelsProcessor;
    }

    @Override
    public void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        CompileContext<Page, ?> context = router.get(path, Page.class, req.getParameterMap());
        Page page = pipeline.get(context, context.getParams(path, req.getParameterMap()), subModelsProcessor);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), page);
    }
}