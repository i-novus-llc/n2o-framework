package net.n2oapp.framework.autotest.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.HeaderContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.BulkActionController;
import net.n2oapp.framework.ui.controller.action.BulkActionMergeController;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.framework.ui.controller.query.CopyValuesController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import net.n2oapp.framework.ui.controller.query.SimpleDefaultValuesController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    private DataProcessingStack dataProcessingStack;
    private ErrorMessageBuilder errorMessageBuilder;
    private QueryProcessor queryProcessor;
    private N2oOperationProcessor operationProcessor;

    @Value("${n2o.config.path}")
    private String basePath;


    @Autowired
    public N2oController(DataProcessingStack dataProcessingStack, ErrorMessageBuilder errorMessageBuilder,
                         QueryProcessor queryProcessor, N2oOperationProcessor operationProcessor) {
        this.queryProcessor = queryProcessor;
        this.dataProcessingStack = dataProcessingStack;
        this.errorMessageBuilder = errorMessageBuilder;
        this.operationProcessor = operationProcessor;
    }


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

    @GetMapping({"/n2o/data/**", "/n2o/data/", "/n2o/data"})
    public ResponseEntity<GetDataResponse> getData(HttpServletRequest request) {
        String path = getPath(request, "/n2o/data");
        DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

        GetDataResponse response = dataController.getData(path, request.getParameterMap(), null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({"/n2o/data/**", "/n2o/data/", "/n2o/data"})
    public ResponseEntity<SetDataResponse> setData(@RequestBody Map<? extends String, ?> body, HttpServletRequest request) {
        String path = getPath(request, "/n2o/data");
        DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

        SetDataResponse dataResponse = dataController.setData(path, request.getParameterMap(), new DataSet(body), null);
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);

    }

    public void setBuilder(N2oApplicationBuilder builder) {
        this.builder = builder;
    }

    private ControllerFactory createControllerFactory(MetadataEnvironment environment) {
        Map<String, Object> beans = new HashMap<>();
        beans.put("queryController", new QueryController(dataProcessingStack, queryProcessor,
                environment.getSubModelsProcessor(), environment.getMetadataRegister(), errorMessageBuilder));
        beans.put("operationController", new OperationController(dataProcessingStack, environment.getDomainProcessor(),
                operationProcessor, errorMessageBuilder));
        beans.put("copyValuesController", new CopyValuesController(dataProcessingStack, queryProcessor, environment.getSubModelsProcessor(),
                environment.getMetadataRegister(), errorMessageBuilder));
        beans.put("bulkActionController", new BulkActionController(dataProcessingStack, environment.getDomainProcessor(),
                operationProcessor));
        beans.put("simpleDefaultValuesController", new SimpleDefaultValuesController(dataProcessingStack, queryProcessor,
                environment.getSubModelsProcessor(), environment.getMetadataRegister(), errorMessageBuilder));
        beans.put("bulkActionMergeController", new BulkActionMergeController(dataProcessingStack,
                environment.getDomainProcessor(), operationProcessor));
        ControllerFactory factory = new N2oControllerFactory(beans);
        return factory;
    }

    private String getPath(HttpServletRequest request, String prefix) {
        String path = request.getRequestURI().substring(request.getRequestURI().indexOf(prefix) + prefix.length());
        return RouteUtil.normalize(!path.isEmpty() ? path : "/");
    }
}
