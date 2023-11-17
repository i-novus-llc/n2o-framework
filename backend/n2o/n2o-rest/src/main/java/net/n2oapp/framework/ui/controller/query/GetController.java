package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.ControllerTypeAware;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.springframework.util.CollectionUtils;

/**
 * Абстрактный контроллер получения данных
 */
public abstract class GetController implements ControllerTypeAware {

    private static final String METADATA_FILE_EXTENSION = ".query.xml";
    private DataProcessingStack dataProcessingStack;
    private QueryProcessor queryProcessor;
    private SubModelsProcessor subModelsProcessor;
    private AlertMessageBuilder messageBuilder;


    protected GetController(DataProcessingStack dataProcessingStack,
                            QueryProcessor queryProcessor,
                            SubModelsProcessor subModelsProcessor,
                            AlertMessageBuilder messageBuilder) {
        this.dataProcessingStack = dataProcessingStack;
        this.queryProcessor = queryProcessor;
        this.subModelsProcessor = subModelsProcessor;
        this.messageBuilder = messageBuilder;
    }

    public abstract GetDataResponse execute(QueryRequestInfo requestScope, QueryResponseInfo responseInfo);


    public CollectionPage<DataSet> executeQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        CollectionPage<DataSet> pageData;
        dataProcessingStack.processQuery(requestInfo, responseInfo);
        try {
            pageData = queryProcessor.execute(requestInfo.getQuery(), requestInfo.getCriteria());
            pageData = executeAdditionalRequest(requestInfo, pageData);
            executeSubModelsRequest(requestInfo, pageData);
        } catch (N2oSpelException e) {
            dataProcessingStack.processQueryError(requestInfo, responseInfo, e);
            throw new N2oSpelException(e, requestInfo.getQuery().getId() + METADATA_FILE_EXTENSION);
        } catch (N2oException e) {
            dataProcessingStack.processQueryError(requestInfo, responseInfo, e);
            throw e;
        } catch (Exception e) {
            throw new N2oException(e);
        }
        dataProcessingStack.processQueryResult(requestInfo, responseInfo, pageData);
        return pageData;
    }

    /**
     * В случае, если запросили страницу, в которой нет данных вычисляем последнюю страницу с данными
     * и возвращаем её в качестве результата
     */
    private CollectionPage<DataSet> executeAdditionalRequest(QueryRequestInfo requestInfo, CollectionPage<DataSet> pageData) {
        if (CollectionUtils.isEmpty(pageData.getCollection()) && pageData.getCriteria().getPage() > 1) {
            requestInfo.getCriteria().setPage((int) Math.ceil((double) pageData.getCount() / pageData.getCriteria().getSize()));
            pageData = queryProcessor.execute(requestInfo.getQuery(), requestInfo.getCriteria());
        }
        return pageData;
    }

    @SuppressWarnings("unchecked")
    private void executeSubModelsRequest(QueryRequestInfo requestInfo, CollectionPage<DataSet> page) {
        if (!page.getCollection().isEmpty() && requestInfo.isSubModelsExists() && requestInfo.getSize() == 1) {
            DataSet dataSet = page.getCollection().iterator().next();
            subModelsProcessor.executeSubModels(requestInfo.getQuery().getSubModelQueries(), dataSet);
        }
    }

    public DataProcessingStack getDataProcessingStack() {
        return dataProcessingStack;
    }

    public QueryProcessor getQueryProcessor() {
        return queryProcessor;
    }

    public SubModelsProcessor getSubModelsProcessor() {
        return subModelsProcessor;
    }

    public AlertMessageBuilder getMessageBuilder() {
        return messageBuilder;
    }
}
