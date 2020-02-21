package net.n2oapp.framework.autotest.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.HeaderContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер n2o запросов для автотестов
 */
@RestController
public class N2oController {

    private N2oApplicationBuilder builder;

    @Value("${n2o.config.path}")
    private String basePath;


    @GetMapping("/n2o/config")
    public Map config() {
        Map<String, Object> config = new HashMap<>();
        List<SourceInfo> headers = builder.getEnvironment().getMetadataRegister().find(N2oHeader.class);
        config.put("menu", builder.read().transform().validate().compile().transform().bind().get(new HeaderContext(headers.get(headers.size() - 1).getId()), new DataSet()));
        return config;
    }

    @GetMapping({"/n2o/page/**", "/n2o/page/", "/n2o/page"})
    public Page page(HttpServletRequest request) {
        String path = getPath(request, "/n2o/page");
        CompileContext<Page, ?> context = builder.route(path, Page.class, request.getParameterMap());
        return builder.read().transform().validate().compile().transform().bind().get(context, context.getParams(path, request.getParameterMap()));

    }

    private String getPath(HttpServletRequest request, String prefix) {
        String path = request.getRequestURI().substring(request.getRequestURI().indexOf(prefix) + prefix.length());
        return RouteUtil.normalize(!path.isEmpty() ? path : "/");
    }

    public void setBuilder(N2oApplicationBuilder builder) {
        this.builder = builder;
    }

}
