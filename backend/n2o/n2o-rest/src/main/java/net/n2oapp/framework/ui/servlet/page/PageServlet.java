package net.n2oapp.framework.ui.servlet.page;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.mvc.n2o.N2oServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет возвращающий страницу по запросу /n2o/page/*
 */
public class PageServlet extends N2oServlet {
    private MetadataRouter router;
    private ReadCompileBindTerminalPipeline pipeline;

    @Override
    public void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        CompileContext<Page, ?> context = router.get(path, Page.class, req.getParameterMap());
        Page page = pipeline.get(context, context.getParams(path, req.getParameterMap()));
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), page);
    }

    public void setRouter(MetadataRouter router) {
        this.router = router;
    }

    public void setPipeline(ReadCompileBindTerminalPipeline pipeline) {
        this.pipeline = pipeline;
    }

}
