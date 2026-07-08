package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.rest.ControllerTypeEnum;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Контроллер получения данных для копирования
 */
public class MergeValuesController extends GetController {
    public MergeValuesController(DataProcessingStack dataProcessingStack,
                                 QueryProcessor queryProcessor,
                                 SubModelsProcessor subModelsProcessor,
                                 AlertMessageBuilder messageBuilder) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder);
    }

    @Override
    public GetDataResponse execute(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        DataSet defaultModel = requestInfo.getData() == null ? new DataSet() : new DataSet(requestInfo.getData());
        if (requestInfo.getQuery() != null) {
            try {
                CollectionPage<DataSet> queryDefaultPage = executeQuery(requestInfo, responseInfo);
                List<DataSet> queryModels = queryDefaultPage.getCollection();
                if (queryModels != null && queryModels.size() > 1) {
                    List<DataSet> mergedModels = new ArrayList<>(queryModels.size());
                    for (DataSet queryModel : queryModels) {
                        DataSet mergedModel = new DataSet(defaultModel);
                        merge(mergedModel, queryModel, requestInfo.getQuery().getCopiedFields());
                        mergedModels.add(mergedModel);
                    }
                    queryDefaultPage.setCollection(mergedModels);
                    return new GetDataResponse(queryDefaultPage, responseInfo, requestInfo.getMessagesForm());
                }
                DataSet queryDefaultModel = queryDefaultPage.getCollection().getFirst();
                merge(defaultModel, queryDefaultModel, requestInfo.getQuery().getCopiedFields());
                return new GetDataResponse(defaultModel, requestInfo.getCriteria(), responseInfo, requestInfo.getMessagesForm());
            } catch (N2oException e) {
                responseInfo.addMessage(getMessageBuilder().build(e, requestInfo));
            }
        }
        defaultModel.remove(QuerySimpleField.PK);//при копировании идентификатор должен быть null, иначе будет изменение
        return new GetDataResponse(defaultModel, requestInfo.getCriteria(), responseInfo, requestInfo.getMessagesForm());
    }


    private static void merge(DataSet defaultModel, DataSet queryModel, Set<String> fieldsToCopy) {
        if (fieldsToCopy != null) {
            defaultModel.merge(queryModel);
            defaultModel.entrySet().removeIf(stringObjectEntry -> !fieldsToCopy.contains(stringObjectEntry.getKey()));
        } else {
            defaultModel.clear();
        }
    }

    @Override
    public ControllerTypeEnum getControllerType() {
        return ControllerTypeEnum.MERGE;
    }
}
