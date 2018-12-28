package net.n2oapp.framework.ui.controller.count;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CountController extends AbstractController {
    private DataProcessingStack dataProcessingStack;
    private QueryProcessor queryProcessor;

    public CountController(ObjectMapper objectMapper,
                           MetadataRouter router,
                           ReadCompileBindTerminalPipeline pipeline,
                           DomainProcessor domainProcessor,
                           DataProcessingStack dataProcessingStack,
                           QueryProcessor queryProcessor) {
        super(objectMapper, router, pipeline, domainProcessor);
        this.queryProcessor = queryProcessor;
        this.dataProcessingStack = dataProcessingStack;
    }

    public Integer getCount(HttpServletRequest request, HttpServletResponse response) {
        QueryRequestInfo requestInfo = createQueryRequestInfo(request);
        QueryResponseInfo responseInfo = new QueryResponseInfo();
        //результат не процессится
        dataProcessingStack.processQuery(requestInfo, responseInfo);
        try {
            return queryProcessor.executeCount(requestInfo.getQuery(), requestInfo.getCriteria());

        } catch (N2oException e) {
            dataProcessingStack.processQueryError(requestInfo, responseInfo, e);
            throw e;
        }
    }


}
