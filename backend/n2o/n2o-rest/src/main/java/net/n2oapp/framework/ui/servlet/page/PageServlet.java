package net.n2oapp.framework.ui.servlet.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.register.route.RoutingResult;
import net.n2oapp.framework.config.reader.PageNotFoundException;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import net.n2oapp.framework.mvc.n2o.N2oServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Сервлет возвращающий страницу по запросу /n2o/page/*
 */
public class PageServlet extends N2oServlet {
    private MetadataRouter router;
    private ReadCompileBindTerminalPipeline pipeline;

    @Override
    public void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        RoutingResult routingResult = router.get(path);
        DataSet data = new DataSet();
        data.putAll(routingResult.getParams());
        req.getParameterMap().forEach((k, v) -> {
            if (v.length == 1) {
                data.put(k, v[0]);
            } else {
                data.put(k, Arrays.asList(v));
            }
        });
        Page page = pipeline.get(routingResult.getContext(Page.class), data);
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
