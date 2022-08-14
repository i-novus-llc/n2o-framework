package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.hash;

/**
 * Трансформация query для провайдера данных mongodb.
 * Генерирует тело для фильтров, <select/>, <sorting/>.
 */
@Component
public class MongodbEngineQueryTransformer implements SourceTransformer<N2oQuery>, SourceClassAware {

    @Override
    public N2oQuery transform(N2oQuery source, SourceProcessor p) {
        if (!isMongodb(source))
            return source;
        if (source.getFields() != null) {
            for (QuerySimpleField field : source.getSimpleFields()) {
                if (Boolean.TRUE.equals(field.getIsSelected()) && field.getSelectExpression() == null)
                    transformSelect(field);
                if (Boolean.TRUE.equals(field.getIsSorted()) && field.getSortingExpression() == null)
                    transformSortings(field);
            }
        }
        if (source.getFilters() != null) {
            transformFilters(source.getFilters());
        }
        return source;
    }

    private void transformSelect(QuerySimpleField field) {
        if (field.getId().equals("id")) {
            field.setSelectExpression("_id");
            field.setMapping("['_id'].toString()");
        } else {
            field.setSelectExpression(field.getId());
        }
    }

    private void transformSortings(QuerySimpleField field) {
        if (field.getId().equals("id")) {
            field.setSortingExpression("_id :idDirection");
        } else {
            field.setSortingExpression(field.getId() + " " + colon(field.getId() + "Direction"));
        }
    }

    private void transformFilters(N2oQuery.Filter[] filters) {
        for (N2oQuery.Filter filter : filters) {
            String domain = getDomain(filter);
            if (filter.getFilterId() == null)
                filter.setFilterId(RouteUtil.normalizeParam(filter.getFieldId()) + "_" + filter.getType());
            if (filter.getText() == null) {
                if ("id".equals(filter.getFieldId())) {
                    if (filter.getType().equals(FilterType.eq))
                        filter.setText("{ _id: new ObjectId('#" + filter.getFilterId() + "') }");
                } else {
                    switch (filter.getType()) {
                        case eq:
                            filter.setText("{ '" + filter.getFieldId() + "': " + getFilterField(filter, domain) + " }");
                            break;
                        case notEq:
                            filter.setText("{ '" + filter.getFieldId()  + "': {$ne: " + getFilterField(filter, domain) + " }}");
                            break;
                        case like:
                            filter.setText("{ '" + filter.getFieldId()  + "': {$regex: '.*" + hash(filter.getFilterId()) + ".*'}}");
                            break;
                        case likeStart:
                            filter.setText("{ '" + filter.getFieldId()  + "': {$regex: '" + hash(filter.getFilterId()) + ".*'}}");
                            break;
                        case more:
                            filter.setText("{ '" + filter.getFieldId()  + "': {$gte: " + getFilterField(filter, domain) + "}}");
                            break;
                        case less:
                            filter.setText("{ '" + filter.getFieldId()  + "': {$lte: " + getFilterField(filter, domain) + "}}");
                            break;
                        case in:
                            filter.setText("{ '" + filter.getFieldId()  + "': {$in: " + getFilterField(filter, domain) + "}}");
                            break;
                        case notIn:
                            filter.setText("{ '" + filter.getFieldId()  + "': {$nin: " + getFilterField(filter, domain) + "}}");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private String getFilterField(N2oQuery.Filter filter, String domain) {
        if (domain.equals("string") || domain.equals("date") || domain.equals("localdate") || domain.equals("localdatetime"))
            return "'" + hash(filter.getFilterId()) + "'";
        return hash(filter.getFilterId());
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQuery.class;
    }

    private static String getDomain(N2oQuery.Filter filter) {
        return filter.getDomain() != null ? filter.getDomain().toLowerCase().trim() : "string";
    }

    private boolean isMongodb(N2oQuery source) {
        return checkMondodb(source.getLists())
                && checkMondodb(source.getUniques())
                && checkMondodb(source.getCounts());
    }

    private boolean checkMondodb(N2oQuery.Selection[] selection) {
        if (selection == null) return true;
        return Arrays.stream(selection).noneMatch(elem -> !(elem.getInvocation() instanceof N2oMongoDbDataProvider));
    }
}
