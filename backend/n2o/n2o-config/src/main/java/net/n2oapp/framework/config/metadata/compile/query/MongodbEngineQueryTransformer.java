package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
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
            for (AbstractField field : source.getFields()) {
                if (!Boolean.FALSE.equals(field.getIsSelected()) && field.getSelectExpression() == null)
                    transformSelect(field);
                if (field instanceof QuerySimpleField querySimpleField)
                    transformSimpleField(querySimpleField);
            }
        }
        if (source.getFilters() != null) {
            transformFilters(source.getFilters());
        }
        return source;
    }

    private void transformSimpleField(QuerySimpleField field) {
        if (Boolean.TRUE.equals(field.getIsSorted()) && field.getSortingExpression() == null)
            transformSortings(field);
    }

    private void transformSelect(AbstractField field) {
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
            if (filter.getFilterId() == null)
                filter.setFilterId(RouteUtil.normalizeParam(filter.getFieldId()) + "_" + filter.getType());
            if (filter.getText() == null)
                filter.setText(buildFilterText(filter));
        }
    }

    private String buildFilterText(N2oQuery.Filter filter) {
        if ("id".equals(filter.getFieldId()))
            return filter.getType().equals(FilterTypeEnum.EQ)
                    ? "{ _id: new ObjectId('#" + filter.getFilterId() + "') }"
                    : null;

        String domain = getDomain(filter);
        return switch (filter.getType()) {
            case EQ -> "{ '" + filter.getFieldId() + "': " + getFilterField(filter, domain) + " }";
            case NOT_EQ -> "{ '" + filter.getFieldId() + "': {$ne: " + getFilterField(filter, domain) + " }}";
            case LIKE -> "{ '" + filter.getFieldId() + "': {$regex: '.*" + hash(filter.getFilterId()) + ".*'}}";
            case LIKE_START -> "{ '" + filter.getFieldId() + "': {$regex: '" + hash(filter.getFilterId()) + ".*'}}";
            case MORE -> "{ '" + filter.getFieldId() + "': {$gte: " + getFilterField(filter, domain) + "}}";
            case LESS -> "{ '" + filter.getFieldId() + "': {$lte: " + getFilterField(filter, domain) + "}}";
            case IN -> "{ '" + filter.getFieldId() + "': {$in: " + getFilterField(filter, domain) + "}}";
            case NOT_IN -> "{ '" + filter.getFieldId() + "': {$nin: " + getFilterField(filter, domain) + "}}";
            default -> null;
        };
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
        return Arrays.stream(selection).allMatch(elem -> elem.getInvocation() instanceof N2oMongoDbDataProvider);
    }
}
