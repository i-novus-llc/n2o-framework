package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.springframework.stereotype.Controller;

import java.util.Set;

/**
 * Контроллер получения данных для копирования
 */
@Controller
public class CopyValuesController extends DefaultValuesController {
    public CopyValuesController(DataProcessingStack dataProcessingStack,
                                QueryProcessor queryProcessor,
                                SubModelsProcessor subModelsProcessor,
                                AlertMessageBuilder messageBuilder,
                                MetadataEnvironment environment) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder, environment);
    }

    @Override
    public GetDataResponse execute(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        DataSet defaultModel = extractCopyModel(requestInfo, responseInfo);
        return new GetDataResponse(defaultModel, requestInfo.getCriteria(), responseInfo, requestInfo.getMessagesForm());
    }

    protected DataSet extractCopyModel(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        DataSet defaultModel = extractDefaultModel(requestInfo, responseInfo);
        if (requestInfo.getQuery() != null) {
            CollectionPage<DataSet> queryDefaultPage;
            try {
                queryDefaultPage = executeQuery(requestInfo, responseInfo);
                DataSet queryDefaultModel = queryDefaultPage.getCollection().iterator().next();
                merge(defaultModel, queryDefaultModel, requestInfo.getQuery().getCopiedFields());
                return defaultModel;
            } catch (N2oException e) {
                responseInfo.addMessage(getMessageBuilder().build(e, requestInfo));
            }
        }
        defaultModel.remove(N2oQuery.Field.PK);//при копировании идентификатор должен быть null, иначе будет изменение
        return defaultModel;
    }


    @SuppressWarnings("unchecked")
    private static void merge(DataSet defaultModel, DataSet queryModel, Set<String> fieldsToCopy) {
        if (fieldsToCopy != null) {
            defaultModel.merge(queryModel);
            defaultModel.entrySet().removeIf(stringObjectEntry -> !fieldsToCopy.contains(stringObjectEntry.getKey()));
        } else {
            defaultModel.clear();
        }
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.copy;
    }
}
