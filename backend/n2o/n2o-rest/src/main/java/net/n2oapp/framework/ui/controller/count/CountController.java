package net.n2oapp.framework.ui.controller.count;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
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

    public CountController(MetadataEnvironment environment,
                           MetadataRouter router,
                           DataProcessingStack dataProcessingStack,
                           QueryProcessor queryProcessor) {
        super(environment, router);
        this.queryProcessor = queryProcessor;
        this.dataProcessingStack = dataProcessingStack;
    }

    public Integer getCount(HttpServletRequest request, HttpServletResponse response) {
        QueryRequestInfo requestInfo = createQueryRequestInfo(request);
        QueryResponseInfo responseInfo = new QueryResponseInfo();
        responseInfo.setAlertMessageBuilder(getMessageBuilder());
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
